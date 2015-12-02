package com.ebay.quality.compare;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import java.awt.Color;

import javax.swing.ScrollPaneConstants;

import java.awt.Font;

import javax.swing.ImageIcon;

import java.awt.Toolkit;
import java.beans.PropertyChangeListener;

import com.ebay.quality.compare.Executor;

import javax.swing.JProgressBar;
import javax.swing.SwingConstants;




public class Vivid {
	static Logger logger = Logger.getLogger(Vivid.class);
	private JFrame frmVividaVisualTesting;
	private static String userAgent;
	private static List<String> browsers=new ArrayList<String>();
	private final String mobileResolution="mobileResolution=600px*4000px";
	private final String tabletResolution="tabletResolution=768px*4000px";
	private final String desktopResolution="desktopResolution=1920px*5500px";
	private String threadCounts="threadCount=15";
	private final String writeScreenShotsAt="writeScreenShotsAt=screenshots";
	private String configTxt;
	private boolean isConfigUploaded=false;
	private String prgrsBar = "Running.....";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) { 
		logger.info("Args length:"+args.length);
		/*
		 * Comment following lines if you want to build 
		 * Command Line version of jar
		 * Keep them uncommented if you want to build 
		 * as a executable Swing application
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Vivid window = new Vivid();
					window.frmVividaVisualTesting.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//END comments
		
		/*
		 * Comment below lines if you are building swing application
		 * Leave them uncommented if you are building CLI jar
		 */
				Executor.startVivid(args);
		//END
	}

	/**
	 * Create the application
	 */
	public Vivid() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVividaVisualTesting = new JFrame();
		frmVividaVisualTesting.setResizable(false);
		frmVividaVisualTesting.setIconImage(Toolkit.getDefaultToolkit().getImage("/Users/kvikram/Desktop/logo.png"));
		frmVividaVisualTesting.setTitle("VIVID-A VISUAL TESTING TOOL");
		frmVividaVisualTesting.setBounds(100, 100, 1022, 745);
		frmVividaVisualTesting.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel.setBackground(new Color(211, 211, 211));
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(8);
		frmVividaVisualTesting.getContentPane().add(panel, BorderLayout.NORTH);
		
		JLabel lblVivid = new JLabel("");
		lblVivid.setIcon(new ImageIcon(getClass().getResource("/vividlogo.png")));
		lblVivid.setFont(new Font("BlairMdITC TT", Font.PLAIN, 25));
		lblVivid.setBackground(Color.WHITE);
		lblVivid.setForeground(Color.BLACK);
		panel.add(lblVivid);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBackground(new Color(211, 211, 211));
		lblVivid.setLabelFor(panel_1);
		frmVividaVisualTesting.getContentPane().add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(null);
		panel_1.setPreferredSize(new Dimension(1700, 1500)); 
		
		JLabel lblNewLabel = new JLabel("Urls:");
		lblNewLabel.setBounds(32, 265, 31, 27);
		panel_1.add(lblNewLabel);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(211, 211, 211));
		panel_3.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(75, 92, 873, 51);
		panel_1.add(panel_3);
		panel_3.setLayout(null);
		
		
		JLabel browserType = new JLabel("Browser Type:");
		browserType.setBounds(162, 16, 91, 16);
		panel_3.add(browserType);
		
		final JCheckBox desktop = new JCheckBox("Desktop");
		desktop.setBounds(263, 12, 91, 23);
		panel_3.add(desktop);
		
		final JCheckBox tablet = new JCheckBox("Tablet");
		tablet.setBounds(366, 12, 71, 23);
		panel_3.add(tablet);
		
		final JCheckBox mobile = new JCheckBox("Mobile");
		mobile.setBounds(449, 12, 80, 23);
		panel_3.add(mobile);
		
		JLabel threadCount = new JLabel("Thread Count:");
		threadCount.setBounds(540, 16, 91, 16);
		panel_3.add(threadCount);
		
		final JComboBox threadCountDD = new JComboBox();
		threadCountDD.setBounds(646, 12, 80, 27);
		panel_3.add(threadCountDD);
		threadCountDD.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}));
		
		threadCountDD.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				threadCounts="threadCount="+threadCountDD.getSelectedItem().toString();
			}
		});
		mobile.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				compileChkBoxes(mobile);
				
			}
		});
		tablet.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				compileChkBoxes(tablet);
				
			}
		});
		desktop.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				compileChkBoxes(desktop);
				
			}
		});
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(211, 211, 211));
		panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(420, 23, 242, 53);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		JButton uploadBtn = new JButton("Upload URLs");
		uploadBtn.setBounds(48, 13, 147, 29);
		panel_2.add(uploadBtn);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(211, 211, 211));
		panel_4.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(400, 428, 257, 53);
		panel_1.add(panel_4);
		panel_4.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(75, 155, 873, 245);
		panel_1.add(scrollPane);
		
		
		final JTextArea txtrEnterYourUrl = new JTextArea();
		scrollPane.setViewportView(txtrEnterYourUrl);
		txtrEnterYourUrl.setToolTipText("Enter URL pairs | separated ");
		txtrEnterYourUrl.setLineWrap(false);
		
		JButton run = new JButton("RUN");
		run.setBounds(6, 13, 117, 29);
		panel_4.add(run);
		
