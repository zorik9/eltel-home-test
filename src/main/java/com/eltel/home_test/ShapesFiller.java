package com.eltel.home_test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import lombok.Data;

@Data
public class ShapesFiller {
	
	private BufferedImage bufferedImage;
	private Color color;
	
	public ShapesFiller(BufferedImage bufferedImage, Color color) {
		this.bufferedImage = bufferedImage;
		this.color = color;
	}
	
	public ShapesFiller() {}
	
	public void fillShape(String shape) {
		switch(shape) {
		case "triangle":
			fillTriangle();
			break;
		case "circle":
			fillCircle();
			break;
		case "square":
			fillSquare();
			break;
		}
	}
	
	private void fillSquare() {
		int rgb = color.getRGB();
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
            	bufferedImage.setRGB(i, j, rgb);
            }
        }
	}
	
	private void fillTriangle() {
		int rgb = color.getRGB();
        for (int i = 0; i < bufferedImage.getHeight()-5; i++) {
        	for (int j = bufferedImage.getWidth() /2 - i/3; j < bufferedImage.getWidth() /2 + i/3 + 2; j++) {
            	bufferedImage.setRGB(j, i, rgb);
            }
        }
	}
	
	private void fillCircle() {
		int rgb = color.getRGB();
		double r = bufferedImage.getWidth() / 2;
		double xc = bufferedImage.getMinX() + r;
		double yc = bufferedImage.getMinY() + r;
		double d;
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
            	d = Math.sqrt(Math.pow(i-xc, 2) + Math.pow(j-yc, 2));
            	if(d <= r) {
            		bufferedImage.setRGB(i, j, rgb);
            	}
            }
        }
	}
}
