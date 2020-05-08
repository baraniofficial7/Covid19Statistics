package com.covid19.stat.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.covid19.stat.entity.DeltaEntity;
import com.covid19.stat.entity.DistrictEntity;
import com.covid19.stat.entity.StateEntity;

import org.springframework.stereotype.Component;

@Component
public class Covid19ServiceImpl implements Covid19Service{

	List<StateEntity> stateEntityList=new ArrayList<StateEntity>();

	@Override
	public void addData(Set<String> stateSet, Map<String, Object> stateInfo) {
		List<String> stateList= new ArrayList<String>(stateSet);
		stateEntityList=stateList.stream().map(action->{
			StateEntity stateEntity= new StateEntity();
			Map<String,Object> stateInformation=(Map<String, Object>) stateInfo.get(action);
			String stateCode=stateInformation.get("statecode").toString();
			stateEntity.setStatecode(stateCode);
			stateEntity.setStatename(action);
			Map<String,Object> districtInformation=(Map<String, Object>) stateInformation.get("districtData");
			Set<String> districtName=districtInformation.keySet();
			List<String> districtList= new ArrayList<String>(districtName);
			List<DistrictEntity> districtEntityList=districtList.stream().map(mapper->{
				DistrictEntity districtEntity= new DistrictEntity();
				districtEntity.setDistrictName(mapper);
				Map<String,Object> particularMap=(Map<String, Object>) districtInformation.get(mapper);
				districtEntity.setNotes(String.valueOf(particularMap.get("notes")));
				districtEntity.setConfirmed((int) particularMap.get("confirmed"));
				districtEntity.setDeceased((int) particularMap.get("deceased"));
				districtEntity.setRecovered((int) particularMap.get("recovered"));
				int active=((int) particularMap.get("confirmed")-((int) particularMap.get("deceased")+(int) particularMap.get("recovered")));
				if(active<0 || mapper.equals("Unknown")) {
					active=(int) particularMap.get("active");
				}
				districtEntity.setActive(active);
				Map<String,Object> deltaMap=(Map<String, Object>) particularMap.get("delta");
				DeltaEntity deltaEntity= new DeltaEntity();
				deltaEntity.setDeltaName(stateCode+"_"+mapper);
				deltaEntity.setConfirmed((int)deltaMap.get("confirmed"));
				deltaEntity.setDeceased((int)deltaMap.get("deceased"));
				deltaEntity.setRecovered((int)deltaMap.get("recovered"));
				districtEntity.setDeltaentity(deltaEntity);
				return districtEntity;
			}).collect(Collectors.toList());
			stateEntity.setDistrict(districtEntityList);
			return stateEntity;
		}).collect(Collectors.toList());

	}

	@Override
	public Map<String,Object> getDistrictData(String code) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<DistrictEntity> resultEntity=new ArrayList<DistrictEntity>();
		List<StateEntity> tempEntity= new ArrayList<StateEntity>();
		StateEntity particularState=new StateEntity();
		AtomicInteger total = new AtomicInteger(0);
		AtomicInteger activeTotal = new AtomicInteger(0);
		AtomicInteger recoveredTotal = new AtomicInteger(0);
		AtomicInteger deathTotal = new AtomicInteger(0);
		tempEntity= stateEntityList.stream().filter(predicate->predicate.getStatecode().equals(code)).map(mapper->{
			StateEntity temp=mapper;
			return temp;
		}).collect(Collectors.toList());
		particularState=tempEntity.get(0);
		List<DistrictEntity> prepareEntity=particularState.getDistrict();
		resultEntity=prepareEntity.stream().map(mapper->{
			DistrictEntity districtEntity= new DistrictEntity();
			districtEntity.setConfirmed(mapper.getConfirmed());
			total.addAndGet(mapper.getConfirmed());
			districtEntity.setActive(mapper.getConfirmed()-(mapper.getRecovered()+mapper.getDeceased()));
			activeTotal.addAndGet(mapper.getConfirmed()-(mapper.getRecovered()+mapper.getDeceased()));
			districtEntity.setRecovered(mapper.getRecovered());
			recoveredTotal.addAndGet(mapper.getRecovered());
			districtEntity.setDeceased(mapper.getDeceased());
			deathTotal.addAndGet(mapper.getDeceased());
			districtEntity.setDistrictName(mapper.getDistrictName());
			districtEntity.setNotes(mapper.getNotes());
			DeltaEntity delta= mapper.getDeltaentity();
			DeltaEntity deltaEntity= new DeltaEntity();
			deltaEntity.setConfirmed(delta.getConfirmed());
			deltaEntity.setDeceased(delta.getDeceased());
			deltaEntity.setDeltaName(delta.getDeltaName());
			deltaEntity.setRecovered(delta.getRecovered());
			districtEntity.setDeltaentity(deltaEntity);
			return districtEntity;
		}).collect(Collectors.toList());
		resultEntity=prepareEntity;
		Collections.sort(resultEntity,(a,b)->b.getConfirmed()-a.getConfirmed());
		resultMap.put("district", resultEntity);
		resultMap.put("total", total.intValue());
		resultMap.put("activeTotal", activeTotal.intValue());
		resultMap.put("recoveredTotal", recoveredTotal.intValue());
		resultMap.put("deathTotal", deathTotal.intValue());
		return resultMap;

	}


	@Override
	public Map<String,Object> getStateData() {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<StateEntity> stateList=new ArrayList<StateEntity>();
		AtomicInteger total = new AtomicInteger(0);
		AtomicInteger activeTotal = new AtomicInteger(0);
		AtomicInteger recoveredTotal = new AtomicInteger(0);
		AtomicInteger deathTotal = new AtomicInteger(0);
		stateList=stateEntityList.stream().map(mapper->{
			AtomicInteger confirmed = new AtomicInteger(0);
			AtomicInteger active = new AtomicInteger(0);
			AtomicInteger deceased = new AtomicInteger(0);
			AtomicInteger recoverd = new AtomicInteger(0);
			StateEntity stateEntity= new StateEntity();
			List<DistrictEntity> resultEntity=mapper.getDistrict();
			resultEntity.stream().forEach(action->{
				confirmed.addAndGet(action.getConfirmed());
				active.addAndGet(action.getConfirmed()-(action.getDeceased()+action.getRecovered()));
				deceased.addAndGet(action.getDeceased());
				recoverd.addAndGet(action.getRecovered());
			});
			stateEntity.setStatename(mapper.getStatename());
			stateEntity.setStatecode(mapper.getStatecode());
			stateEntity.setConfirmed(confirmed.intValue());
			stateEntity.setActive(active.intValue());
			stateEntity.setRecoverd(recoverd.intValue());
			stateEntity.setDeceased(deceased.intValue());
			total.addAndGet(confirmed.intValue());
			activeTotal.addAndGet(active.intValue());
			recoveredTotal.addAndGet(recoverd.intValue());
			deathTotal.addAndGet(deceased.intValue());
			return stateEntity;
		}).collect(Collectors.toList());
		Collections.sort(stateList,(a,b)->b.getConfirmed()-a.getConfirmed());
		resultMap.put("state", stateList);
		resultMap.put("total", total.intValue());
		resultMap.put("activeTotal", activeTotal.intValue());
		resultMap.put("recoveredTotal", recoveredTotal.intValue());
		resultMap.put("deathTotal", deathTotal.intValue());
		return resultMap;
	}

}
