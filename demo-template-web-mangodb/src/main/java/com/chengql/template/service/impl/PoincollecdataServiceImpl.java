package com.chengql.template.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.chengql.template.domain.Poincollecdata;
import com.chengql.template.service.PoincollecdataService;

@Service
public class PoincollecdataServiceImpl implements PoincollecdataService {
	@Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void savePoincollecdata(Poincollecdata poincollecdata) {
		  mongoTemplate.save(poincollecdata);
	}

	@Override
	public List<Poincollecdata> listSize(int size) {
		Query query = new Query();
		query.limit(size);
		return mongoTemplate.find(query, Poincollecdata.class);
	}
 
}
