package com.eltel.home_test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import lombok.Data;

@Data
public class CommandsActionListener implements ActionListener{

	private String command;
	
	public CommandsActionListener() { }

	@Override
	public void actionPerformed(ActionEvent e) {
		command = ((JButton)e.getSource()).getText();
	}
}
