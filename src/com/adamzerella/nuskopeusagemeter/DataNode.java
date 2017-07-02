package com.adamzerella.nuskopeusagemeter;

import java.net.URL;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 * Class to represent a node of 'usage' data
 * @author Adam A. Zerella (adam.zerella@gmail.com)
 * @version 1.0.0
 */
public class DataNode {
	String URLStream			= 	"";
	String token 				= 	"";
	String webPage 				= 	"";
	String planName 			= 	"";

	int downSpeed 				= 	0;
	int upSpeed 				= 	0;
	int planQuotaGB 			= 	0;
	int lastResetDateYear 		= 	0;
	int lastResetDateMonth 		= 	0;
	int lastResetDateDay 		=	0;

	float downloadsToDate 		= 	0;
	float uploadsToDate 		= 	0;

	Calendar lastResetDate		= 	null;
	URL nuskopeURL				=	null;

	/**
	 * Implicit default constructor
	 */
	DataNode() {}

	/**
	 * Constructor to receive a token string, try to read it
	 * @param token - the token to use
	 */
	DataNode(String token) {
		if (token.isEmpty()) throw new NullPointerException("Token is empty?");

		this.webPage = "http://api.nuskope.com.au/usage/?Token=";
		this.lastResetDate = new GregorianCalendar();
		this.token = token;	
		this.webPage = this.webPage + this.token; //append token to end of URL

		try {
			this.nuskopeURL = new URL(webPage);
			Scanner input = new Scanner(nuskopeURL.openStream());

			while (input.hasNextLine()){
				URLStream += input.nextLine();
			}
			
			instansiateData(URLStream);
			input.close();
		} catch (Exception ex){
			System.err.println(ex.getMessage());
		} 

		//printNode(); // <--- DEBUGGING 
	}

	/**
	 * @return token string
	 */
	public String getToken(){
		return this.token;
	}

	/**
	 * Instantiate relevant variables with pulled token data using REGEX
	 * @param URLStream - The URL stream to sort through
	 */
	public void instansiateData(String URLStream){
		if (token.isEmpty()) throw new NullPointerException("Token is empty?");

		String planName 		= 	"";
		String downSpeed 		=	"";
		String upSpeed 			= 	"";
		String planQuotaGB 		= 	"";
		String downloadsToDate 	= 	"";
		String uploadsToDate 	= 	"";
		//DecimalFormat df 		= 	new DecimalFormat("0.00"); //Used for download/uploadToDate

		planName = URLStream.replaceAll(".*PlanName\"\\:\"", "");
		planName = planName.replaceAll(" \\d.*", "");
		this.planName = planName;

		downSpeed = URLStream.replaceAll("\\D", "");
		int downSpeedInt = Integer.parseInt(downSpeed.substring(0, 2));
		this.downSpeed = downSpeedInt;

		upSpeed = URLStream.replaceAll(".*?\\\\/", "");
		upSpeed = upSpeed.replaceAll(".?[a-z].*", "");
		int upSpeedInt = Integer.parseInt(upSpeed);
		this.upSpeed = upSpeedInt;

		planQuotaGB = URLStream.replaceAll(".*PlanQuotaGB\"\\:", "");
		planQuotaGB = planQuotaGB.replaceAll("\\W.*", "");
		this.planQuotaGB = Integer.parseInt(planQuotaGB);

		downloadsToDate = URLStream.replaceAll("DailyUsage??.*", "");
		downloadsToDate = downloadsToDate.replaceAll(".*DownloadsGB\"\\:\"", "");
		downloadsToDate = downloadsToDate.replaceAll("\"\\,\"", ""); 
		this.downloadsToDate = Float.parseFloat(downloadsToDate); //Round number here!

		uploadsToDate = URLStream.replaceAll("DailyUsage??.*", ""); //Need to match downloads after itself
		uploadsToDate = uploadsToDate.replaceAll(".*UploadsGB\"\\:\"", "");
		uploadsToDate = uploadsToDate.replaceAll("\"\\,\"+", ""); 
		uploadsToDate = uploadsToDate.replaceAll("DownloadsGB.*", ""); 
		this.uploadsToDate = Float.parseFloat(uploadsToDate); //Round number here!

		String lastResetDateYear = URLStream.replaceAll(".*LastReset\"\\:\"", ""); 
		lastResetDateYear = lastResetDateYear.replaceAll("-.*", "");
		this.lastResetDateYear = Integer.parseInt(lastResetDateYear);

		String lastResetDateMonth = URLStream.replaceAll(".*LastReset\"\\:\"\\d{4}\\-", "");
		lastResetDateMonth = lastResetDateMonth.replaceAll("-.*", "");
		this.lastResetDateMonth = Integer.parseInt(lastResetDateMonth);

		String lastResetDateDay = URLStream.replaceAll(".*LastReset\"\\:\"\\d{4}\\-\\d{2}\\-", "");
		lastResetDateDay = lastResetDateDay.replaceAll("\".*", "");
		this.lastResetDateDay = Integer.parseInt(lastResetDateDay);

		lastResetDate.set(
				this.lastResetDateYear, 
				this.lastResetDateMonth, 
				this.lastResetDateDay); //Set properties of the date
	}

	/**
	 * Method to check if the token stream is valid
	 * @return true if valid
	 */
	public boolean isValid(){
		if (this.token.isEmpty()) throw new NullPointerException("Token is empty?");

		if (this.planName.equals('"' + "Invalid Token" + '"')) return false;

		return true;
	}

	/**
	 * Method used for console debugging, prints information
	 */
	public void printNode(){ //Debugging 
		System.out.println("\n" + "=======================================");
		System.out.println("NuSkope User DataNode");
		System.out.println("=======================================" + "\n");
		System.out.println("Plan Details");
		System.out.println("---------------------------------------");
		System.out.println("Plan Type: " + "\t"+"\t"+	planName); 
		System.out.println("Download Speed: " +"\t"+	downSpeed + "  Mb/s");
		System.out.println("Upload Speed: " +"\t"+"\t"+ upSpeed   + "  Mb/s");
		System.out.println("Plan Quota: " +"\t"+"\t"+ 	planQuotaGB + "GB");
		System.out.println("Valid Token?" + "\t"+"\t" + isValid());
		System.out.println("---------------------------------------"+"\n");
		System.out.println("Quota Remaining");
		System.out.println("---------------------------------------");
		System.out.println("Last Reset Date: " 
				+ "\t" + lastResetDate.get(Calendar.DAY_OF_MONTH) 
				+ "-" +	lastResetDate.get(Calendar.MONTH) 
				+ "-" + lastResetDate.get(Calendar.YEAR));	
		System.out.println("Download Quota Used: " +"\t"+ downloadsToDate + "  GB");
		System.out.println("Upload Quota Used: " +"\t"+ uploadsToDate + "  GB");
		System.out.println("=======================================" + "\n");
	}
	
}