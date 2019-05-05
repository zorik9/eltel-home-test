package com.eltel.home_test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entity {
	private String entity_ID;
	private String name;
	private String color;
	private String shape;
	private String size;
	private Integer x;
	private Integer y;
	
	public String getKey() {
		return entity_ID + "_" + name;
	}
}
