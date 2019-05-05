package com.eltel.home_test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel implements KeyListener{

	private Config config;
	private Timer timer;
	private Map<String, Boolean> entitiesMap;
	private List<Entity> entities;
	private EntitiesDrawer entitiesDrawer;
	private ActionListeners actionListeners;
	private String command;
	private boolean isCoordinatesTranslated;
	private BorderLimits borderLimits;
	
	public BoardPanel(List<Entity> entities, ActionListeners actionListeners) {
		this.entities = entities;
		this.actionListeners = actionListeners;
		init();
	}
	
	private void init() {
		requestFocus();
		addKeyListener(this);
		initEntitiesMap();
		borderLimits = new BorderLimits();
		entitiesDrawer = new EntitiesDrawer(entities, this);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setFocusable(true);
		config = new Config();
		timer = new Timer(config.getTimerInterval(), new TimerListener());
		timer.start();
	}

	 
	private void translateEntitiesCoordinates() {
		int gapX = config.getGridGapX();
		int gapY = config.getGridGapY();
		int gridX = config.getGridSizeX();
		int gridY = config.getGridSizeY();
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int ratioX = panelWidth / gridX;
		int ratioY = panelHeight / gridY;
		borderLimits.setRatioX(ratioX);
		borderLimits.setRatioY(ratioY);
		int borderGapWidth = panelWidth - ratioX * gridX;
		int borderGapHeight= panelHeight - ratioY * gridY;
		borderLimits.setBorderX1(config.getGridGapX() + (int)(config.getEntityStep() * 2));
		borderLimits.setBorderY1(config.getGridGapY() + (int)(config.getEntityStep() * 4) + config.getBoardTopMargin());
		borderLimits.setBorderX2(panelWidth - borderGapWidth / 2 - gapX);
		borderLimits.setBorderY2(panelHeight - borderGapHeight + gapY + (int)(config.getEntityStep() * 2));
		
		for (Entity entity : entities) {
			entity.setX(entity.getX() * ratioX + gapX);
			entity.setY(entity.getY() * ratioY + gapY);
		}
		
		isCoordinatesTranslated = true;
	}
	 
	private void initEntitiesMap() {
		 entitiesMap = new HashMap<>();
		 for (Entity entity : entities) {
			 entitiesMap.put(entity.getKey(), true);
		 }
	}
	 
	public void paintComponent(Graphics g) {
		if(!isCoordinatesTranslated) {
			translateEntitiesCoordinates();
		}
		
		updateCommand();
		updateEntitiesMap();
		
		super.paintComponent(g);
		paintEntities(g);
		repaint();
	}
	
	private void paintEntities(Graphics g) {
		entitiesDrawer.setBorderLimits(borderLimits);
		entitiesDrawer.draw(g);
	}
	
    public class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	if(command != null && command.equals("start")) {
        		entitiesDrawer.setBorderLimits(borderLimits);
        		entitiesDrawer.moveRandomlly();
        	}
        }
    }
    
    private void updateEntitiesMap() {
    	Map<String, Boolean> entitiesActionListenerMap = actionListeners.getEntitiesActionListener().getEntitiesMap();
    	if(entitiesActionListenerMap != null && entitiesActionListenerMap.size() > 0) {
    		entitiesMap = entitiesActionListenerMap; 
    	}
    	entitiesDrawer.setEntitiesMap(entitiesMap);
    }
    
    private void updateCommand() {
    	command = actionListeners.getCommandsActionListener().getCommand();
    	
    	if(command == null) {
    		return;
    	}
    	
    	switch(command) {
    	case "start":
    		if(!timer.isRunning()) {
    			timer.start();
    		}
    		break;
    	case "stop":
    		if(timer.isRunning()) {
    			timer.stop();
    		}
    		break;
    	default:
    		break;
    	}
    }

	@Override
	public void keyTyped(KeyEvent e) {
		 handleKeyEvent(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		handleKeyEvent(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		handleKeyEvent(e);
	}
	
    protected void handleKeyEvent(KeyEvent e) {
        int id = e.getID();
        if (id != KeyEvent.KEY_TYPED) {
          int keyCode = e.getKeyCode();
          if(keyCode >= 37 && keyCode <= 40) {
        	  entitiesDrawer.setBorderLimits(borderLimits);
        	  entitiesDrawer.move(keyCode - 37);
          }
        }
    }
}
