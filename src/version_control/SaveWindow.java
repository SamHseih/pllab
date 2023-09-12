package version_control;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
//import java.awt.event.*;
import javax.swing.WindowConstants;

import ccu.pllab.tcgen.launcher.BlackBoxLauncher;
import version_AST.ReduceASTCreate;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollBar;
import javax.swing.DropMode;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class SaveWindow {

	public JFrame frmSave;
	private JTextField textField_1;
	private JComboBox<String> comboBox_compare_version;
	private JTextField txtVersion;
	private JComboBox<String> comboBox;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SaveWindow window = new SaveWindow();
					window.frmSave.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void switch_window(SaveWindow window) {
		window.frmSave.setVisible(true);
	}

	public void setSpecInfo(String specFile, String specPath) {
		String specName = specFile.substring(0, specFile.lastIndexOf("."));
		for(int i = 0; i < this.comboBox.getItemCount(); i++) {
			if(this.comboBox.getItemAt(i).equals(specName)) {
				this.comboBox.setSelectedIndex(i);
				VersionControlDB dbconnect = new VersionControlDB();
				dbconnect.connectDB();
				Statement stmt;
				try {
					stmt = dbconnect.connect.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT `specification_version` FROM `specification_version` WHERE `specification_name` = '"+comboBox.getSelectedItem()+"' AND `user` = '"+LoginWindow.account+"'");
		/*			int max = 0;
					while(rs.next()) {
						if(Integer.parseInt(rs.getString("specification_version")) > max) {
							max = Integer.parseInt(rs.getString("specification_version"));
						}
					}
					max+=1;
					txtVersion.setText("version " + max);
					comboBox_compare_version.removeAllItems();
					for(int j = max-1; j >= 0; j--) {
						comboBox_compare_version.addItem(String.valueOf(j));
					}*/
					//
					ArrayList<String> allVersion = new ArrayList<String>();
					
					while(rs.next()) {
						allVersion.add(rs.getString("specification_version"));
					}
					//Collections.sort(allVersion);
					Collections.reverse(allVersion);
					txtVersion.setText("version " + (Integer.parseInt(((allVersion.get(0).split("\\."))[0]))+1)); //取最新版本+1的版本 省去小數點
					comboBox_compare_version.removeAllItems();
					for(String ver: allVersion) {
						comboBox_compare_version.addItem(ver);
					}
					//
					rs.close();	rs = null;
					stmt.close(); stmt = null;
					dbconnect.closeDB();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String projectPath2 = specPath.substring(0, specPath.lastIndexOf("\\"));
				String projectPath = projectPath2.substring(0, projectPath2.lastIndexOf("\\"));
				textField_1.setText(projectPath);
				
				break;
			}
		}
	}
	
	/**
	 * Create the application.
	 */
	public SaveWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSave = new JFrame();
		frmSave.setTitle("save version");
		frmSave.setBounds(100, 100, 600, 510);
		frmSave.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSave.getContentPane().setLayout(null);
		
		JButton btnSave = new JButton("back");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartWindow window = new StartWindow();
				window.switch_window(window);
				frmSave.dispose();
			}
		});
		btnSave.setBackground(UIManager.getColor("List.selectionBackground"));
		btnSave.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		btnSave.setBounds(469, 419, 99, 35);
		frmSave.getContentPane().add(btnSave);
		
		JLabel structure_compare_version_label = new JLabel("compare version");
		structure_compare_version_label.setFont(new Font("微軟正黑體 Light", Font.BOLD, 20));
		structure_compare_version_label.setBounds(14, 133, 176, 35);
		frmSave.getContentPane().add(structure_compare_version_label);
		
		comboBox_compare_version = new JComboBox<String>();
		comboBox_compare_version.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		comboBox_compare_version.setBounds(191, 139, 80, 25);
		frmSave.getContentPane().add(comboBox_compare_version);
		
		JLabel coverageCriteria = new JLabel("coverage criteria");
		coverageCriteria.setFont(new Font("微軟正黑體 Light", Font.BOLD, 20));
		coverageCriteria.setBounds(14, 234, 176, 35);
		frmSave.getContentPane().add(coverageCriteria);
		
		JComboBox coverageCriteria_comboBox = new JComboBox();
		coverageCriteria_comboBox.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		coverageCriteria_comboBox.addItem("DC");
		coverageCriteria_comboBox.addItem("DCC");
		coverageCriteria_comboBox.setBounds(191, 242, 80, 25);
		frmSave.getContentPane().add(coverageCriteria_comboBox);
		
		textField_1 = new JTextField();
		textField_1.setText("<choose a project path>");
		textField_1.setEditable(false);
		textField_1.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		textField_1.setColumns(10);
		textField_1.setBounds(191, 190, 263, 25);
		frmSave.getContentPane().add(textField_1);
		
		comboBox = new JComboBox<String>();
		comboBox.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		comboBox.setBounds(191, 43, 263, 25);
		frmSave.getContentPane().add(comboBox);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				try {
					VersionControlDB dbconnect = new VersionControlDB();
					dbconnect.connectDB();
					Statement stmt = dbconnect.connect.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT `specification_version` FROM `specification_version` WHERE `specification_name` = '"+comboBox.getSelectedItem()+"' AND `user` = '"+LoginWindow.account+"'");
					
					ArrayList<String> allVersion = new ArrayList<String>();
					
					while(rs.next()) {
						allVersion.add(rs.getString("specification_version"));
					}
				//	Collections.sort(allVersion);
					Collections.reverse(allVersion);				

					txtVersion.setText("version " + (Integer.parseInt(((allVersion.get(0).split("\\."))[0]))+1)); //取最新版本+1的版本 省去小數點
					comboBox_compare_version.removeAllItems();
					for(String ver: allVersion) {
						comboBox_compare_version.addItem(ver);
					}
					
					rs.close();	rs = null;
					stmt.close(); stmt = null;
					dbconnect.closeDB();

					System.out.println("connect close success!");
				} catch(Exception e) {
					System.out.println("connect fail:" + e);
				}
			}		
		});
		
		JLabel lblJavaProject = new JLabel("project");
		lblJavaProject.setFont(new Font("微軟正黑體 Light", Font.BOLD, 20));
		lblJavaProject.setBounds(14, 37, 130, 35);
		frmSave.getContentPane().add(lblJavaProject);
		
		JLabel lblOclFile = new JLabel("project path");
		lblOclFile.setFont(new Font("微軟正黑體 Light", Font.BOLD, 20));
		lblOclFile.setBounds(14, 181, 130, 40);
		frmSave.getContentPane().add(lblOclFile);
		
		JButton button = new JButton("browse");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser=new JFileChooser(new File("D:\\runtime-New_configuration(1)"));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if( fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
				{
					File file_exit_test = new File(fileChooser.getSelectedFile()+"//spec//"+comboBox.getSelectedItem().toString()+".ocl");
					if(file_exit_test.exists()) {
						file_exit_test = new File(fileChooser.getSelectedFile()+"//spec//"+comboBox.getSelectedItem().toString()+".uml");
						if(file_exit_test.exists()) {
							File selectedFile=fileChooser.getSelectedFile();
							textField_1.setText(selectedFile.getAbsolutePath());
						}
						else {
							JOptionPane.showMessageDialog(null, "There are no .uml file in this directory '"+comboBox.getSelectedItem().toString()+"'.", "error", 
									JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "There are no .ocl file in this directory '"+comboBox.getSelectedItem().toString()+"'.", "error", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		button.setFont(new Font("微軟正黑體 Light", Font.BOLD, 12));
		button.setBounds(488, 191, 80, 27);
		frmSave.getContentPane().add(button);
		
		JLabel lblVersion = new JLabel("version");
		lblVersion.setFont(new Font("微軟正黑體 Light", Font.BOLD, 20));
		lblVersion.setBounds(14, 85, 130, 35);
		frmSave.getContentPane().add(lblVersion);
		
		txtVersion = new JTextField();
		txtVersion.setText("version -1");
		txtVersion.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		txtVersion.setColumns(10);
		txtVersion.setBounds(191, 91, 157, 25);
		frmSave.getContentPane().add(txtVersion);
		
		JLabel lblDescription = new JLabel("description");
		lblDescription.setFont(new Font("微軟正黑體 Light", Font.BOLD, 20));
		lblDescription.setBounds(14, 282, 163, 35);
		frmSave.getContentPane().add(lblDescription);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(0, -54, 330, 174);
		textPane.setText("1.\r\n2.\r\n");
		textPane.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		//textPane.setBounds(323, 235, 219, 98);
		frmSave.getContentPane().add(textPane);
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(191, 282, 377, 124);
		frmSave.getContentPane().add(scrollPane);
		
		
		try {	//connect server to take maximum version 
			VersionControlDB dbconnect = new VersionControlDB();
			dbconnect.connectDB();
			Statement stmt = dbconnect.connect.createStatement();
			Statement stmt2 = dbconnect.connect.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `specification_name` WHERE `user` = '"+LoginWindow.account+"'");
			rs.next();
			comboBox.addItem(rs.getString("specification_name"));
			ResultSet rs2 = stmt2.executeQuery("SELECT `specification_version` FROM `specification_version` WHERE `specification_name` = '"+rs.getString("specification_name")+"' AND `user` = '"+LoginWindow.account+"'");
			ArrayList<String> allVersion = new ArrayList<String>();
			
			while(rs2.next()) {
				allVersion.add(rs2.getString("specification_version"));
			}
			//Collections.sort(allVersion);
			Collections.reverse(allVersion);
			txtVersion.setText("version " + (Integer.parseInt(((allVersion.get(0).split("\\."))[0]))+1)); //取最新版本+1的版本 省去小數點
			comboBox_compare_version.removeAllItems();
			for(String ver: allVersion) {
				comboBox_compare_version.addItem(ver);
			}
			//comboBox_compare_version.addItem("0");
			while(rs.next()) {
				comboBox.addItem(rs.getString("specification_name"));
			}			
			rs2.close();	rs2 = null;
			stmt2.close(); stmt2 = null;
			rs.close();	rs = null;
			stmt.close(); stmt = null;
			dbconnect.closeDB();
			System.out.println("connect close success!");
		} catch(Exception e) {
			System.out.println("connect wrong!");
			System.out.println(e);
		}
		
		JButton button_4 = new JButton("save");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String verNum = ((txtVersion.getText()).split(" "))[1];
				if(!textField_1.getText().contains("<")) {									
					if((verNum.split("\\.")).length <= 3) {
						String[] s = verNum.split("\\.");
						int firstVerNum = Integer.parseInt(s[0]);					
						int maxVer = Integer.parseInt(((comboBox_compare_version.getItemAt(0)).split("\\."))[0])+1;						
						
						if(firstVerNum != 0 && firstVerNum <= maxVer) {
							boolean verIsExist = false;
							String ver = txtVersion.getText();
							for(int i = 0; i < comboBox_compare_version.getItemCount(); i++) {
								if(ver.equals(comboBox_compare_version.getItemAt(i))) {
									verIsExist = true;
									break;
								}
							}						
							if(!verIsExist) {
								int result=JOptionPane.showConfirmDialog(frmSave,
						                   "Are you sure you want to save this folder?\n"+
						                   "version: "+txtVersion.getText()+"\n"+
						                   "project file path: "+textField_1.getText()+"\n"+
						                   "coverage criteria: "+coverageCriteria_comboBox.getSelectedItem().toString()+"\n"+
						                   "description: "+textPane.getText()+"\n",
						                   "save",
						                   JOptionPane.YES_NO_OPTION,
						                   JOptionPane.WARNING_MESSAGE);
						        if (result==JOptionPane.YES_OPTION) { //save to database
						        	try {
						        		//
						        		String specname = comboBox.getSelectedItem().toString();
						        		String savefilepath = specname+"\\"+txtVersion.getText();
						        		
						        		//建立testdata 黑箱DC的
						        		File oclFile = new File(textField_1.getText()+"//spec//"+specname+".ocl");
						        		File umlFile = new File(textField_1.getText()+"//spec//"+specname+".uml");
						        		BlackBoxLauncher b = new BlackBoxLauncher(oclFile, umlFile);
						        		b.genBlackBoxTestScripts_VersionControl(specname, savefilepath+"\\test data",txtVersion.getText(), comboBox_compare_version.getSelectedItem().toString(), coverageCriteria_comboBox.getSelectedItem().toString());
						        		//FileHandleToServer.storeFile(savefilepath,specname+".ocl",oclpath);
						        		
						        		System.out.println("test data generate success!");
						        		
						        		FileInputStream oclpath = new FileInputStream(new File(textField_1.getText()+"//spec//"+specname+".ocl"));
						        		FileInputStream umlpath = new FileInputStream(new File(textField_1.getText()+"//spec//"+specname+".uml"));
						        	//	FileInputStream javacodepath = new FileInputStream(new File(textField_1.getText()+"//src//"+specname+".java"));			        		
						        		String nullString = "Not yet implemented!";
						        		InputStream javacodepath = new ByteArrayInputStream(nullString.getBytes());
						        		//建立ReduceAST and reduceAST的.xml
						        		Path oclPath = Paths.get(textField_1.getText()+"//spec//"+specname+".ocl");
						        		File ocl = oclPath.toFile();
						        	//	ReduceASTCreate reduceAST = new ReduceASTCreate(ocl, specname);
						        	//	String s = ReduceASTCreate.ast2xml(reduceAST.getReduceAST());
						        	//	InputStream ast2umlfile = new ByteArrayInputStream(s.getBytes());
						        		
						        		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						        		Date date = new Date();
						        		String strDate = sdFormat.format(date);
						        		String[] stringsplit = txtVersion.getText().split(" ");
						        		String fileversion = stringsplit[1];
						        		//save the file to server
						        		FileHandleToServer.storeFile(savefilepath,specname+".ocl",oclpath);
						        		FileHandleToServer.storeFile(savefilepath,specname+".uml",umlpath);
						        		FileHandleToServer.storeFile(savefilepath,specname+".java",javacodepath);
						        	//	FileHandleToServer.storeFile(savefilepath,specname+"AST2UML.uml",ast2umlfile);
						        		
						        		
						        		//save the file information to database
						        		VersionControlDB dbconnect = new VersionControlDB();
										dbconnect.connectDB();
										Statement stmt = dbconnect.connect.createStatement();
						    			stmt.executeUpdate("INSERT INTO `specification_version`(`specification_name`, `specification_version`, `create_time`, `description`, `user`) VALUES"
						    					+ " ('"+specname+"','"+fileversion+"','"+strDate+"','"+textPane.getText()+"','"+LoginWindow.account+"')");
						    			stmt.close(); stmt = null;
						    			dbconnect.closeDB();
						    			System.out.println("connect close success!");
						        		//success dialog
						        		JOptionPane.showMessageDialog(null,"version save is success!","success", JOptionPane.CLOSED_OPTION);
						        		StartWindow window = new StartWindow();
										window.switch_window(window);
										frmSave.dispose();
						        	}
						        	catch(Exception ex) {
						        		System.out.println("connect error");
						        		System.out.println(ex);
						        	}
						        }
							}
							else {
								JOptionPane.showMessageDialog(null, "version is already existed!", "error", 
										JOptionPane.ERROR_MESSAGE);
							}
						}
						else {
							JOptionPane.showMessageDialog(null, "wrong version range", "error", 
									JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "wrong version type, please hint XX.XX.XX", "error", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "please choose the right file path", "error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button_4.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		button_4.setBounds(356, 419, 99, 35);
		frmSave.getContentPane().add(button_4);
		
		
		this.frmSave.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//關閉事件->都不做事(不關閉)
		frmSave.addWindowListener(new WindowAdapter() 
		{
		    public void windowClosing(WindowEvent event) {
		    	int result=JOptionPane.showConfirmDialog(frmSave,
		                   "Are you sure you want to leave the save window?",
		                   "Exit",
		                   JOptionPane.YES_NO_OPTION,
		                   JOptionPane.WARNING_MESSAGE);
		        if (result==JOptionPane.YES_OPTION) {frmSave.dispose();}	//選擇YES才關閉frame
		    
		}});
		
		
	}
}
