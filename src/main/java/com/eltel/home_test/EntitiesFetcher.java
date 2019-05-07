package com.eltel.home_test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    		/** Support FileSystem creation on runtime,
    		 * in order to avoid FileSystemNotFoundException when running executable jar
    		 * 
    		 * */
    		URI uri = getClass().getClassLoader().getResource(fileName).toURI();
    		Map<String, String> env = new HashMap<>(); 
    		env.put("create", "true");
    		//FileSystem zipfs = FileSystems.newFileSystem(uri, env);
    		FileSystems.newFileSystem(uri, env);
    		Path resourcePath = Paths.get(uri);
			return new String(Files.readAllBytes(resourcePath));
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException("Failed to read json entities array from " + fileName, e);
		}
    }
}
