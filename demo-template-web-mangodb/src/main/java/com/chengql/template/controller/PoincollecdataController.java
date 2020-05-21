package com.chengql.template.controller;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chengql.template.domain.Poincollecdata;
import com.chengql.template.service.PoincollecdataService;





/**
 * 
 * 
 * @author chengql
 * @email chengql@hanwei.cn
 * @date 2020-05-20 13:43:04
 */
@Controller
@RequestMapping(value = "/poincollecdata")
public class PoincollecdataController {
    private static final Logger LOG = LoggerFactory.getLogger(PoincollecdataController.class);
	@Autowired
	private PoincollecdataService poincollecdataService;
	
	@RequestMapping(value="/performance/analysis/{dataSize}")
	@ResponseBody
	public String performanceAnalysis(@PathVariable("dataSize") int dataSize){
		long currentTime = System.currentTimeMillis();
		Poincollecdata poincollecdata = null;
		for(int i=0;i<dataSize;i++) {
			poincollecdata = new Poincollecdata();
			poincollecdata.setTags("Tpoint"+i);
			poincollecdata.setStatus("Tnormal"+i);
			poincollecdata.setValue(i+"");
			poincollecdata.setCollectTime(new Date());
			poincollecdataService.savePoincollecdata(poincollecdata);
		}
		long lastTime = System.currentTimeMillis();
		System.out.println("新增"+dataSize+"条耗时"+(lastTime-currentTime)+"毫秒");
		return "新增"+dataSize+"条耗时"+(lastTime-currentTime)+"毫秒";
	}
	
	@RequestMapping(value="/list/{size}")
	@ResponseBody
	public List<Poincollecdata> list(@PathVariable("size")int size) {
		long currentTime = System.currentTimeMillis();
		List<Poincollecdata> list = poincollecdataService.listSize(size);
		long lastTime = System.currentTimeMillis();
		System.out.println("读取"+size+"条耗时"+(lastTime-currentTime)+"毫秒");
		return list;
	}
}
