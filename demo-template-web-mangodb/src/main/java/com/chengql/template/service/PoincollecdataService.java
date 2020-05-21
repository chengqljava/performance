package com.chengql.template.service;

import java.util.List;

import com.chengql.template.domain.Poincollecdata;

public interface PoincollecdataService {
	public void savePoincollecdata(Poincollecdata poincollecdata);
	
	public List<Poincollecdata> listSize(int size);
}
