package com.eltel.home_test;

import java.io.IOException;
import java.net.URI;
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
    
    @SuppressWarnings("finally")
	private String getJsonEntitiesArray() {
    	String fileName = config.getEntitiesFile();
    	URI uri = null;
    	Path resourcePath = null;
    	String jsonEntitiesArray = null;
    	String uriPath = null;
    	String fileFullPath = null;
    	boolean isError = false;
    	try {
    		/** Support FileSystem creation on runtime,
    		 * in order to avoid FileSystemNotFoundException when running executable jar
    		 * 
    		 * */
    		
    		fileFullPath = getClass().getClassLoader().getResource(fileName).getPath();
    		uriPath = fileFullPath.startsWith("file:") ? fileFullPath.replace("file:", "jar:file:") : "file:" + fileFullPath;
    		uri = URI.create(uriPath);
    		Map<String, String> env = new HashMap<>(); 
    		env.put("create", "true");
    		
    		if(uriPath.startsWith("jar:")) {
	    		System.out.println("Creating new file system environment");
	    		FileSystems.newFileSystem(uri, env);
	    		System.out.println("File system environment has been successfuly created");
    		}
    		
    		resourcePath = Paths.get(uri);
    		jsonEntitiesArray = new String(Files.readAllBytes(resourcePath));
		} catch (IOException e) {
			isError = true;
			throw new RuntimeException("Failed to read json entities array from " + fileName + ", due to IO issues", e);
		} catch (Exception e) {
			isError = true;
			throw new RuntimeException("Failed to read json entities array from " + fileName, e);
		} finally {
			if(isError && config.isDebug()) {
				System.out.println("file full path= " + fileFullPath);
				System.out.println("uri = " + uri);
				System.out.println("uri schema= " + uri.getScheme());
				System.out.println("resourcePath = " + resourcePath);
				System.out.println("jsonEntitiesArray = " + jsonEntitiesArray);
			}
			return jsonEntitiesArray;
		}
    }
}
