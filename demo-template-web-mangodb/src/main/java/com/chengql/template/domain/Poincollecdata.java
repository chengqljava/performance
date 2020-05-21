package com.chengql.template.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;




/**
 * 
 * 
 * @author chengql
 * @email chengql@hanwei.cn
 * @date 2020-05-20 13:43:04
 */
@Data
@Document(collection="poincollec_data")
public class Poincollecdata implements Serializable {

	private static final long serialVersionUID = 1L;
	
	  /**
	   * 
	   */
	private String tags;
	  /**
	   * 
	   */
	private String status;
	  /**
	   * 
	   */
	private String value;
	  /**
	   * 
	   */
	private Date collectTime;

}
