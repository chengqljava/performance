package com.chengql.template.query;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class PointQuery implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String startTime;
	private String endTime;
	/*
	 * 标签
	 */
	private List<String> tags;
   /*
   * 状态
    */
	private List<String> status;
	/*表名*/
	private String measurement;
	/*数据库*/
	private String database;
	/*
	 * 列名
	 */
	private String descColumn;
	/*
	 * 列名
	 */
	private String ascColumn;
	
	private int page=1;
	private int size =10;

}
