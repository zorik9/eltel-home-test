package com.eltel.home_test;

import java.awt.Color;

import lombok.Getter;

@Getter
public class Config {
	private boolean resizableWindow = true;
	private int windowLocationX = 100;
	private int windowLocationY = 100;
	private int windowWidthMargin = 295;
	private int windowHeightMargin = 75;
	private int boardWidth = 500;
	private int boardHeight = 575;
	private int mainWidth = 550;
	private int mainHeight = 575;
	private int windowWidth = 995;
	private int windowHeight = 630;
	private int checkboxDefaultHeight = 27;
	private int buttonDefaultHeight = 25;
	private int defaultMargin = 15;
	private int boardTopMargin = 5;
	private Color whiteColor = new Color(255, 255, 255);
	private double entityStep = 2.5;
	private int timerInterval = 5000; // 5 seconds intervals
	private int gridSizeX = 100;
	private int gridSizeY = 100;
	private int maxNumOfEntities = 10;
	private int gridGapX = 10;
	private int gridGapY = 25;
	private String entitiesFile = "entityData.json";
}
