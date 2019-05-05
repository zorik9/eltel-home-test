package com.eltel.home_test;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class App extends JFrame{

	private Config config;
	private List<Entity> entities;
	private EntitiesFetcher entitiesFetcher;
	
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    App app = new App();
                    app.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public App() {    	
    	config = new Config();
    	entitiesFetcher = new EntitiesFetcher();
    	entities = entitiesFetcher.getEntities();
    	
    	addMainPnel();
        setWindowProperties();
    }
    
    private void addMainPnel() {
        JPanel mainPanel = new MainPanel(entities);
    	mainPanel.setPreferredSize(new Dimension(config.getMainWidth(), config.getMainHeight()));
    	mainPanel.setBackground(config.getWhiteColor());
        Container container = getContentPane();
        container.add(mainPanel);
    }
    
    private void setWindowProperties() {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(config.isResizableWindow());
        setBounds(config.getWindowLocationX(), config.getWindowLocationY(), config.getWindowWidth(), config.getWindowHeight());
    }
}