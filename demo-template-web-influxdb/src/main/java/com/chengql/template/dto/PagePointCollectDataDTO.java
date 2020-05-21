package com.chengql.template.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class PagePointCollectDataDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long total;
	private List<PointCollectDataDTO> pointCollectDataList;

}
