package com.adamzerella.nuskopeusagemeter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class that represents the frame object of the application
 * @author Adam A. Zerella (adam.zerella@gmail.com)
 * @version 1.0.0
 */
public class UsageMeterFrame extends JFrame implements ActionListener {
	JLabel lblDays 					= 	null;
	JLabel lblplanNameDYNAMIC		=	null;
	JLabel lblQuotaDYNAMIC			=	null;
	JLabel lblDownSpeedDYNAMIC		=	null;
	JLabel lblUpSpeedDYNAMIC		=	null;
	JLabel lblDownloadQuotaDYNAMIC	=	null;
	JLabel lblUploadQuotaDYNAMIC	=	null;
	JLabel lblResetDateDYNAMIC		=	null;
	JLabel lblUsage 				= 	null;
	JLabel lblDateFooter 			= 	null;
	JButton btnRefresh				= 	null;
	JProgressBar pgbUsageRemain 	= 	null;
	JProgressBar pgbDaysMonth 		= 	null;

	DataNode node					= 	null;
	Calendar currentDate 			= 	null;
	DecimalFormat 	df 				= 	null; 	//Used for download/uploadToDate
	TopMenuBar topMenu				=	null;
	PropertiesFile	propFile		=	null;

	String pwd 						= 	"";
	boolean configFileExist			=	false;
	int resetDayDate				=	0;
	int currentDayInInt 			= 	0;

	/**
	 * Implicit default constructor
	 */
	public UsageMeterFrame(){}

