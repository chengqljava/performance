package com.chengql.template.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.QueryResult;

import com.chengql.template.domain.PointCollectData;
import com.chengql.template.query.PointQuery;

public interface TsdbService {
	/*
	 * 创建数据库
	 */
	void createDatabase(String database);

	/*
	 * 删除数据库
	 */
	void dropDatabase(String database);

	/*
	 * 数据库是否存在
	 */
	boolean databaseExist(String database);

	/*
	 * 创建保留策略
	 */
	void createRetentionPolicy();

	/*
	 * 创建保留策略
	 */
	void createRetentionPolicy(String database, String policyName, String duration, int replication, Boolean isDefault);

	/*
	 * 删除保留策略
	 */
	void dropRetentionPolicy();

	/*
	 * 删除保留策略
	 */
	void dropRetentionPolicy(String database, String retentionPolicy, String policyName);

	/*
	 * 插入数据
	 * 
	 * @ measurement 表名
	 * 
	 * @ tags 标签
	 * 
	 * @ fields
	 * 
	 * @ time 时间
	 * 
	 * @ TimeUnit类型
	 * 
	 */
	void insert(String measurement, Map<String, String> tags, Map<String, Object> fields, long time, TimeUnit timeUnit);

	/*
	 * 插入数据
	 * 
	 * @ measurement 表名
	 * 
	 * @ tags 标签
	 * 
	 * @ fields
	 * 
	 * @ time 时间
	 * 
	 * @ TimeUnit类型
	 * 
	 * @ retentionPolicy 保留策略
	 */
	void insert(String measurement, Map<String, String> tags, Map<String, Object> fields, long time, TimeUnit timeUnit,
			String retentionPolicy);
	/*
	 * 批量插入数据
	 * 
	 * @ measurement 表名
	 * 
	 * @ tags 标签
	 * 
	 * @ fields
	 * 
	 * @ time 时间
	 * 
	 * @ TimeUnit类型
	 * 建议5000条以下最多不要超过10000条
	 */
	void insertBatch(List<PointCollectData> pointCollectDataList);

	/*
	 * 批量插入数据
	 * 
	 * @ measurement 表名
	 * 
	 * @ tags 标签
	 * 
	 * @ fields
	 * 
	 * @ time 时间
	 * 
	 * @ TimeUnit类型
	 * 
	 * @ retentionPolicy 保留策略
	 * 建议5000条以下最多不要超过10000条
	 */
	void insertBatch(String measurement, Map<String, String> tags, Map<String, Object> fields, long time, TimeUnit timeUnit,
			String retentionPolicy);
	/**
	 * 查询
	 *
	 * @param command 查询语句
	 * @return
	 */
	QueryResult query(String command);

	/**
	 * 查询
	 *
	 * @param command  查询语句
	 * @param database 数据库
	 * @return
	 */
	QueryResult query(String command, String database);

	/**
	 * 查询
	 *
	 * @param pointQuery 查询条件
	 * @return
	 */
	QueryResult query(PointQuery pointQuery);

	/**
	 * 查询
	 *
	 * @param pointQuery 查询条件
	 * @return
	 */
	QueryResult queryPage(PointQuery pointQuery);

	/**
	 * 查询
	 *
	 * @param pointQuery 查询条件
	 * @return
	 */
	QueryResult count(PointQuery pointQuery);
	
	void flush();

}
