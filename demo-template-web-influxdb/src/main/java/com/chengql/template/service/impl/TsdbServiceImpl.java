package com.chengql.template.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.chengql.template.domain.PointCollectData;
import com.chengql.template.query.PointQuery;
import com.chengql.template.service.TsdbService;

@Service
public class TsdbServiceImpl implements TsdbService {
	private static final Logger logger = LoggerFactory.getLogger(TsdbServiceImpl.class);

	private static final InfluxDB.ConsistencyLevel CONSISTENCY_LEVEL = InfluxDB.ConsistencyLevel.ANY;

	private static final TimeUnit PRECESION = TimeUnit.SECONDS;

	@Value("${spring.influx.url:''}")
	private String influxDBUrl;

	/**
	 * 用户名
	 */
	@Value("${spring.influx.user:''}")
	private String userName;
	/**
	 * 密码
	 */
	@Value("${spring.influx.password:''}")
	private String password;
	/**
	 * 默认数据库
	 */
	@Value("${spring.influx.database:''}")
	private String  defalutDatabase;

	/**
	 * 保留策略
	 */
	@Value("${spring.influx.retentionpolicy}")
	private String retentionPolicy;
	
	/**
	 * 默认表名
	 */
	@Value("${spring.influx.default.table.name}")
	private String defaultTableName;

	private InfluxDB influxDB;

	
	@PostConstruct
	public void init() {
		influxDB = InfluxDBFactory.connect(influxDBUrl, userName, password);

		try {
			// 如果指定的数据库不存在，则新建一个新的数据库，并新建一个默认的数据保留规则
			if (!this.databaseExist(defalutDatabase)) {
				createDatabase(defalutDatabase);
				createRetentionPolicy();
			}
		} catch (Exception e) {
			// 该数据库可能设置动态代理，不支持创建数据库
			logger.error("[TsdbService] occur error when init tsdb, err msg: {}", e);
		} finally {
			influxDB.setRetentionPolicy(retentionPolicy);
		}

		influxDB.setLogLevel(InfluxDB.LogLevel.NONE);
		 // Flush every 1000 Points, at least every 100ms
        // bufferLimit represents the maximum number of points can stored in the retry buffer
        // exceptionHandler represents a consumer function to handle asynchronous errors
        // threadFactory represents the ThreadFactory instance to be used
        influxDB.enableBatch(BatchOptions.DEFAULTS
                .actions(1000)
                .flushDuration(100)
                .bufferLimit(10)
                .exceptionHandler((points, e) -> {
                    List<Point> target = new ArrayList<>();
                    points.forEach(target::add);
                    String msg = String.format("failed to write points:%s\n", target.toString().substring(0, 10000));
                    logger.error(msg, e);
                })
                .threadFactory(
                        Executors.defaultThreadFactory()
                ));
	}

	/**
	 * 测试连接是否正常
	 * 
	 * @return true 正常
	 */
	public boolean ping() {
		boolean isConnected = false;
		Pong pong;
		try {
			pong = influxDB.ping();
			if (pong != null) {
				isConnected = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isConnected;
	}

	@Override
	public void createDatabase(String database) {
		influxDB.query(new Query("CREATE DATABASE " + database, ""));
	}

	@Override
	public void dropDatabase(String database) {
		influxDB.query(new Query("DROP DATABASE " + database, ""));
	}

	@Override
	public boolean databaseExist(String database) {
		return influxDB.databaseExists(database);
	}

	@Override
	public void dropRetentionPolicy() {
		this.dropRetentionPolicy(defalutDatabase, retentionPolicy,"default");
	}
	/**
	 * 创建默认的保留策略
	 *
	 * @param 策略名：default，保存天数：30天，保存副本数量：1 设为默认保留策略
	 */
	@Override
	public void createRetentionPolicy() {
		String command = String.format("CREATE RETENTION POLICY %s ON %s DURATION %s REPLICATION %s DEFAULT",
				 "default", defalutDatabase, "30d", 1);
				 this.query(command);
	}

	/**
	 * 创建自定义保留策略
	 *
	 * @param policyName  策略名
	 * @param duration    保存天数
	 * @param replication 保存副本数量
	 * @param isDefault   是否设为默认保留策略
	 */
	@Override
	public void createRetentionPolicy(String database, String policyName, String duration, int replication,
			Boolean isDefault) {
		String sql = String.format("CREATE RETENTION POLICY %s ON %s DURATION %s REPLICATION %s ", policyName, database,
				duration, replication);
		if (isDefault) {
			sql = sql + " DEFAULT";
		}
		this.query(sql);

	}


	@Override
	public void dropRetentionPolicy(String database, String retentionPolicy,String policyName) {
		//drop retention POLICY "2_hours" ON "telegraf"
		String sql = String.format("drop retention POLICY %s ON %s ", policyName, retentionPolicy);
		this.query(sql, database);

	}

	@Override
	public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields, long time,
			TimeUnit timeUnit) {
		Builder builder = Point.measurement(measurement);
		builder.tag(tags);
		builder.fields(fields);
		if (0 != time) {
			builder.time(time, timeUnit);
		}
		influxDB.write(defalutDatabase, retentionPolicy, builder.build());
	}

	@Override
	public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields, long time,
			TimeUnit timeUnit, String retentionPolicy) {
		Builder builder = Point.measurement(measurement);
		builder.tag(tags);
		builder.fields(fields);
		if (0 != time) {
			builder.time(time, timeUnit);
		}
		influxDB.write(defalutDatabase, retentionPolicy, builder.build());
	}

	/**
	 * 查询
	 *
	 * @param command 查询语句
	 * @return
	 */
	@Override
	public QueryResult query(String command) {
		return influxDB.query(new Query(command, defalutDatabase));
	}

