package com.chengql.template.dto;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import lombok.Data;
@Data
public class PointCollectDataDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * 位编号
	 */
	private String tags;
	/*
	 * normal 正常 highwarn 高报 lowwar低报
	 */
	private String status;
	/*
	 * 采集值
	 */
	private String value;
	/*
	 * 采集时间
	 */
	private String time;
	
	/*
	 * 默认秒
	 */
	private TimeUnit timeUnit=TimeUnit.SECONDS;
}
