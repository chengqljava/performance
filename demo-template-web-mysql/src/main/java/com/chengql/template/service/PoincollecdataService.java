package com.chengql.template.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chengql.template.domain.Poincollecdata;
import com.chengql.template.domain.query.PoincollecdataQuery;
import com.chengql.template.mapper.PoincollecdataMapper;


/**
 * 
 * 
 * @author chengql
 * @email chengql@hanwei.cn
 * @date 2020-05-20 13:43:04
 */
@Service
@Transactional(readOnly = true)
public class PoincollecdataService extends
		ServiceImpl<PoincollecdataMapper, Poincollecdata>{

	
	/** 插入一条记录
     *
     * @param entity 实体对象*/
    @Transactional
	public boolean  savePoincollecdata(Poincollecdata poincollecdata){
	  //TODO验证参数有效性
	  return  baseMapper.insert(poincollecdata)>0;
	}
	/** 插入批量条记录
     *
     * @param entity 实体对象*/
    @Transactional
   public	boolean saveBatch(List<Poincollecdata> poincollecdatas){
	  //TODO验证参数有效性
	  return this.saveOrUpdateBatch(poincollecdatas,poincollecdatas.size());
	}
	
	 /**
     * 根据 ID 修改
     *
     * @param entity 实体对象
     */
     @Transactional
	public boolean updateById(Poincollecdata poincollecdata){
	  //TODO验证参数有效性
	  return baseMapper.updateById(poincollecdata)>0;
	}
	
	/**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Transactional
   public	boolean deleteById(Serializable id){
	  //TODO验证参数有效性
	  return baseMapper.deleteById(id)>0;
	}
	
	 /**
     * 删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     */
    @Transactional
	public boolean deleteByIds(Collection<? extends Serializable> idList){
	 //TODO验证参数有效性
	 return baseMapper.deleteBatchIds(idList)==idList.size();
	}
	/**
     * 根据 ID 查找
     *
     * @param id 主键ID
     */
	public Poincollecdata selectById(Serializable id){
	  //TODO验证参数有效性
	 return baseMapper.selectById(id);
	}
	/**
     * 根据 ID 查找批量查找
     *
     * @param idList 主键ID
     */
	public List<Poincollecdata> selectBatchIds(Collection<? extends Serializable> idList){
	   //TODO验证参数有效性
	   return baseMapper.selectBatchIds(idList);
	}
	
	/**
     * 根据 Query条件 查找列表
     *
     * @param poincollecdataQuery 主键ID
     */
	public List<Poincollecdata> list(PoincollecdataQuery poincollecdataQuery){
	 //TODO验证参数有效性
	 poincollecdataQuery.setSize(-1);
	  return baseMapper.list(poincollecdataQuery);
	}
	
	public Page<Poincollecdata> listPage(PoincollecdataQuery poincollecdataQuery){
	 //TODO验证参数有效性
	 poincollecdataQuery.setRecords(baseMapper.list(poincollecdataQuery));
	  return poincollecdataQuery;
	}
	public List<Poincollecdata> listSize(int size){
		  return baseMapper.listSize(size);
		}
		
	
	
}
