package com.covid19.stat.entity;

import java.util.List;

import lombok.Data;

@Data
public class StateEntity {
	 String statecode;
	 String statename;
	 List<DistrictEntity> district;
	 int confirmed;
	 int active;
	 int deceased;
	 int recoverd;
	 int total; 
}
