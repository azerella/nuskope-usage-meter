package com.adamzerella.nuskopeusagemeter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class to represent a configuration file
 * @author Adam A. Zerella (adam.zerella@gmail.com)
 * @version 1.0.0
 */
public class PropertiesFile extends Properties{
	UsageMeterFrame frame			=	null;
	Properties prop					=	null;
	String propertiesFile			=	"";

	/**
	 * Implicit default constructor
	 */
	PropertiesFile(){}

	PropertiesFile(UsageMeterFrame frame) {
		this.frame 	= 	frame;

		propertiesFile = System.getProperty("user.dir") + "\\config.properties";

		//Check if the file exists in the current dir, load or create.
		try {
			System.out.println("Properties file exists! " + "\t" + "Loading File...");
			this.load(new FileInputStream(propertiesFile));
		} catch	(Exception ex){
			System.out.println("Properties file is missing!" + "\t" + "Creating new file..");
			try {
				FileOutputStream output = new FileOutputStream("config.properties");
				try {
					output.close();
				} catch (IOException e) {
					System.out.println("Unable to close I/O Output stream...");
				}
			} catch (FileNotFoundException e) {
				System.out.println("Unable to write to properties file! No permissions? Disk space?");
			}
		} 
	}

	/**
	 * Method to update the "xcord" field in properties file
	 */
	public void updateX() {
		this.setProperty("xcord", Integer.toString(frame.getLocation().x));

		try {
			this.store(new FileOutputStream("config.properties"), null);
		} catch (Exception e) {
			System.out.println("Cannot write to config file. Deleted file? Permissions?");
		}
	}

	/**
	 * Method to update the "ycord" field in properties file
	 */
	public void updateY(){
		this.setProperty("ycord", Integer.toString(frame.getLocation().y));

		try {
			this.store(new FileOutputStream("config.properties"), null);
		} catch (Exception e) {
			System.out.println("Cannot write to config file. Deleted file? Permissions?");
		}
	}

	/**
	 * Method to update the "token" field in properties file
	 */
	public void updateToken(String token){
		if (token.isEmpty()) throw new NullPointerException("Token is set to null!");

		this.setProperty("token", token);

		try {
			this.store(new FileOutputStream("config.properties"), null);
		} catch (Exception e) {
			System.out.println("Cannot write to config file. Deleted file? Permissions?");
		}
	}
}
