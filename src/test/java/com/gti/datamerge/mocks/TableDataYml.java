/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.mocks;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Xachman
 */
public class TableDataYml {
	
	private String name;
	private List<Map<String,Object>> data;

	public TableDataYml() {
		
	}
	public TableDataYml(String name, List<Map<String, Object>> data) {
		this.name = name;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
}
