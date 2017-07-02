package com.adamzerella.nuskopeusagemeter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

/**
 * Class to create the settings/preferences menu
 * 		--	PENDING IMPLEMENTATION	--
 * @author Adam A. Zerella (adam.zerella@gmail.com)
 * @version 1.0.0
 */
public class SettingsDialog  extends JDialog implements ActionListener{

	/**
	 * Constructor used to instantiate the settings menu dialog
	 */
	public SettingsDialog(){
		this.setSize(300, 350);
		this.setTitle("Settings Menu");
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	/**
	 * Action Listener for all action events in this class
	 * @param e - The ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {}
	
}