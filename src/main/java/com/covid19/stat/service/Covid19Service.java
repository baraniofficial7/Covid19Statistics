package com.covid19.stat.service;


import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public interface Covid19Service {

	void addData(Set<String> stateList, Map<String, Object> stateInfo);

	Map<String,Object> getDistrictData(String code);

	Map<String,Object> getStateData();


}
