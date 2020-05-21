package com.chengql.template.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chengql.template.dto.PagePointCollectDataDTO;
import com.chengql.template.dto.PointCollectDataDTO;
/*
 * 数据转化
 */
public class DataTransformationUtil {
	
	private static final String TOTAL_CLOUMN="count_value";

	/*
	 * 点位数据
	 * @param pointResult 点位信息
	 */
	public static List<PointCollectDataDTO> pointCollectData(QueryResult queryResult) {
		List<PointCollectDataDTO>  list = new ArrayList<>();
		if(!queryResult.hasError()) {
			List<Result> resultList = queryResult.getResults();
			List<Series> seriesList = null;
			List<String> columns = null;
			List<List<Object>> values = null;
			List<Map<String,Object>> mapList = new ArrayList<>();
			Map<String,Object> cloumnValue = null;
			for(Result result : resultList) {
				if(!result.hasError()) {
				  seriesList = result.getSeries();
				  for(Series series : seriesList) {
					  columns = series.getColumns();
					  values = series.getValues();
					  for(int valuesIndex=0;valuesIndex<values.size();valuesIndex++) {
						  cloumnValue = new HashMap<>();
						  for(int index=0;index<columns.size();index++) {
							  cloumnValue.put(columns.get(index), values.get(valuesIndex).get(index));
						  }  
						  mapList.add(cloumnValue);
					  }
					 
				  }
				}
				
			}
			//转化
			list =	JSONArray.parseArray(JSONObject.toJSONString(mapList), PointCollectDataDTO.class);
		}
		
		return list;
	}
	
	/*
	 * 分页数据
	 * @param pageResult 分页信息
	 * @param pointResult 点位信息
	 */
	public static PagePointCollectDataDTO pagePointCollectData(QueryResult pageResult,QueryResult pointResult) {
		PagePointCollectDataDTO  pagePointCollectDataDTO = new PagePointCollectDataDTO();
		long total =0L;
		if(!pageResult.hasError()) {
			List<Result> resultList = pageResult.getResults();
			List<Series> seriesList = null;
			List<String> columns = null;
			List<List<Object>> values = null;
			for(Result result : resultList) {
				if(!result.hasError()) {
				  seriesList = result.getSeries();
				  for(Series series : seriesList) {
					  columns = series.getColumns();
					  values = series.getValues();
					  for(int valuesIndex=0;valuesIndex<values.size();valuesIndex++) {
						  for(int index=0;index<columns.size();index++) {
							  if(TOTAL_CLOUMN.equals(columns.get(index))) {
								  total =   new Double(values.get(valuesIndex).get(index).toString()).longValue();
								  break;
							  }
						  }  
					  }
					 
				  }
				}
				
			}
		}
		pagePointCollectDataDTO.setTotal(total);
		pagePointCollectDataDTO.setPointCollectDataList(pointCollectData(pointResult));
		return pagePointCollectDataDTO;
	}
}
