package com.chengql.template.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chengql.template.domain.PointCollectData;
import com.chengql.template.dto.PagePointCollectDataDTO;
import com.chengql.template.dto.PointCollectDataDTO;
import com.chengql.template.query.PointQuery;
import com.chengql.template.service.TsdbService;
import com.chengql.template.util.DataTransformationUtil;

@RestController
@RequestMapping(value="/point")
public class PointController {
	@Autowired
	private  TsdbService tsdbService;
     /*
      * heleo
      */
	@RequestMapping(value="/save")
	public QueryResult save() {
		Map<String,String> tags = new HashMap<>();
		tags.put("tags", "heleo");
		tags.put("status", "normal");
		Map<String,Object> fields = new HashMap<>();
		fields.put("value", "world");
		System.out.println(new Date().getTime());
		tsdbService.insert("point_collect_data", tags, fields, System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		
		QueryResult queryResult =tsdbService.query(" select * from point_collect_data");
		return queryResult;
	}
	
	@RequestMapping(value="/list")
	public QueryResult list(PointQuery pointQuery) {
		long currentTime = System.currentTimeMillis();
		QueryResult queryResult =tsdbService.query(pointQuery);
		long lastTime = System.currentTimeMillis();
		System.out.println("读取"+pointQuery.getSize()+"条耗时"+(lastTime-currentTime)+"毫秒");
		return queryResult;
	}
	@RequestMapping(value="/count")
	public QueryResult count(PointQuery pointQuery) {
		long currentTime = System.currentTimeMillis();
		QueryResult queryResult =tsdbService.count(pointQuery);
		long lastTime = System.currentTimeMillis();
		System.out.println("读取"+pointQuery.getSize()+"条耗时"+(lastTime-currentTime)+"毫秒");
		return queryResult;
	}
	@RequestMapping(value="/page")
	public QueryResult page(PointQuery pointQuery) {
		long currentTime = System.currentTimeMillis();
		QueryResult queryResult =tsdbService.queryPage(pointQuery);
		long lastTime = System.currentTimeMillis();
		System.out.println("读取"+pointQuery.getSize()+"条耗时"+(lastTime-currentTime)+"毫秒");
		return queryResult;
	}
	
	@RequestMapping(value="/batch")
	public QueryResult batch() throws InterruptedException {
		List<PointCollectData> pointCollectDataList = new ArrayList<>();
		PointCollectData  pointCollectData = null;
		for(int i=0;i<5000;i++) {
			pointCollectData = new PointCollectData();
			pointCollectData.setStatus(i/2==0?"normal":"lowwarn");
			pointCollectData.setTags("tags"+i%3);
			pointCollectData.setTime(System.currentTimeMillis());
			pointCollectData.setValue("value"+i);
			pointCollectData.setTimeUnit(TimeUnit.MILLISECONDS);
			pointCollectDataList.add(pointCollectData);
			Thread.sleep(1);
		}
		tsdbService.insertBatch(pointCollectDataList);
	    
		QueryResult queryResult =tsdbService.query(" select * from point_collect_data");
		return queryResult;
	}

	
	@RequestMapping(value="/page/dto")
	public List<PointCollectDataDTO> pagePointCollectDataDTO(PointQuery pointQuery) {
		List<String> tags = new ArrayList<>();
		tags.add("heleo");
		tags.add("normal");
        pointQuery.setMeasurement("point_collect_data");	
        pointQuery.setDescColumn("time");
        pointQuery.setSize(4);
        pointQuery.setPage(2);
		QueryResult queryResult =tsdbService.queryPage(pointQuery);
	
		return DataTransformationUtil.pointCollectData(queryResult);
	}
	
	@RequestMapping(value="/page/count")
	public PagePointCollectDataDTO pageCount(PointQuery pointQuery) {
        pointQuery.setMeasurement("point_collect_data");	
        pointQuery.setDescColumn("time");
        pointQuery.setSize(10);
        pointQuery.setPage(2);
		QueryResult pointResult =tsdbService.queryPage(pointQuery);
		QueryResult pageResult = tsdbService.count(pointQuery);
		return DataTransformationUtil.pagePointCollectData(pageResult, pointResult);
	}
	@RequestMapping(value="/performance/analysis/{dataSize}")
	public String performanceAnalysis(@PathVariable("dataSize") int dataSize){
		long currentTime = System.currentTimeMillis();
		Map<String,String> tags = new HashMap<>();
		tags.put("tags", "heleo");
		tags.put("status", "normal");
		Map<String,Object> fields = new HashMap<>();
		fields.put("value", "world");
		for(int i=0;i<dataSize;i++) {
			tags = new HashMap<>();
			tags.put("tags", "Tpoint"+(i%9999));
			tags.put("status", "Tnormal");
			fields = new HashMap<>();
			fields.put("value", i);
			tsdbService.insert("point_collect_data", tags, fields, System.currentTimeMillis()+i, TimeUnit.MILLISECONDS);
		}
		long lastTime = System.currentTimeMillis();
		System.out.println("新增"+dataSize+"条耗时"+(lastTime-currentTime)+"毫秒");
		return "新增"+dataSize+"条耗时"+(lastTime-currentTime)+"毫秒";
	}
	@RequestMapping(value="/flush")
	public String flush() {
		tsdbService.flush();
		return "flush";
	}
}
