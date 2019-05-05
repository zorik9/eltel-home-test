package com.eltel.home_test;

import java.awt.Component;

import lombok.Data;

@Data
public class ActionListeners {
	private CommandsActionListener commandsActionListener;
	private EntitiesActionListener entitiesActionListener;
	private Component component;
	
	public ActionListeners() {
		commandsActionListener = new CommandsActionListener();
		entitiesActionListener = new EntitiesActionListener();
	}
	
	public void setComponent(Component component) {
		this.component = component;
		entitiesActionListener.setComponent(component);
	}
}
