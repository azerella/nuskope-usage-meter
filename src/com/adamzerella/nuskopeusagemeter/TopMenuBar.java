package com.adamzerella.nuskopeusagemeter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Class to represent the top menu bar
 * @author Adam A. Zerella (adam.zerella@gmail.com)
 * @version 1.0.0
 */
public class TopMenuBar extends JMenuBar implements ActionListener{
	String userInput 			= 	"";
	JMenuItem setToken 			=	null;

	/**
	 * The constructor for our topMenu object 
	 * @param usageFrame - The frame to attach the menuBar to
	 */
	public TopMenuBar(UsageMeterFrame usageFrame){
		JMenu fileMenu = new JMenu("File");
		fileMenu.addActionListener(this);
		this.add(fileMenu);

		setToken = new JMenuItem("Set Token");
		setToken.addActionListener(this);
		fileMenu.add(setToken);

		JMenuItem exitMenu = new JMenuItem("Exit");
		exitMenu.addActionListener(this);
		fileMenu.add(exitMenu);

		JMenu viewMenu = new JMenu("View");
		viewMenu.addActionListener(this);
		this.add(viewMenu);

		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(this);
		viewMenu.add(aboutItem);

		JMenu settingMenu = new JMenu("Settings");
		settingMenu.addActionListener(this);
		settingMenu.setEnabled(false); //Pending feature completion
		settingMenu.setToolTipText("Coming soon!");
		this.add(settingMenu);

		JMenuItem prefItem = new JMenuItem("Preferences");
		prefItem.addActionListener(this);
		settingMenu.add(prefItem);

		usageFrame.setJMenuBar(this);
	}

	public String getUserInput(){
		return this.userInput;
	}

	public void setUserInput(String var){
		if (var.isEmpty())  throw new NullPointerException("User Input is empty!");

		this.userInput = var;
	}

	/**
	 * Method to return "Set Token" menu item.
	 * @return - The user entered text
	 */
	public JMenuItem getTokenMenuItem(){
		return this.setToken;
	}

	/**
	 * An implemented method to fire whenever actioned objects from the top menu are triggered
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "Exit":
			System.out.println("EXIT PRESSED");
			System.exit(0);
			break;
		case "Set Token":
			System.out.println("SET TOKEN PRESSED");
			this.userInput = JOptionPane.showInputDialog("Enter your provided token");
			this.setToolTipText("Please enter your provided NuSkope API Token.");
			System.out.println("Token: "+"'" + userInput + "'"+ " has been set.");
			break;
		case "About":
			System.out.println("ABOUT PRESSED");
			AboutDialog about = new AboutDialog();
			break;
		case "Preferences":
			System.out.println("SETTINGS PRESSED");
			SettingsDialog settings = new SettingsDialog();
			break;
		}			
	}
	
}