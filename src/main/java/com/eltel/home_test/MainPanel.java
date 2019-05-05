package com.eltel.home_test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	private Config config;
	private List<Entity> entities;
	private ActionListeners actionListeners;
	
	public MainPanel (List<Entity> entities) {
		this.entities = entities;
		setBorder(BorderFactory.createLineBorder(Color.black));
		init();
	}
	
    public void init() {
    	config  = new Config();
    	actionListeners = new ActionListeners();
    	
    	JPanel controlPanel = new ControlPanel(entities, actionListeners);
    	JPanel boardPanel = new BoardPanel(entities, actionListeners);
    	controlPanel.setPreferredSize(new Dimension(config.getWindowWidth() - config.getBoardWidth(), config.getBoardHeight()));
    	boardPanel.setPreferredSize(new Dimension(config.getBoardWidth(), config.getBoardHeight()));
    	GridBagLayout gridBagLayout = new GridBagLayout();
    	gridBagLayout.columnWidths = new int[] {config.getMainWidth()/5, config.getMainWidth() /5 * 4};
    	gridBagLayout.rowHeights = new int[] {config.getMainHeight()};
    	setLayout(gridBagLayout);
    	GridBagConstraints constraints = new GridBagConstraints();
    	
    	addPanel(constraints, controlPanel, 0, 0);
    	addPanel(constraints, boardPanel, 1, 0);
    }
    
    private void addPanel(GridBagConstraints constraints, JPanel panel, int gridx, int gridy) {
    	constraints.fill = GridBagConstraints.BOTH;
    	constraints.weightx = 0.5;
    	constraints.gridx = gridx;
    	constraints.gridy = gridy;
    	
    	add(panel, constraints);
    }
}