	/**
	 * Constructor to instantiate the frame object used for the application.
	 * @param dim - the dimensions (x,y) of the frame
	 * @throws FileNotFoundException 
	 */
	public UsageMeterFrame(int width, int height) {
		this.currentDate = new GregorianCalendar();
		this.propFile = new PropertiesFile(this);
		this.currentDayInInt = currentDate.get(Calendar.DAY_OF_MONTH);

		//Shutdown thread that fires on shutdown, used for storing prop data
		Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Hook"){ 
			public void run(){
				System.out.println("\n" + "[SHUTDOWN HOOK]");	
				propFile.writeXPosition();
				propFile.writeYPosition();

				if (!topMenu.getUserInput().isEmpty()){
					propFile.writeToken(topMenu.getUserInput());
				} else{
					propFile.writeToken("null"); //Filler text, required; related to bug 
				}
				System.out.println(""
						+ "x_pos: "  + "\t" + propFile.getProperty("x_pos") + "\n"  
						+ "y_pos: "  + "\t" + propFile.getProperty("y_pos") + "\n"
						+ "token: "  + "\t" + propFile.getProperty("token") + "\n"
						);
			}
		});

		setLookAndFeel();

		createHeader();
		createContent();
		createFooter();	
		
		//Load or create properties file
		this.topMenu = new TopMenuBar(this);	
		this.setSize(width, height);
		this.setTitle("Nuskope Usage Meter");
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setIconImage(new ImageIcon("src/resources/icon16.png").getImage());

		//Load properties file
		//Check if config file has a token field
		if (!propFile.getProperty("token").isEmpty()){
			this.node = new DataNode(propFile.getProperty("token"));
			updateGUI(this.node);
			topMenu.setUserInput(this.node.getToken());

			if (node.isValid()){
				topMenu.getTokenMenuItem().setEnabled(false);
			}
			//Check if user input has been entered
		} 
		else if (!topMenu.getUserInput().isEmpty()){
			this.node = new DataNode(topMenu.getUserInput());
			updateGUI(this.node);

			if (node.isValid()){
				topMenu.getTokenMenuItem().setEnabled(false);
			}
		} else { //File needs to exist!!! Otherwise run .jar twice.
			System.out.println("No input to setup node with...First time setup?");
		}

		//Set Location and token
		if (!propFile.getProperty("x_pos").equals(null) && !propFile.getProperty("y_pos").equals(null)){
			this.setLocation(
					Integer.parseInt(this.propFile.getProperty("x_pos")),
					Integer.parseInt(this.propFile.getProperty("y_pos")));
		}
		
		this.setVisible(true);	
	}

	/**
	 * Method to "update the labels/bars used on the frame
	 * @throws Exception 
	 */
	public void updateGUI(DataNode info) {
		if (info.equals(null)) throw new NullPointerException("Node is null!");

		try {
			resetDayDate			= 	info.lastResetDateDay;
			float dateDistanceFloat = 	calculateDateDistance((resetDayDate)); //Result rounds to 1 otherwise

			this.lblDays.setText(calculateDateDistance(resetDayDate) + " Days"); 
			this.lblUsage.setText(info.downloadsToDate + " GB");
			this.pgbUsageRemain.setValue(planQuotaDivider(info.planQuotaGB, info.downloadsToDate));
			this.pgbDaysMonth.setValue((int) ((dateDistanceFloat/31) * 100));			
			this.lblplanNameDYNAMIC.setText(info.planName);
			this.lblQuotaDYNAMIC.setText(info.planQuotaGB + " GB");
			this.lblDownSpeedDYNAMIC.setText(info.downSpeed + " Mb/s");
			this.lblUpSpeedDYNAMIC.setText(info.upSpeed + " Mb/s");
			this.lblDownloadQuotaDYNAMIC.setText(info.downloadsToDate + " GB");
			this.lblUploadQuotaDYNAMIC.setText(info.uploadsToDate + " GB");
			this.lblResetDateDYNAMIC.setText(
					info.lastResetDate.get(Calendar.DAY_OF_MONTH) + "-" +
							info.lastResetDate.get(Calendar.MONTH) + "-" +
							info.lastResetDate.get(Calendar.YEAR) );
			this.lblDateFooter.setText(
					"Last updated: " + 	
							currentDate.get(Calendar.DAY_OF_MONTH) + "-" +
							currentDate.get(Calendar.MONTH) + "-" +
							currentDate.get(Calendar.YEAR) + "  " + 
							currentDate.get(Calendar.HOUR_OF_DAY) + ":" +  
							currentDate.get(Calendar.MINUTE) ); //+currentDate.get(Calendar.MILLISECOND));
			updateUsageColour();
		} 
		catch (Exception ex) {
			System.out.println("CAUGHT EXCEPTION ON REFRESH");
			ex.printStackTrace();
			this.pgbUsageRemain.setValue(0);
			this.pgbDaysMonth.setValue(0);
			this.lblDateFooter.setText("Last updated: " );
			this.lblDays.setText("");
			this.lblUsage.setText("");
			this.lblplanNameDYNAMIC.setText("");
			this.lblQuotaDYNAMIC.setText("");
			this.lblDownSpeedDYNAMIC.setText("");
			this.lblUpSpeedDYNAMIC.setText("");
			this.lblDownloadQuotaDYNAMIC.setText("");
			this.lblUploadQuotaDYNAMIC.setText("");
			this.lblResetDateDYNAMIC.setText("");
		} 
	}	

	/**
	 * Modify the progress bar colours according to their respect value.
	 */
	private void updateUsageColour() {
		if (pgbUsageRemain.getValue() >= 30 ){
			pgbUsageRemain.setForeground(new Color(255,215,0));
		} else if (pgbUsageRemain.getValue() >= 50 ){
			pgbUsageRemain.setForeground(new Color(255,165,0));
		} else if  (pgbUsageRemain.getValue() >= 70 ){
			pgbUsageRemain.setForeground(new Color(255,140,0));
		} else if  (pgbUsageRemain.getValue() >= 80 ){
			pgbUsageRemain.setForeground(new Color(255,69,0));
		}	
	}

	/**
	 * Method to set the "theme" of the JSwing API
	 */
	private void setLookAndFeel() {	
		// Set System L&F for the relevant systems
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}  catch (Exception ex) {
			System.err.println("UI L&F EXCEPTION: " + ex.getMessage());
		}
	}

	/**
	 * Method to create the frame header (north)
	 */
	private void createHeader(){
		JPanel paneHeader = new JPanel();
		paneHeader.setBackground(UIManager.getColor("Button.background"));
		paneHeader.setPreferredSize(new Dimension(100,150));
		paneHeader.setLayout(null);
		this.add(paneHeader, BorderLayout.NORTH);

		pgbUsageRemain = new JProgressBar();
		pgbUsageRemain.setStringPainted(true);
		pgbUsageRemain.setForeground(Color.GREEN);
		pgbUsageRemain.setBounds(27, 36, 336, 23);
		paneHeader.add(pgbUsageRemain);

		pgbDaysMonth = new JProgressBar();
		pgbDaysMonth.setStringPainted(true);
		pgbDaysMonth.setBounds(27, 103, 336, 23);
		paneHeader.add(pgbDaysMonth);

		JLabel lblMeteredUsageRemaining = new JLabel("Metered Usage Used: ");
		lblMeteredUsageRemaining.setBounds(27, 11, 188, 14);
		paneHeader.add(lblMeteredUsageRemaining);

		JLabel lblDaysInMonth = new JLabel("Days to Reset:");
		lblDaysInMonth.setBounds(27, 78, 188, 14);
		paneHeader.add(lblDaysInMonth);

		lblDays = new JLabel();
		lblDays.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDays.setBounds(290, 78, 73, 14);
		paneHeader.add(lblDays);

		lblUsage = new JLabel();
		lblUsage.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsage.setBounds(290, 11, 73, 14);
		paneHeader.add(lblUsage);

		this.add(paneHeader, BorderLayout.NORTH);
	}

	/**
	 * Method to create the frame content (centered)
	 */
	private void createContent() {
		JPanel paneContent = new JPanel();
		paneContent.setBackground(Color.WHITE);
		paneContent.setLayout(null);

		btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(140, 240, 107, 23);
		btnRefresh.addActionListener(this);
		paneContent.add(btnRefresh);

		JPanel contentContainer = new JPanel();
		contentContainer.setLayout(new BorderLayout());
		contentContainer.setBounds(25, 10, 338, 220);

		JPanel paneType = new JPanel();
		paneType.setLayout(null);
		paneType.setBorder(new TitledBorder("Plan Type"));
		paneType.setBackground(new Color(255,255,255));
		paneType.setPreferredSize(new Dimension(100, 120));

		JLabel lblPlanName = new JLabel("Name: ");
		lblPlanName.setSize(100, 20);
		lblPlanName.setLocation(10, 20);

		lblplanNameDYNAMIC = new JLabel("");
		lblplanNameDYNAMIC.setSize(150, 20);
		lblplanNameDYNAMIC.setLocation(200, 20);

		JLabel lblQuota = new JLabel("Quota: ");
		lblQuota.setSize(100, 20);
		lblQuota.setLocation(10, 40);

		lblQuotaDYNAMIC = new JLabel("");
		lblQuotaDYNAMIC.setSize(150, 20);
		lblQuotaDYNAMIC.setLocation(200, 40);

		JLabel lblDownSpeed = new JLabel("Download Speed: ");
		lblDownSpeed.setSize(100, 20);
		lblDownSpeed.setLocation(10, 60);

		lblDownSpeedDYNAMIC = new JLabel("");
		lblDownSpeedDYNAMIC.setSize(150, 20);
		lblDownSpeedDYNAMIC.setLocation(200, 60);

		JLabel lblUploadSpeed = new JLabel("Upload Speed: ");
		lblUploadSpeed.setSize(100, 20);
		lblUploadSpeed.setLocation(10, 80);

		lblUpSpeedDYNAMIC = new JLabel("");
		lblUpSpeedDYNAMIC.setSize(150, 20);
		lblUpSpeedDYNAMIC.setLocation(200, 80);

		paneType.add(lblPlanName);
		paneType.add(lblDownSpeed);
		paneType.add(lblUploadSpeed);
		paneType.add(lblQuota);

		paneType.add(lblplanNameDYNAMIC);
		paneType.add(lblDownSpeedDYNAMIC);
		paneType.add(lblUpSpeedDYNAMIC);
		paneType.add(lblQuotaDYNAMIC);

		JPanel usagePane = new JPanel();
		usagePane.setLayout(null);
		usagePane.setPreferredSize(new Dimension(100,40));
		usagePane.setBackground(new Color(255,255,255));
		usagePane.setBorder(new TitledBorder("Usage"));

		JLabel lblDownloadQuota = new JLabel("Download Quota Used: ");
		lblDownloadQuota.setSize(120, 20);
		lblDownloadQuota.setLocation(10, 20);

		lblDownloadQuotaDYNAMIC = new JLabel("");
		lblDownloadQuotaDYNAMIC.setSize(120, 20);
		lblDownloadQuotaDYNAMIC.setLocation(200, 20);

		JLabel lblUploadQuota = new JLabel("Upload Quota Used: ");
		lblUploadQuota.setSize(120, 20);
		lblUploadQuota.setLocation(10, 40);

		lblUploadQuotaDYNAMIC = new JLabel("");
		lblUploadQuotaDYNAMIC.setSize(120, 20);
		lblUploadQuotaDYNAMIC.setLocation(200, 40);

		JLabel lblResetDate = new JLabel("Last Reset Date: ");
		lblResetDate.setSize(120, 20);
		lblResetDate.setLocation(10, 60);

		lblResetDateDYNAMIC = new JLabel("");
		lblResetDateDYNAMIC.setSize(120, 20);
		lblResetDateDYNAMIC.setLocation(200, 60);

		usagePane.add(lblDownloadQuota);
		usagePane.add(lblUploadQuota);
		usagePane.add(lblResetDate);
		usagePane.add(lblDownloadQuotaDYNAMIC);
		usagePane.add(lblUploadQuotaDYNAMIC);
		usagePane.add(lblResetDateDYNAMIC);

		contentContainer.add(paneType, BorderLayout.NORTH);
		contentContainer.add(usagePane, BorderLayout.CENTER);
		paneContent.add(contentContainer);

		this.add(paneContent, BorderLayout.CENTER);
	}

	/**
	 * Method to create the footer area, containing the date information (south)
	 */
	private void createFooter() {
		lblDateFooter = new JLabel();
		lblDateFooter.setText("Last updated: "); 

		JPanel footerArea = new JPanel();
		footerArea.setLayout(new BorderLayout());
		footerArea.setBorder(new EmptyBorder(0,10,0,0));
		footerArea.setPreferredSize((new Dimension(this.getX(), 25)));
		footerArea.add(lblDateFooter);	

		this.add(footerArea, BorderLayout.SOUTH);
	}

	/**
	 * Action Listener for all action events in this class
	 * @param e - The ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("REFRESH PRESSED");

		if (e.getActionCommand().equals("Refresh")){
			try {
				DataNode temp = new DataNode(this.topMenu.getUserInput());
				if (temp.isValid()){
					DataNode updateNode = new DataNode(temp.getToken());
					updateGUI(updateNode);
					this.topMenu.setToken.setEnabled(false);
				} else{
					this.topMenu.setUserInput("");
					this.topMenu.setToken.setEnabled(true);
				}
			} catch (NullPointerException ex){
				System.out.println("Token from top menu hasn't been set? Properties file deleted?");
			}
		}
	}

	/**
	 * Function to find the LCD of the users monthly quota limit
	 * @param planQuotaGB - The monthly quota allocated
	 * @param downloadsToDate - The amount of quota used
	 * @return - the LCD of the monthly quota
	 * @throws IllegalArgumentException 
	 */
	public int planQuotaDivider(int planQuotaGB, float downloadsToDate) {
		//if (planQuotaGB < 10) throw new IllegalArgumentException("Plan quota is too small! Invalid token?");

		return ( (int) ( (downloadsToDate / planQuotaGB) * 100 ) );
	}	

	/**
	 * Function to find the number of days difference from today to the reset date
	 * @param resetDay - the day your plan resets
	 * @return - the number of days difference from current date to reset
	 * @throws Exception - invalid reset date 
	 */
	public int calculateDateDistance(int resetDate) {
		if (resetDate < 0 || resetDate > 31) throw new IllegalArgumentException("Invalid reset date!");

		int result = 0;
		int daysInMonth = currentDate.getActualMaximum(Calendar.DAY_OF_MONTH);

		if (currentDayInInt < resetDate){
			result = resetDate - currentDayInInt;
		}
		if (currentDayInInt == resetDate){
			result =  daysInMonth -(currentDayInInt - resetDate) ;
		}
		if (currentDayInInt > resetDate){
			result =  daysInMonth -(currentDayInInt - resetDate) ;
		}
		return result;
	}

}