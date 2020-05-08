package com.covid19.stat.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.covid19.stat.service.Covid19Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "https://covid19-india-statistics.herokuapp.com/")
@Controller
public class Covid19Controller implements ErrorController{
	@Autowired
	Covid19Service covid19Service;
	
	private static final String PATH="/error";
	
	@RequestMapping(value=PATH)
	public String error() {
		return "index.html";
	}
	
	@Override
	public String getErrorPath() {
		return PATH;
	}

	public void readJson() throws IOException, ParseException {
		String value="https://api.covid19india.org/state_district_wise.json";
		URL url = new URL(value);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
		conn.setRequestMethod("GET");
		conn.connect();
		int responsecode = conn.getResponseCode();
		if(responsecode != 200) {
			throw new RuntimeException("HttpResponseCode: " +responsecode);
		}else{
			String inline="";
			Scanner sc = new Scanner(url.openStream());
			while(sc.hasNext()){
				inline+=sc.nextLine();
			}
			sc.close();
			JSONParser parse = new JSONParser();
			JSONObject jobj = (JSONObject)parse.parse(inline);
			Set<String> stateList= jobj.keySet();
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> stateInfo = new HashMap<String, Object>();
			stateInfo = mapper.readValue(inline, new TypeReference<HashMap>() {});
			covid19Service.addData(stateList,stateInfo);

		}
		
	}
	
	@ResponseBody
	@RequestMapping("/covid19india/states")
	public Map<String,Object> getAllStates() throws IOException, ParseException {
		readJson();
		Map<String,Object> stateValue=covid19Service.getStateData();
		return stateValue;
	}
	
	@ResponseBody
	@RequestMapping("/covid19india/districts")
	public Map<String,Object> getAllDistrict(@RequestParam String statecode) throws IOException, ParseException {
		Map<String,Object> districtValue=covid19Service.getDistrictData(statecode);
		return districtValue;
	}
	
	@ResponseBody
	@RequestMapping("/covid19india/statesAlone")
	public Map<String,Object> getAllStatesAlone() throws IOException, ParseException {
		Map<String,Object> stateValue=covid19Service.getStateData();
		return stateValue;
	}

}