	@Override
	public QueryResult query(String command, String database) {
		return influxDB.query(new Query(command, database));
	}

	@Override
	public QueryResult query(PointQuery pointQuery) {
		String measurement = ("".equals(pointQuery.getMeasurement())||null==pointQuery.getMeasurement())?defaultTableName:pointQuery.getMeasurement();
		String database = ("".equals(pointQuery.getDatabase())||null==pointQuery.getDatabase())?defalutDatabase:pointQuery.getDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select  * FROM  "+measurement);
		sqlParameJudgment(pointQuery,sql);
		sql.append(" tz('Asia/Shanghai') ");
		logger.info("SQL {}",sql.toString());
		return  influxDB.query(new Query(sql.toString(), database));
	}

	@Override
	public QueryResult count(PointQuery pointQuery) {
		String measurement = ("".equals(pointQuery.getMeasurement())||null==pointQuery.getMeasurement())?defaultTableName:pointQuery.getMeasurement();
		String database = ("".equals(pointQuery.getDatabase())||null==pointQuery.getDatabase())?defalutDatabase:pointQuery.getDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) FROM  "+measurement);
		sqlParameJudgment( pointQuery,sql);
		logger.info("SQL {}",sql.toString());
		sql.append(" tz('Asia/Shanghai') ");
		return  influxDB.query(new Query(sql.toString(), database));
	}
	
	/**
	 * @param pointQuery
	 * @param sql
	 * SQL语句参数组装
	 */
	private void sqlParameJudgment(PointQuery pointQuery,StringBuilder sql) {
		if(StringUtils.isNotBlank(pointQuery.getStartTime())||StringUtils.isNotBlank(pointQuery.getEndTime())||null != pointQuery.getTags()|| null != pointQuery.getStatus()) {
			sql.append(" WHERE ");
		}
		boolean andFlag = false;
		
		if(StringUtils.isNotBlank(pointQuery.getStartTime())) {
			sql.append(" time>= '"+pointQuery.getStartTime()+"'");
			andFlag = true;
		}
		if(StringUtils.isNotBlank(pointQuery.getEndTime())) {
			if(andFlag) {
				sql.append(" and ");
			}
			sql.append(" time<= '"+pointQuery.getEndTime()+"'");
			andFlag = true;
		}
		
		if(null != pointQuery.getTags()) {
			if(andFlag) {
				sql.append(" and ");
			}
			sql.append(" ( ");
			boolean tagsFlag = false;
			for(String tags:pointQuery.getTags()) {
				if(tagsFlag) {
					sql.append(" OR ");
				}
				sql.append(" tags ='"+tags+"'");
				if(!tagsFlag) {
					tagsFlag = true;
				}
			}
			sql.append(" ) ");
			andFlag = true;
		}
		if(null != pointQuery.getStatus()) {
			if(andFlag) {
				sql.append(" and ");
			}
			sql.append(" ( ");
			boolean statusFlag = false;
			for(String status:pointQuery.getStatus()) {
				if(statusFlag) {
					sql.append(" OR ");
				}
				sql.append(" status ='"+status+"'");
				if(!statusFlag) {
					statusFlag = true;
				}
			}
			sql.append(" ) ");
			andFlag = true;
		}
		if(StringUtils.isNotBlank(pointQuery.getDescColumn())||StringUtils.isNotBlank(pointQuery.getAscColumn())) {
			sql.append(" ORDER BY ");
			boolean symbolFlag = false;
			if(StringUtils.isNotBlank(pointQuery.getDescColumn())) {
				sql.append(pointQuery.getDescColumn()+ " DESC ");
				symbolFlag = true;
			}
			if(StringUtils.isNotBlank(pointQuery.getAscColumn())) {
				if(symbolFlag) {
					sql.append(",");
				}
				sql.append(pointQuery.getAscColumn()+ " ASC ");
			}
		}
	}

	@Override
	public QueryResult queryPage(PointQuery pointQuery) {
		/* LIMIT rows OFFSET (page - 1)*rows*/
		String measurement = ("".equals(pointQuery.getMeasurement())||null==pointQuery.getMeasurement())?defaultTableName:pointQuery.getMeasurement();
		String database = ("".equals(pointQuery.getDatabase())||null==pointQuery.getDatabase())?defalutDatabase:pointQuery.getDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select  * FROM  "+measurement);
		sqlParameJudgment(pointQuery,sql);
		sql.append(" LIMIT "+pointQuery.getSize()+" OFFSET "+((pointQuery.getPage() - 1)*pointQuery.getSize()));
		sql.append(" tz('Asia/Shanghai') ");
		logger.info("SQL {}",sql.toString());
		return  influxDB.query(new Query(sql.toString(), database));
	}

	@Override
	public void insertBatch(List<PointCollectData> pointCollectDataList) {
		BatchPoints batchPoints = BatchPoints.database(defalutDatabase)
	                .retentionPolicy(retentionPolicy)
	                .build();
		Point  point = null;
		for(PointCollectData pointCollectData : pointCollectDataList) {
             point = Point.measurement(defaultTableName)
                    .time(pointCollectData.getTime(), pointCollectData.getTimeUnit())
                    .addField("value",pointCollectData.getValue())
                    .tag("tags",pointCollectData.getTags())
                    .tag("status",pointCollectData.getStatus())
                    .build();
             batchPoints.point(point);
		}

		influxDB.write(batchPoints);
	}

	@Override
	public void insertBatch(String measurement, Map<String, String> tags, Map<String, Object> fields, long time,
			TimeUnit timeUnit, String retentionPolicy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() {
		influxDB.flush();
	}
	
	


}
