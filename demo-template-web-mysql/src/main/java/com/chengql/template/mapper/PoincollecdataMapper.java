package com.chengql.template.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengql.template.domain.Poincollecdata;
import com.chengql.template.domain.query.PoincollecdataQuery;

/**
 * 
 * 
 * @author chengql
 * @email chengql@hanwei.cn
 * @date 2020-05-20 13:43:04
 */
public interface PoincollecdataMapper extends BaseMapper<Poincollecdata>{
	
	List<Poincollecdata> list(PoincollecdataQuery poincollecdataQuery);
	List<Poincollecdata> listSize(@Param("size")int size);
	
	
}
