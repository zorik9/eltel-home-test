package com.eltel.home_test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {

	private List<JButton> buttons;
	private List<JCheckBox> checkBoxes;
	private GridBagConstraints constraints;
	private List<String> entityKeyList;
	private List<String> buttonsText;
	private List<Entity> entities;
	private Config config;
	private ActionListeners actionListeners;
	private Map<String, Boolean> entitiesMap;
	
	private int numOfEntities;
	private int numOfButtons = 2;
	private int yPos = 0;
	
	public ControlPanel (List<Entity> entities, ActionListeners actionListeners) {
		this.entities = entities;
		this.actionListeners = actionListeners;
		actionListeners.setComponent(this);
		init();
	}
	
    public void init() {
    	setBorder(BorderFactory.createLineBorder(Color.black));
    	
    	config = new Config();
    	entityKeyList = new ArrayList<>();
    	fillEntitiesDetails();
    	
    	buttonsText = Arrays.asList("start", "stop");
    	checkBoxes = new ArrayList<JCheckBox>(entityKeyList.size());
    	buttons = new ArrayList<JButton>(numOfButtons);
    	constraints = new GridBagConstraints();
    }
    
    private void fillEntitiesDetails() {
    	for (Entity entity : entities) {
    		entityKeyList.add(entity.getKey());
		}
    	numOfEntities = entityKeyList.size();
    }
    
    private void setConstraints() {
    	constraints.fill = GridBagConstraints.HORIZONTAL;
    	constraints.anchor = GridBagConstraints.PAGE_START;
    	constraints.gridwidth = GridBagConstraints.REMAINDER;
    	constraints.weightx = 0.5;
    	constraints.gridx = 0;
    }
    
    private void addEntities() {
    	JCheckBox checkBox;
    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	setBackground(config.getWhiteColor());
		String entityKey;
		Boolean isSelectedKey;
		boolean isSelected = true;
		
		entitiesMap = actionListeners.getEntitiesActionListener().getEntitiesMap();
		
		for (int i = 0; i < numOfEntities; i++) {
			entityKey = entityKeyList.get(i);
			checkBoxes.add(new JCheckBox(entityKey));
			checkBox = checkBoxes.get(i);
			checkBox.setName(entityKey);
			isSelectedKey = entitiesMap.get(entityKey);
			isSelected = isSelectedKey != null ? isSelectedKey : true;
			checkBox.setSelected(isSelected);
			checkBox.addActionListener(actionListeners.getEntitiesActionListener());
			constraints.gridy = yPos++;
			add(checkBox, constraints);
		}
    }
    
    private void addMarginHeight() {
    	int height = config.getMainHeight() -config.getDefaultMargin() * 2 -numOfEntities * config.getCheckboxDefaultHeight() - numOfButtons * config.getButtonDefaultHeight();
    	add(Box.createRigidArea(new Dimension(0,height)));
    }
    
    private void addButtons() {
    	String buttonText;
    	JButton button;
		for (int i = 0; i < numOfButtons; i++) {
			buttonText = buttonsText.get(i);
			buttons.add(new JButton(buttonText));
			button = buttons.get(i);
			button.setName(String.valueOf(i));
			button.addActionListener(actionListeners.getCommandsActionListener());
	    	constraints.gridy = yPos++;
			add(button, constraints);
			
			if(i < numOfButtons) { // Create margin between buttons.
				add(Box.createRigidArea(new Dimension(0, config.getDefaultMargin())));
			}
		}
    }
    
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
    	setConstraints();
    	addEntities();
    	addMarginHeight();
    	setConstraints();
    	addButtons();
		repaint();
	}
}