//		JButton cancel = new JButton("CANCEL");
//		cancel.setBounds(525, 446, 117, 29);
//		panel_1.add(cancel);
		
		JButton exit = new JButton("EXIT");
		exit.setBounds(134, 13, 117, 29);
		panel_4.add(exit);
		
		final JLabel statusLabel = new JLabel("");
		statusLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statusLabel.setBounds(431, 493, 188, 36);
		panel_1.add(statusLabel);
		
		/**
		 * Exits the application when user clicks on 
		 * Exit button or X button on panel
		 */
		exit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
				
			}
		});
		
		/**
		 * Upload button listener to perform 
		 * the upload action when user clicks on upload button
		 */
		uploadBtn.addActionListener(new ActionListener() {			
			 public void actionPerformed(ActionEvent ae) {
			        JFileChooser chooser = new JFileChooser();
	            	String line;
	            	String urls=null;
			        chooser.setMultiSelectionEnabled(false);
			        int option = chooser.showOpenDialog(frmVividaVisualTesting);
			        if (option == JFileChooser.APPROVE_OPTION) {
			          File sf = chooser.getSelectedFile();
			              try {
			            	  BufferedReader readBuff = new BufferedReader(new FileReader(sf));
			            	  while((line=readBuff.readLine())!=null){
			            		  if(line.toLowerCase().startsWith("url"))
			            			  urls = urls==null?line+"\n":urls+line+"\n";		         		            			  
			            	  }	
			            	  readBuff.close();
			            	  configTxt = urls;
			            	  logger.info("configTxt\n"+configTxt);
		            		  txtrEnterYourUrl.setText(urls);
		            		  isConfigUploaded=true;
						}catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			      
			        }
			 }
		});
		
		/**
		 * Run button listener to trigger the execution of the main 
		 * program
		 */
		run.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				String errorMsg=null;
				userAgent=null;
				StringBuffer urlstrBuff = new StringBuffer();
				if(!isConfigUploaded){
				String[] urls = txtrEnterYourUrl.getText().split("\n");
				for(String url:urls){
					urlstrBuff.append(url.toLowerCase()).append("\n");
				}
				configTxt = urlstrBuff.toString();
				}
				for(String browser:browsers){
					if(userAgent==null){
						userAgent="userAgent="+browser.toLowerCase();
					}
					else
						userAgent=userAgent+","+browser.toLowerCase();
				}
				if(urlstrBuff.length()==5)
					errorMsg =errorMsg==null?"Please enter the test url. Test url field is empty\n":errorMsg+"Please enter the test url. Test url field is empty\n";
				if(userAgent==null || userAgent.length()==0)
					errorMsg = errorMsg==null?"Please choose atleast one browser type\n":errorMsg +"Please choose atleast one browser type\n";
				if(errorMsg!=null){
					JOptionPane.showMessageDialog(frmVividaVisualTesting, errorMsg,"Erros!", JOptionPane.ERROR_MESSAGE);
					return;
				}	
				
				configTxt = configTxt + userAgent + "\n" + threadCounts + "\n" + mobileResolution + "\n" + tabletResolution + "\n" + desktopResolution + "\n" + writeScreenShotsAt;
				String filePath = createConfigFile();
				final String[] args = {"-propertyFilePath",filePath};
				logger.info(filePath);
				//Executor.startVivid(args);
				SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {				
					 protected Boolean doInBackground() throws Exception {
						 String prgrs = ".";						 
						 publish(getProgressbar(prgrs));
						 Thread.sleep(1000L);
						 Executor.startVivid(args);	
					  return true;
					 }

					 protected void done() {

						  boolean status;
						  try {
						   status = get();
						   if(status)
							   statusLabel.setText("Completed...");
						  } catch (InterruptedException e) {
						  } catch (ExecutionException e) {
						  }
					 }
					 
					 protected void process(List<String> chunks) {
						 statusLabel.setText(chunks.get(0));
						 System.out.println(statusLabel.getText());
						 }
					 

					};
					worker.execute();				
			}
			
			
		});
		
		
	}
	
	/*
	 * Creates a config file based on user's input 
	 * on Swing UI
	 */
	private String createConfigFile(){
		String configFilePath = null;
		try {
			File configFile = new File("config.properties");
			configFilePath=configFile.getAbsolutePath();
			FileWriter fileWriter = new FileWriter(configFile);
			fileWriter.write(configTxt);
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return configFilePath;
		
	}
	
	/*
	 * Validate at least one check box is checked for browsers
	 */
	private void compileChkBoxes(JCheckBox chkBox){
			if(chkBox.isSelected()){
				logger.info(chkBox.getText() +" is selected now");
				if(browsers.size()==0){
					browsers.add(chkBox.getText());
				}
				else if(browsers.size()>0 && !browsers.contains(chkBox.getText())){
					browsers.add(chkBox.getText());
				}
			}else if(browsers.size()>0 && browsers.contains(chkBox.getText())){
				logger.info(chkBox.getText() +" is dselected now");
				browsers.remove(chkBox.getText());
			}
			
				
		}
	
	/**
	 * Gives indication to user that program is still running
	 * @param progrsSignal
	 * @return
	 */
	public String getProgressbar(String progrsSignal){			
		prgrsBar = prgrsBar + progrsSignal;
		if(prgrsBar.equals("Running..........."))
			prgrsBar="Running";
		return prgrsBar;
	}
}
