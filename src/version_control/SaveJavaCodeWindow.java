package version_control;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class SaveJavaCodeWindow {

	public JFrame frame;
	private JTextField textField_1;
	private JTextField txtState;
	private JComboBox<String> projectComboBox;
	private JComboBox<String> versionComboBox;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SaveJavaCodeWindow window = new SaveJavaCodeWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	
	public void setJavaCodeInfo(String specFile, String specPath) {
		String specName = specFile.substring(0, specFile.lastIndexOf("."));
		
		for(int i = 0; i < this.projectComboBox.getItemCount(); i++) {
			if(this.projectComboBox.getItemAt(i).equals(specName)) {
				this.projectComboBox.setSelectedIndex(i);
				
				versionComboBox.removeAllItems();
				try {
					VersionControlDB dbconnect = new VersionControlDB();
					dbconnect.connectDB();
					Statement stmt = dbconnect.connect.createStatement();
					
					ResultSet rs = stmt.executeQuery("SELECT `specification_version` FROM `specification_version` WHERE `specification_name` = '"+projectComboBox.getItemAt(projectComboBox.getSelectedIndex())+"' AND `user` = '"+LoginWindow.account+"'");		
				
					while(rs.next()) {
						versionComboBox.addItem(rs.getString("specification_version"));
					}	
					txtState.setText("Version 0 can't implemented!");
					
					rs.close();	rs = null;
					stmt.close(); stmt = null;			
					dbconnect.closeDB();
					
					System.out.println("connect close success!");
				} catch(Exception e) {
					System.out.println("connect fail:" + e);
				}
				
				String projectPath2 = specPath.substring(0, specPath.lastIndexOf("\\"));
				String projectPath = projectPath2.substring(0, projectPath2.lastIndexOf("\\"));
				textField_1.setText(projectPath+"\\src\\"+specName+".java");
				
				break;
			}
		}
		
	}
	
	
	public void switch_window(SaveJavaCodeWindow window) {
		window.frame.setVisible(true);
	}
	
	public SaveJavaCodeWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("save java code");
		frame.setBounds(100, 100, 574, 348);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		
		
		JButton btnBackButton = new JButton("back");
		btnBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartWindow window = new StartWindow();
				window.switch_window(window);
				frame.dispose();
			}
		});
		btnBackButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		btnBackButton.setBounds(445, 261, 99, 27);
		frame.getContentPane().add(btnBackButton);
		
		txtState = new JTextField();
		txtState.setText(" Version 0 can't implemented!");
		txtState.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		txtState.setEditable(false);
		txtState.setColumns(10);
		txtState.setBounds(187, 141, 263, 25);
		frame.getContentPane().add(txtState);
		
		JLabel lblJavaProject = new JLabel("project");
		lblJavaProject.setFont(new Font("微軟正黑體 Light", Font.BOLD, 20));
		lblJavaProject.setBounds(14, 34, 130, 35);
		frame.getContentPane().add(lblJavaProject);
		
		JLabel lblVersion = new JLabel("version");
		lblVersion.setFont(new Font("微軟正黑體 Light", Font.BOLD, 20));
		lblVersion.setBounds(14, 82, 130, 35);
		frame.getContentPane().add(lblVersion);
		
		versionComboBox = new JComboBox<String>();
		versionComboBox.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		versionComboBox.setBounds(187, 88, 263, 25);
		frame.getContentPane().add(versionComboBox);		
		
		projectComboBox = new JComboBox<String>();
		projectComboBox.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		projectComboBox.setBounds(187, 40, 263, 25);
		frame.getContentPane().add(projectComboBox);
		projectComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				versionComboBox.removeAllItems();
				try {
					VersionControlDB dbconnect = new VersionControlDB();
					dbconnect.connectDB();
					Statement stmt = dbconnect.connect.createStatement();
					
					ResultSet rs = stmt.executeQuery("SELECT `specification_version` FROM `specification_version` WHERE `specification_name` = '"+projectComboBox.getItemAt(projectComboBox.getSelectedIndex())+"' AND `user` = '"+LoginWindow.account+"'");		
				
					while(rs.next()) {
						versionComboBox.addItem(rs.getString("specification_version"));
					}	
					txtState.setText(" Version 0 can't implemented!");
					
					rs.close();	rs = null;
					stmt.close(); stmt = null;			
					dbconnect.closeDB();
					
					System.out.println("connect close success!");
				} catch(Exception e) {
					System.out.println("connect fail:" + e);
				}
			}		
		});
		
		versionComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				VersionControlDB dbconnect = new VersionControlDB();
				dbconnect.connectDB();
				try {
					Statement stmt = dbconnect.connect.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT `javacode_state` FROM `specification_version` WHERE `specification_name` = '"+projectComboBox.getItemAt(projectComboBox.getSelectedIndex())+"' AND `specification_version` = '"+versionComboBox.getSelectedIndex()+"' AND `user` = '"+LoginWindow.account+"'");		
					rs.next();
					txtState.setText(rs.getString("javacode_state"));
					rs.close(); rs = null;
					stmt.close(); stmt = null;			
					dbconnect.closeDB();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JLabel lblJavaCodeFile = new JLabel("java code path");
		lblJavaCodeFile.setFont(new Font("微軟正黑體 Light", Font.BOLD, 20));
		lblJavaCodeFile.setBounds(14, 185, 159, 40);
		frame.getContentPane().add(lblJavaCodeFile);
		
		textField_1 = new JTextField();
		textField_1.setText("<choose a project path>");
		textField_1.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(187, 194, 263, 25);
		frame.getContentPane().add(textField_1);
		
		JButton button = new JButton("browse");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser=new JFileChooser(new File("D:\\runtime-New_configuration(1)"));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if( fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
				{
					File selectedFile = fileChooser.getSelectedFile();
					if(selectedFile.getName().contains(".java")) {
						if(selectedFile.getName().contains(projectComboBox.getItemAt(projectComboBox.getSelectedIndex()))) {
							textField_1.setText(selectedFile.getAbsolutePath());
						}
						else {
							JOptionPane.showMessageDialog(null, " This is not current project file", "error", 
									JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(null, " This is not .java file", "error", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		button.setFont(new Font("微軟正黑體 Light", Font.BOLD, 12));
		button.setBounds(464, 195, 80, 27);
		frame.getContentPane().add(button);
		
		JButton btnSaveButton = new JButton("save");
		btnSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(versionComboBox.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, " Please choose correct version", "error", 
							JOptionPane.ERROR_MESSAGE);
				}
				else if(textField_1.getText().contains("<choose")) {
					JOptionPane.showMessageDialog(null, " Please choose a .java file", "error", 
							JOptionPane.ERROR_MESSAGE);
				}
				else {
					String projectName = projectComboBox.getItemAt(projectComboBox.getSelectedIndex());
					String version = versionComboBox.getItemAt(versionComboBox.getSelectedIndex());
					int result=JOptionPane.showConfirmDialog(frame,
			                   "Are you sure you want to save this java file?\n"+
			                   "project: "+projectName+"\n"+
			                   "version: "+version+"\n"+
			                   "current state: "+txtState.getText()+"\n"+
			                   "java file path: "+textField_1.getText()+"\n",
			                   "save",
			                   JOptionPane.YES_NO_OPTION,
			                   JOptionPane.WARNING_MESSAGE);
			        if (result==JOptionPane.YES_OPTION) { 
			        	//save file
			        	try {
			        		FileInputStream javaPath = new FileInputStream(new File(textField_1.getText()));
							FileHandleToServer.storeFile(projectName+"\\version "+version, projectName+".java",javaPath);
			        	} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			        	//save information to DB
			        	VersionControlDB dbconnect = new VersionControlDB();
						dbconnect.connectDB();
						try {
							Statement stmt = dbconnect.connect.createStatement();
							int rs = stmt.executeUpdate("UPDATE `specification_version` SET `javacode_state`='It has been implemented!' WHERE `specification_name` = '"+projectName+"' AND `specification_version` = '"+version+"' AND `user` = '"+LoginWindow.account+"'");		
							
							stmt.close(); stmt = null;			
							dbconnect.closeDB();
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						//success dialog
		        		JOptionPane.showMessageDialog(null,"java file save is success!","success", JOptionPane.CLOSED_OPTION);
		        		StartWindow window = new StartWindow();
						window.switch_window(window);
						frame.dispose();
			        }
				}
			}
		});
		btnSaveButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		btnSaveButton.setBounds(330, 261, 99, 27);
		frame.getContentPane().add(btnSaveButton);	
		
		JLabel lblCurrentState = new JLabel("current state");
		lblCurrentState.setFont(new Font("微軟正黑體 Light", Font.BOLD, 20));
		lblCurrentState.setBounds(14, 130, 159, 40);
		frame.getContentPane().add(lblCurrentState);
		
		try {	//connect server to take maximum version 
			VersionControlDB dbconnect = new VersionControlDB();
			dbconnect.connectDB();
			Statement stmt = dbconnect.connect.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `specification_name` WHERE `user` = '"+LoginWindow.account+"'");		
			while(rs.next()) {
				projectComboBox.addItem(rs.getString("specification_name"));
			}			

			ResultSet rs2 = stmt.executeQuery("SELECT `specification_version` FROM `specification_version` WHERE `specification_name` = '"+projectComboBox.getItemAt(0)+"' AND `user` = '"+LoginWindow.account+"'");		
			versionComboBox.removeAllItems();
			while(rs2.next()) {
				versionComboBox.addItem(rs2.getString("specification_version"));
			}	
			
			rs.close();	rs = null;
			rs2.close(); rs2 = null;
			stmt.close(); stmt = null;
			dbconnect.closeDB();
			System.out.println("connect close success!");
		} catch(Exception e) {
			System.out.println("connect wrong!");
			System.out.println(e);
		}
		
		
		
		
		this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//關閉事件->都不做事(不關閉)
		frame.addWindowListener(new WindowAdapter() 
		{
		    public void windowClosing(WindowEvent event) {
		    	int result=JOptionPane.showConfirmDialog(frame,
		                   "Are you sure you want to leave the newSpec window?",
		                   "Exit",
		                   JOptionPane.YES_NO_OPTION,
		                   JOptionPane.WARNING_MESSAGE);
		        if (result==JOptionPane.YES_OPTION) {frame.dispose();}	//選擇YES才關閉frame
		    
		}});
	}
}
