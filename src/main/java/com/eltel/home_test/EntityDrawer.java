package com.eltel.home_test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import lombok.Data;

@Data
public class EntityDrawer {

	final static Logger logger = Logger.getLogger(EntityDrawer.class);
	
	private Entity entity;
	private Component component;
	private Config config;
	private ColorUtils colorUtils;
	private ShapesFiller shapesFiller;
	
	private int previousDirection;
	private int currentDirection;
	private int coefficient;
	private int deltaRnd = 1;
	private BorderLimits borderLimits;

    public EntityDrawer(Entity entity, Component component, BorderLimits borderLimits) {
		this.entity = entity;
		this.component = component;
		this.borderLimits = borderLimits;
		init();
	}
	
    public EntityDrawer(Entity entity, Component component) {
		this.entity = entity;
		this.component = component;
		init();
	}
    
    public EntityDrawer(Component component) {
		this.component = component;
		init();
	}

	public EntityDrawer() {
		init();
	}
	
	private void init() {
		config = new Config();
		colorUtils = new ColorUtils();
		shapesFiller = new ShapesFiller();
		previousDirection = 3;
		currentDirection = 3;
	}

	public void draw(Graphics g) {
		int x = entity.getX();
		int y = entity.getY();
		
		String iconFileName = "/icons/" + entity.getShape() + "-" + entity.getSize() + ".png";
		URL url = getClass().getResource(iconFileName);
		BufferedImage bufferedImage = getBufferedImage(url);
		Color color = colorUtils.getColorByName(entity.getColor());
		
		shapesFiller.setBufferedImage(bufferedImage);
		shapesFiller.setColor(color);
		shapesFiller.fillShape(entity.getShape());
		
		g.drawImage(bufferedImage, x, y, null);
		drawImageCaption(bufferedImage, g, x, y);
    }
	
	private BufferedImage getBufferedImage(URL url) {
		try {
			return ImageIO.read(url);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read image from url" + url.toString(), e);
		}
	}
	
	private void drawImageCaption(BufferedImage bufferedImage, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();
		g.setFont(new Font("default", Font.BOLD, 16));
		g2d.setColor(Color.BLACK);
		g.drawString(entity.getKey(), x - bufferedImage.getWidth() / 2, y - bufferedImage.getHeight() / 4);
	}
	
	public void move(int direction) {
		if(Math.abs(direction - previousDirection) == 2) {
			logger.debug(String.format("Direction is: %s, previous direction is: %s", direction, previousDirection));
		}
		
		coefficient = direction - direction % 2 - 1;
		/**
		 * Required mapping for coefficient
		 * 0 -> -1
		 * 1 -> -1
		 * 2 -> 1
		 * 3 -> 1
		 * 
		 */
		
		int nextX;
		int nextY;
		double delta = coefficient * config.getEntityStep();
		if(direction % 2 == 0) { // Left or Right
			nextY = entity.getY();
			nextX = entity.getX() + (int)(delta * borderLimits.getRatioX());
		} else { //Up or Down 
			nextX = entity.getX();
			nextY = entity.getY() + (int)(delta * borderLimits.getRatioY());
		}
		
		if(!isValidNewDirection(nextX, nextY)) { // Encountered Wall or corner.
			direction = handleWallsAndCorners(nextX, nextY, direction);
		}
		
		updateDirection(direction);
		logger.debug(String.format("Entity %s, is Moving to: [%d, %d]", entity.getKey(), nextX, nextY));
		
		if(direction % 2 == 0) { // Left or Right
			entity.setX(nextX);
		} else { //Up or Down 
			entity.setY(nextY);
		}
	}
	
	private boolean isValidNewDirection(int x, int y) {
		return x >= borderLimits.getBorderX1() && x <= borderLimits.getBorderX2() &&
				y >= borderLimits.getBorderY1() && y <= borderLimits.getBorderY2();
	}
	
	private int handleWallsAndCorners(int x, int y, int direction) {
		int cornerNewDirection = getCornerNewDirection(x, y, direction);
		
		if(cornerNewDirection != -10) {
			return cornerNewDirection;
		} else { // This is a wall
			return getWallNewDirection(x, y, direction);
		}
	}
	
	private int getWallNewDirection(int x, int y, int direction) {
		Long seed = 25L;
		Random random = new Random(seed);
		int delta = random.nextInt(1) -1; // -1 or 1 coin flip
		return (direction + delta + 4 ) % 4;
	}
	
	private int getCornerNewDirection(int x, int y, int direction) {
		int newDirection;
		
		if(isTopLeftCorner(x, y)) {
			if(direction == 0) {
				newDirection = 3; 
			} else { // direction == 1
				newDirection = 2;
			}
			return newDirection;
		}
		
		if(isTopRightCorner(x, y)) {
			if(direction == 2) {
				newDirection = 3; 
			} else { // direction == 1
				newDirection = 0;
			}
			return newDirection;
		}
		
		if(isBottomLeftCorner(x, y)) {
			if(direction == 3) {
				newDirection = 2; 
			} else { // direction == 0
				newDirection = 1;
			}
			return newDirection;
		}
		
		if(isBottomRightCorner(x, y)) {
			if(direction == 2) {
				newDirection = 1; 
			} else { // direction == 3
				newDirection = 0;
			}
			return newDirection;
		}
		
		return -10; // Not a corner
	}
	
	private boolean isTopLeftCorner(int x, int y) {
		return x < borderLimits.getBorderX1() && y < borderLimits.getBorderY1();
	}
	
	private boolean isBottomLeftCorner(int x, int y) {
		return x < borderLimits.getBorderX1() && y > borderLimits.getBorderY2();
	}
	
	private boolean isTopRightCorner(int x, int y) {
		return x > borderLimits.getBorderX2() && y < borderLimits.getBorderY1();
	}
	
	private boolean isBottomRightCorner(int x, int y) {
		return x > borderLimits.getBorderX2() && y > borderLimits.getBorderY2();
	}
	
	public void moveRandomlly() {
		int rnd = (int)Math.round(Math.random());
		int delta = 2 - rnd * 2 - 1;
		int newDirection = (currentDirection + delta + 4) % 4;
		move(newDirection);
	}
	
	private void updateDirection(int direction) {
		previousDirection = currentDirection;
		currentDirection = direction;
	}
}
