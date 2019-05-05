package com.eltel.home_test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EntitiesFetcher {
	
	private EntitiesValidator entitiesValidator;
	private Config config;
	
	public EntitiesFetcher() {
		entitiesValidator = new EntitiesValidator();
		config = new Config();
	}
	
	public List<Entity> getEntities() {
		List<Entity> entities = fetchEntities();
		entitiesValidator.validateEntities(entities);
		return entities;
	}
	
    private List<Entity> fetchEntities(){
    	String jsonEntitiesArray = getJsonEntitiesArray();
    	ObjectMapper objectMapper = new ObjectMapper();
    	try {
			return objectMapper.readValue(jsonEntitiesArray, new TypeReference<List<Entity>>(){});
		} catch (IOException e) {
			throw new RuntimeException("Failed to convert entities from input json file.", e);
		}
    }
    
    private String getJsonEntitiesArray() {
    	String fileName = config.getEntitiesFile(); 
    	try {
			return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(fileName).toURI())));
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException("Failed to read json entities array from " + fileName, e);
		}
    }
}
