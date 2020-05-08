package com.covid19.stat.entity;

import lombok.Data;

@Data
public class DeltaEntity {
	private String deltaName;
	private int confirmed;
	private int deceased;
	private int recovered;
}
