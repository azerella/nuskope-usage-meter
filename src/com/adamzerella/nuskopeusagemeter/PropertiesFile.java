package com.adamzerella.nuskopeusagemeter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Class to represent a configuration file
 * @author Adam A. Zerella (adam.zerella@gmail.com)
 * @version 1.0.0
 */
public class PropertiesFile extends Properties{
	UsageMeterFrame frame			=	null;
	String filename					=	"";
	
	/**
	 * Implicit default constructor
	 */
	public PropertiesFile(){}

	/**
	 * File to store user configuration settings of application.
	 * @param frame - The instantiated JFrame
	 */
	public PropertiesFile(UsageMeterFrame frame) {
		this.frame 	= 	frame;
		this.filename = System.getProperty("user.dir") + "\\config.properties";
		
		//Check if the file exists in the current dir, load or create.
		try {
			System.out.println("Properties file exists! " + "Loading File...");
			this.load(new FileInputStream(filename));
		} catch	(Exception ex){
			System.err.println("Properties file is missing!" + "Creating new file..");
			try {
				FileOutputStream output = new FileOutputStream("config.properties");
				output.close();
			} catch (Exception oex) {
				System.err.println(oex.getMessage());
			}
		}
	}

	/**
	 * Method to update the "xcord" field in properties file
	 */
	public void writeXPosition() {
		if (this.frame.equals(null)) {
			this.setProperty("x_pos", Integer.toString(0));
		}else {	
			this.setProperty("x_pos", Integer.toString(frame.getLocation().x));
		}
		
		try {
			this.store(new FileOutputStream("config.properties"), null);
		} catch (Exception e) {
			System.err.println("Cannot write to config file. Deleted file? Permissions?");
		}
	}

	/**
	 * Method to update the "ycord" field in properties file
	 */
	public void writeYPosition(){
		this.setProperty("y_pos", Integer.toString(frame.getLocation().y));

		try {
			this.store(new FileOutputStream("config.properties"), null);
		} catch (Exception e) {
			System.err.println("Cannot write to config file. Deleted file? Permissions?");
		}
	}

	/**
	 * Method to update the "token" field in properties file
	 */
	public void writeToken(String token){
		if (token.isEmpty()) throw new NullPointerException("Token is set to null!");

		this.setProperty("token", token);

		try {
			this.store(new FileOutputStream("config.properties"), null);
		} catch (Exception e) {
			System.err.println("Cannot write to config file. Deleted file? Permissions?");
		}
	}
	
}