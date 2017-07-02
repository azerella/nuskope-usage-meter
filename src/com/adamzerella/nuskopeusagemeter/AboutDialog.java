package com.adamzerella.nuskopeusagemeter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A class to create the about menu dialog
 * @author Adam A. Zerella (adam.zerella@gmail.com)
 * @version 1.0.0
 */
public class AboutDialog extends JDialog implements ActionListener{
	JLabel lblAbout				=	null;
	JButton btnExit				=	null;

	/**
	 * 
	 */
	public AboutDialog(){
		this.setSize(300, 300);
		this.setTitle("About");
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);

		this.lblAbout = new JLabel();
		this.lblAbout.setBorder(new EmptyBorder(10,10,10,10));
		this.lblAbout.setText("<html>"
				+ "<b>NuSkope Usage Meter</b><br>"
				+ "Version: &emsp 1.0.0 <br>"
				+ "Created: &emsp Adam A. Zerella 2016 <br>"
				+ "Contact: &emsp adam.zerella@gmail.com <br><br>-------------"
				+ "--------------------<br>"
				+ "THIS SOFTWARE WAS CREATED UNDER THE GNU 3 GENERAL PUBLIC "
				+ "LICENSE. <br>"
				+ "<br>"
				+ "'NuSkope Pty. Ltd' ARE NOT AFFILIATED WITH THIS PROGRAM WHAT"
				+ " SO EVER. <br><br>"
				+ "THE NUSKOPE ICON DISPLAYED IS USED WITH THE PERMISSION OF NU"
				+ "SKOPE"
				+ "</html>");

		this.setVisible(true);
		this.add(lblAbout, BorderLayout.CENTER);

		JPanel btnContainer = new JPanel();

		btnExit = new JButton("Exit");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("About Exit Button");

		btnContainer.add(btnExit);
		this.add(btnContainer, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("About Exit Button")){
			this.dispose();
		}
	}
	
}