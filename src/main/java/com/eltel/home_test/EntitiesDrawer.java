package com.eltel.home_test;

import java.awt.Component;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class EntitiesDrawer {

	private List<Entity> entities;
	private Component component;
	private EntityDrawer entityDrawer;
	private Map<String, Boolean> entitiesMap;
	private BorderLimits borderLimits;
	private int[][] coordinates;
	
    public EntitiesDrawer(List<Entity> entities, Component component) {
		this.entities = entities;
		this.component = component;
		init();		
	}

	public EntitiesDrawer() {
		init();
	}
	
	private void init() {
		entityDrawer = new EntityDrawer();
		borderLimits = new BorderLimits();
		entityDrawer.setBorderLimits(borderLimits);
		coordinates = new int[entities.size()][2];
		entitiesMap = new HashMap<>();
	}
	
	public void draw(Graphics g) {
		Boolean isSelected = true;
		
		for (Entity entity : entities) {
			isSelected = entitiesMap.get(entity.getKey());
			if(isSelected == null || isSelected == true) {
				entityDrawer.setEntity(entity);
				entityDrawer.draw(g);
			}
		}
	}
	
	public void move(int direction) {
		Entity entity;
		Boolean isSelectedKey;
		boolean isSelected = true;
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
			isSelectedKey = entitiesMap.get(entity.getKey());
			isSelected = isSelectedKey != null ? isSelectedKey : true;
			if(isSelected) {
				entityDrawer.setEntity(entity);
				entityDrawer.setBorderLimits(borderLimits);
				entityDrawer.move(direction);
				updateCoordinates(entity, i);
			}
		}	
	}
	
	public void moveRandomlly() {
		Entity entity;
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
			entityDrawer.setEntity(entity);
			entityDrawer.setBorderLimits(borderLimits);
			entityDrawer.moveRandomlly();
			updateCoordinates(entity, i);
		}
	}
	
	private void updateCoordinates(Entity entity, int entityIndex) {
		coordinates[entityIndex][0] = entity.getX();
		coordinates[entityIndex][1] = entity.getY();
	}
}
