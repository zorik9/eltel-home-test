package com.eltel.home_test;

import java.awt.Color;

public class ColorUtils {
	public Color getColorByName(String color) {
	    try {
	        return (Color)Color.class.getField(color.toUpperCase()).get(null);
	    } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
	    	throw new RuntimeException("Failed to convert color " + color + " to color object", e);
	    }
	}
	
	public boolean isValidColor(String color) {
		try {
			getColorByName(color);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}
