package com.chengql.template.controller.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


/**
*
*
*/
@Data
public class PoincollecdataDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
   /**
   *
   **/
	private String tags;
	
   /**
   *
   **/
	private String status;
	
   /**
   *
   **/
	private String value;
	
   /**
   *
   **/
	private Date collectTime;
	

}
