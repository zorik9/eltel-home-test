package com.eltel.home_test;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;

import lombok.Data;

@Data
public class EntitiesActionListener implements ActionListener{

	private Component component;
	private Map<String, Boolean> entitiesMap;
	
	public EntitiesActionListener(Component component) {
		this.component = component;
		entitiesMap = new HashMap<>();
	}
	
	public EntitiesActionListener() { 
		entitiesMap = new HashMap<>();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JCheckBox entitiyCheckBox = (JCheckBox)e.getSource();
		String entityKey = (entitiyCheckBox).getName();
		boolean selectValue = entitiyCheckBox.isSelected();
		entitiesMap.put(entityKey, selectValue);
	}
}