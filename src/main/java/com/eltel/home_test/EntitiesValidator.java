package com.eltel.home_test;

import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

public class EntitiesValidator {
	
	private Config config;
	private ColorUtils colorUtils;

	public EntitiesValidator() {
		config = new Config();
		colorUtils = new ColorUtils();
	}

	public void validateEntities(List<Entity> entities) {
		validateNumOfEntities(entities);
		validateEntitiesFields(entities);
	}
	
	 private void validateNumOfEntities(List<Entity> entities) {
	    	if(entities == null || entities.size() == 0) {
	    		throw new RuntimeException("Error: No entities were providede.");
		}
		
		if(entities.size() > config.getMaxNumOfEntities()) {
			throw new RuntimeException(String.format("Error: Number of provided entities has excceded %d entities", config.getMaxNumOfEntities()));
		}
	}
	
	private void validateEntitiesFields(List<Entity> entities) {
		boolean isValid = true;
		boolean isValidMandatoryFields = true;
		boolean isValidCoordinates = true;
		boolean isValidColor = true;
		boolean isValidShape = true;
		boolean isValidSize = true;
		
		StringBuilder errMsg = new StringBuilder("\nSeveral errors have been found in the given entities settings:");
		StringBuilder entitiesWithMissingFields = new StringBuilder("\n\nThe following entities have missing fields:");
		StringBuilder entitiesWithInvalidCoordinates = new StringBuilder("\n\nThe following entities have invalid coordinates:");
		StringBuilder entitiesWithInvalidColor = new StringBuilder("\n\nThe following entities have invalid color:");
		StringBuilder entitiesWithInvalidShape = new StringBuilder("\n\nThe following entities have invalid shape:");
		StringBuilder entitiesWithInvalidSize = new StringBuilder("\n\nThe following entities have invalid size:");
		
		for (Entity entity : entities) {
			
			if(entity == null) {
				throw new RuntimeException("Error: An empty entity has been detected.");
			}
			
			if(isCoordinatesOutOfRange(entity.getX(), entity.getY())) {
				isValidCoordinates = false;
				entitiesWithInvalidCoordinates.append(String.format("\nentity: %s, coordinates: [%d, %d]", entity.getKey(), entity.getX(), entity.getY()));
			}
			
			if(StringUtils.isEmpty(entity.getEntity_ID())) {
				isValidMandatoryFields = false;
				entitiesWithMissingFields.append(String.format("\nentity: %s, is missing the id field.", entity.getKey()));
			}
			
			if(StringUtils.isEmpty(entity.getName())) {
				isValidMandatoryFields = false;
				entitiesWithMissingFields.append(String.format("\nentity: %s, is missing the id field.", entity.getName()));
			}
			
			if(StringUtils.isEmpty(entity.getColor())) {
				isValidMandatoryFields = false;
				entitiesWithMissingFields.append(String.format("\nentity: %s, is missing the color field.", entity.getKey()));
			} else {
	    		if(!colorUtils.isValidColor(entity.getColor())) {
	    			isValidColor = false;
	    			entitiesWithInvalidColor.append(String.format("\nentity: %s, color: %s", entity.getKey(), entity.getColor()));
	    		}
			}
			
			if(StringUtils.isEmpty(entity.getShape())) {
				isValidMandatoryFields = false;
				entitiesWithMissingFields.append(String.format("\nentity: %s, is missing the shape field.", entity.getKey()));
			} else {
	    		if(!EnumUtils.isValidEnum(Shape.class, entity.getShape().toUpperCase())) {
	    			isValidShape = false;
	    			entitiesWithInvalidShape.append(String.format("\nentity: %s, shape: %s", entity.getKey(), entity.getShape()));
	    		}
			}
			
			if(StringUtils.isEmpty(entity.getSize())) {
				isValidMandatoryFields = false;
				entitiesWithMissingFields.append(String.format("\nentity: %s, is missing the size field.", entity.getKey()));
			} else {
	    		if(!EnumUtils.isValidEnum(Size.class, entity.getSize().toUpperCase())) {
	    			isValidSize = false;
	    			entitiesWithInvalidSize.append(String.format("\nentity: %s, size: %s", entity.getKey(), entity.getSize()));
	    		}
			}
		}
		
		if(!isValidMandatoryFields) {
			isValid = false;
			errMsg.append(entitiesWithMissingFields);
		}
		
		if(!isValidCoordinates) {
			isValid = false;
			errMsg.append(entitiesWithInvalidCoordinates);
		}
		
		if(!isValidColor) {
			isValid = false;
			errMsg.append(entitiesWithInvalidColor);
		}
		
		if(!isValidShape) {
			isValid = false;
			errMsg.append(entitiesWithInvalidShape);
		}
		
		if(!isValidSize) {
			isValid = false;
			errMsg.append(entitiesWithInvalidSize);
		}
		
		if(!isValid) {
			throw new RuntimeException(errMsg.toString());
		}
	}
	
	private boolean isCoordinatesOutOfRange(int x, int y) {
		return x < 1 || x > config.getGridSizeX() ||
				y < 1 || y > config.getGridSizeY();
	}
}
