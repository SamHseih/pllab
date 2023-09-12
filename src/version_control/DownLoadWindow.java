package version_control;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import javax.swing.JTree;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPanel;

public class DownLoadWindow {

	public JFrame frame;
	private JTextField textFieldDownloadPath;
	private Boolean firsttimenoaction = true; // i don't know why use this boolean, but i don't want delete
	DefaultMutableTreeNode node = null;
	String program_name_file_now = "";
	String program_path = "";
	String program_name_now = "";
	static String[] testCaseColumn = {"No.","OBJ_PRE","ARG_PRE","OBJ_POST","ARG_POST","RETVAL","EXCEPTION"};
	static Object[][] testcasedata = {{"","","","","","",""}};
	private static JTable testCaseTable;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DownLoadWindow window = new DownLoadWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void switch_window(DownLoadWindow window) {
		window.frame.setVisible(true);
	}
	
	/**
	 * Create the application.
	 */
	public DownLoadWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("download version");
		frame.setBounds(0, 0, 1910, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		
		this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//關閉事件->都不做事(不關閉)
		frame.getContentPane().setLayout(null);
		
		JButton backButton = new JButton("back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartWindow window = new StartWindow();
				window.switch_window(window);
				frame.dispose();
			}
		});
		backButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		backButton.setBounds(14, 988, 225, 32);
		frame.getContentPane().add(backButton);
		
		JButton btnBrowse = new JButton("browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser=new JFileChooser(new File("D:\\"));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if( fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
				{
					File selectedFile=fileChooser.getSelectedFile();
					textFieldDownloadPath.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		btnBrowse.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		btnBrowse.setBounds(78, 886, 97, 25);
		frame.getContentPane().add(btnBrowse);
			
		JTree Projecttree = new JTree();
		Projecttree.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		Projecttree.setModel(null);
		Projecttree.setBounds(14, 103, 209, 208);
		frame.getContentPane().add(Projecttree);
		
		JScrollPane programTreescrollPane = new JScrollPane(Projecttree);
		programTreescrollPane.setBounds(14, 182, 209, 208);
		frame.getContentPane().add(programTreescrollPane);
		
		JLabel lblVersion = new JLabel("version");
		lblVersion.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblVersion.setBounds(14, 92, 172, 32);
		frame.getContentPane().add(lblVersion);
		
		JComboBox<String> VersionComboBox = new JComboBox<String>();
		VersionComboBox.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		VersionComboBox.setBounds(14, 137, 209, 32);
		frame.getContentPane().add(VersionComboBox);
		
		JLabel lblProject = new JLabel("Project");
		lblProject.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblProject.setBounds(14, 13, 172, 32);
		frame.getContentPane().add(lblProject);
		
		JLabel lblProgramVersion = new JLabel("version:");
		lblProgramVersion.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblProgramVersion.setBounds(257, 47, 387, 32);
		frame.getContentPane().add(lblProgramVersion);
		
		JLabel lblProgramName = new JLabel("file name: ");
		lblProgramName.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblProgramName.setBounds(658, 47, 245, 32);
		frame.getContentPane().add(lblProgramName);
		
		JScrollPane projectScrollPane = new JScrollPane();
		projectScrollPane.setBounds(253, 92, 650, 928);
		frame.getContentPane().add(projectScrollPane);
		
		JPanel panel = new JPanel();
		projectScrollPane.setViewportView(panel);
		
		JTextPane projectTextPane = new JTextPane();
		panel.add(projectTextPane);
		projectTextPane.setEditable(false);
		projectTextPane.setFont(new Font("Arial", Font.BOLD, 18));
		
		JPanel panel_1 = new JPanel();
		projectScrollPane.setRowHeaderView(panel_1);
		
		JTextPane projectLineTextPane = new JTextPane();
		projectLineTextPane.setEditable(false);
		projectLineTextPane.setFont(new Font("Arial", Font.BOLD, 18));
		panel_1.add(projectLineTextPane);
		//projectTextPane.setBounds(1, 75, 648, 852);		
		//frame.getContentPane().add(projectTextPane);
		
	/*	JTextPane projectLineTextPane = new JTextPane();
		projectLineTextPane.setEditable(false);
		projectLineTextPane.setFont(new Font("Arial", Font.BOLD, 18));
		//projectTextPane.setBounds(1, 75, 648, 852);		
		frame.getContentPane().add(projectLineTextPane);
		
		JScrollPane line1scrollPane = new JScrollPane(projectLineTextPane);
		line1scrollPane.setBounds(257, 92, 36, 928);
		frame.getContentPane().add(line1scrollPane);*/
		
		JLabel lblDownloadpath = new JLabel("download path:");
		lblDownloadpath.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblDownloadpath.setBounds(14, 803, 225, 32);
		frame.getContentPane().add(lblDownloadpath);
		
		textFieldDownloadPath = new JTextField();
		textFieldDownloadPath.setEditable(false);
		textFieldDownloadPath.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		textFieldDownloadPath.setBounds(14, 848, 225, 25);
		frame.getContentPane().add(textFieldDownloadPath);
		textFieldDownloadPath.setColumns(10);
		
		JComboBox<String> ProjectComboBox = new JComboBox<String>();
		ProjectComboBox.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		ProjectComboBox.setBounds(14, 58, 209, 32);
		frame.getContentPane().add(ProjectComboBox);
		
		JTree testCaseTree = new JTree();
		testCaseTree.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		testCaseTree.setModel(null);
		testCaseTree.setBounds(14, 414, 209, 208);
		frame.getContentPane().add(testCaseTree);
		
		JScrollPane testCaseTreescrollPane = new JScrollPane(testCaseTree);
		testCaseTreescrollPane.setBounds(14, 414, 209, 208);
		frame.getContentPane().add(testCaseTreescrollPane);
		
		JLabel testcaselabel = new JLabel("file name: ");
		testcaselabel.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		testcaselabel.setBounds(952, 47, 328, 32);
		frame.getContentPane().add(testcaselabel);
		
		//build_testcaseTable(testCaseTable,);
	    testCaseTable = new JTable(testcasedata, testCaseColumn);
		testCaseTable.setColumnSelectionAllowed(true);
		testCaseTable.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		testCaseTable.setBounds(952, 641, 643, 379);
		testCaseTable.setRowHeight(40);
		testCaseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JButton downloadButton = new JButton("download version");
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textFieldDownloadPath.getText().equals("") ) {
					JOptionPane.showMessageDialog(null, "please choose the path you want to download!", "error", 
							JOptionPane.ERROR_MESSAGE);
				}
				else if(VersionComboBox.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "please don't choose the 0 version!", "error", 
							JOptionPane.ERROR_MESSAGE);
				}
				else {
					int result=JOptionPane.showConfirmDialog(frame,
			                   "Are you sure you want to download this project?\n"+
			                   "project: "+ProjectComboBox.getItemAt(ProjectComboBox.getSelectedIndex()).toString()+"\n"+
			                   "version: "+VersionComboBox.getSelectedIndex()+"\n",
			                   "download",
			                   JOptionPane.YES_NO_OPTION,
			                   JOptionPane.WARNING_MESSAGE);
			        if (result==JOptionPane.YES_OPTION) { //save to database
			        	//download
			        	
			        	FileHandleToServer.DownloadFile(ProjectComboBox.getItemAt(ProjectComboBox.getSelectedIndex()).toString(),
			        						            "version " + VersionComboBox.getSelectedIndex(),
			        						            textFieldDownloadPath.getText());
			        	//success dialog
			        	JOptionPane.showMessageDialog(null,"download is success!","success", JOptionPane.CLOSED_OPTION);
		        		StartWindow window = new StartWindow();
						window.switch_window(window);
						frame.dispose();
			        }
				}
			}
		});
		downloadButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		downloadButton.setBounds(14, 943, 225, 32);
		frame.getContentPane().add(downloadButton);
		
		JScrollPane testCaseTablescrollPane = new JScrollPane(testCaseTable);
		testCaseTablescrollPane.setBounds(948, 92, 930, 928);
		frame.getContentPane().add(testCaseTablescrollPane);
		
		try {	//take comboBox content and jtree
			VersionControlDB dbconnect = new VersionControlDB();
			dbconnect.connectDB();
			Statement stmt = dbconnect.connect.createStatement();
			Statement stmt2 = dbconnect.connect.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `specification_name`");
			rs.next();
			build_tree(Projecttree,rs.getString("specification_name"));			
			
			//build_testcaseTree(testCaseTree,rs.getString("specification_name"),0);
			
			ProjectComboBox.addItem(rs.getString("specification_name"));
			ResultSet rs2 = stmt2.executeQuery("SELECT * FROM `specification_version` WHERE `specification_name` = '"+rs.getString("specification_name")+"' AND `user` = '"+LoginWindow.account+"'");
			while(rs2.next()) {
				VersionComboBox.addItem("version " + rs2.getString("specification_version"));
			}
			while(rs.next()) {
				ProjectComboBox.addItem(rs.getString("specification_name"));
			}
			//take fist list version
			rs.close();	rs = null;
			rs2.close();	rs2 = null;
			stmt.close(); stmt = null;
			stmt2.close(); stmt2 = null;
			dbconnect.closeDB();
			System.out.println("connect close success!");
		} catch(Exception ex) {
			System.out.println(ex);
		}
		
		frame.addWindowListener(new WindowAdapter() 
		{
		    public void windowClosing(WindowEvent event) {
		    	int result=JOptionPane.showConfirmDialog(frame,
		                   "Are you sure you want to leave the save window?",
		                   "Exit",
		                   JOptionPane.YES_NO_OPTION,
		                   JOptionPane.WARNING_MESSAGE);
		        if (result==JOptionPane.YES_OPTION) {frame.dispose();}	//選擇YES才關閉frame
		    
		}});
		
		ProjectComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                	try {
						projectTextPane.setText("");
						build_testcaseTable(testCaseTable,"");
						testCaseTree.setModel(null);
						VersionComboBox.removeAllItems();
						VersionControlDB dbconnect = new VersionControlDB();
						dbconnect.connectDB();
						Statement stmt = dbconnect.connect.createStatement();
    					ResultSet rs = stmt.executeQuery("SELECT `specification_version`, `create_time` FROM `specification_version` WHERE `specification_name` = '"+ProjectComboBox.getSelectedItem().toString()+"' AND `user` = '"+LoginWindow.account+"'");
    					build_tree(Projecttree,ProjectComboBox.getSelectedItem().toString());
    					if(firsttimenoaction) {
    						while(rs.next()) {
    							VersionComboBox.addItem("version " + rs.getString("specification_version"));
    						}
    					}
    					firsttimenoaction = true;
    					rs.close();	rs = null;
    					stmt.close(); stmt = null;
    					dbconnect.closeDB();
    					System.out.println("connect close success!");
    				} catch(Exception ex) {
    					System.out.println("connect fail:" + ex);
    				}
                }
            }
		});
		
		VersionComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                	try {
                		build_testcaseTable(testCaseTable,"");
                		testcaselabel.setText("file name: ");
                		testCaseTree.setModel(null);
                		build_testcaseTree(testCaseTree,ProjectComboBox.getSelectedItem().toString(),VersionComboBox.getSelectedItem().toString());
    					
    				} catch(Exception ex) {
    					System.out.println("connect fail:" + ex);
    				}
                }
            }
		});
		
		//test listen for JTree
		Projecttree.addTreeSelectionListener(new TreeSelectionListener() {
				      public void valueChanged(TreeSelectionEvent e) {
				        node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				        if(node.toString().contains(".")) { //不是選到root
				        	projectTextPane.setText("");
				        	projectLineTextPane.setText("");
				        	lblProgramName.setText("file name: ");
				        	lblProgramVersion.setText("version: ");
				        	try {
						        if(!(VersionComboBox.getSelectedItem().toString()).equals("version 0")) {
						        	//example-> path: grade//version 1 name: grade.ocl
						        	try {
						        		program_name_file_now = node.toString();
						        		program_name_now = node.getParent().toString();
						        		program_path = program_name_now+"//"+VersionComboBox.getSelectedItem().toString();
						        		String filestring = FileHandleToServer.readFile(program_path, program_name_file_now);
						        		projectTextPane.setText(filestring.toString());
						        		TextCompare.addtextlinenumber(projectLineTextPane,projectTextPane);
						        		VersionControlDB dbconnect = new VersionControlDB();
										dbconnect.connectDB();
										Statement stmt = dbconnect.connect.createStatement();  
										String[] version = (VersionComboBox.getSelectedItem().toString()).split(" ");
					        			ResultSet rs = stmt.executeQuery("SELECT `create_time` FROM `specification_version` WHERE `specification_name`='"+ProjectComboBox.getSelectedItem().toString()+"' AND `specification_version`='"+version[1]+"' AND `user` = '"+LoginWindow.account+"'");
					        			rs.next();
					        			lblProgramName.setText("file name: "+program_name_file_now);
					        			lblProgramVersion.setText(VersionComboBox.getSelectedItem().toString()+" ("+rs.getString("create_time")+")");
					        			rs.close();	rs = null;
					        			stmt.close(); stmt = null;
					        			dbconnect.closeDB();
						        	} catch(Exception ex) {
						        		System.out.println(ex);
						        	  }
						        }
				        	} catch(Exception ex) {
				        		System.out.println(ex);
				        	}
				        }
				      }
				    });
		
		testCaseTree.addTreeSelectionListener(new TreeSelectionListener() {
		      public void valueChanged(TreeSelectionEvent e) {
		    	   node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
		    	   build_testcaseTable(testCaseTable,"");
		    	   testcaselabel.setText("file name: ");
			       if(node.toString().contains(".")) { //不是選到root
			    	   try {
			    		   program_path = ProjectComboBox.getSelectedItem().toString()+"\\"+VersionComboBox.getSelectedItem().toString()+"\\test data\\";
			    		   build_testcaseTable(testCaseTable,FileHandleToServer.readFile(program_path, node.toString()));
				    	   testcaselabel.setText("file name: "+node.toString()); 
			    	   } catch (IOException e1) {
			    		   // TODO Auto-generated catch block
			    		   e1.printStackTrace();
			    	   }
			       }
		      }
		});
	}
	
	public static void build_tree(JTree t, String project_name) {		
		DefaultMutableTreeNode node_project = new DefaultMutableTreeNode(project_name);
		DefaultMutableTreeNode node_OCL = new DefaultMutableTreeNode(project_name+".ocl");
		node_project.add(node_OCL);
		DefaultMutableTreeNode node_UML = new DefaultMutableTreeNode(project_name+".uml");
		node_project.add(node_UML);
		DefaultMutableTreeNode node_JavaScript = new DefaultMutableTreeNode(project_name+".java");
		node_project.add(node_JavaScript);	
	//	DefaultMutableTreeNode node_ast2uml = new DefaultMutableTreeNode(project_name+"AST2UML.uml");
	//	node_project.add(node_ast2uml);	
		
		DefaultTreeModel model = new DefaultTreeModel(node_project);
		t.setModel(model);
	}
	
	public static void build_testcaseTree(JTree t, String projectname, String version) {
		try {
			if(version.contains("version 0")) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode("test case");
				DefaultTreeModel fileListTreeModel = new DefaultTreeModel(node);
				t.setModel(fileListTreeModel);
			}
			else {
				String[] fileList = FileHandleToServer.getFileList(projectname+"\\"+version +"\\test data\\");
			
				DefaultMutableTreeNode node = new DefaultMutableTreeNode("test case");
				for(int index = 0; index < fileList.length; index++) 
					node.add(new DefaultMutableTreeNode(fileList[index]));
			
				DefaultTreeModel fileListTreeModel = new DefaultTreeModel(node);
				t.setModel(fileListTreeModel);

				build_testcaseTable(testCaseTable,"");
			}
		} catch(Exception ex) {
			System.out.println("no version exist!");
		}
	}
	
	public static void build_testcaseTable(JTable table, String testcase) {
		DefaultTableModel model;
		if(!testcase.equals("")) {
			try {			
				String[] testcaseContent = testcase.split("\n");
				String[][] data = new String[testcaseContent.length][7];
			//	TableColumnModel columnModel = table.getColumnModel();
				//column寬調整到data都看的到
			//	int[]columnMaxWidth = new int[7];
			//	for(int index = 0; index < 7; index++) columnMaxWidth[index] = 0;
				for(int index = 0; index < testcaseContent.length; index++) {
					for(int j = 0; j < 7; j++) {
						data[index][0] = String.valueOf(index+1);
						String[] testcasesplite = testcaseContent[index].split("=");
						data[index][1] = testcasesplite[1].substring(0, testcasesplite[1].length()-10);  //ex:"[1, 1, 1], ARG_PRE "
						data[index][2] = testcasesplite[2].substring(0, testcasesplite[2].length()-10); //ex:" [1, 1, 1],OBJ_POST "
						data[index][3] = testcasesplite[3].substring(0, testcasesplite[3].length()-10); //ex:" [1, 1, 1],ARG_POST "
						data[index][4] = testcasesplite[4].substring(0, testcasesplite[4].length()-9);//ex:" [1, 1, 1], RETVAL "
						data[index][5] = testcasesplite[5].substring(0, testcasesplite[5].length()-12); //ex:" [], EXCEPTION "
						data[index][6] = testcasesplite[6]; //ex:"[]"
				/*		for(int k = 1; k <= 6; k++) {
							if(data[index][k].length() > columnMaxWidth[k]) columnMaxWidth[k] = data[index][k].length();
						}*/
					}
				}
			/*	for(int index = 1; index < 7; index++) { //column: "No." 不用調整
					columnModel.getColumn(index).setPreferredWidth(columnMaxWidth[index]*100);
				}*/
				model = new DefaultTableModel(data , testCaseColumn);
			} catch(Exception ex) {
				System.out.println("ex");
				model = new DefaultTableModel(testcasedata , testCaseColumn);
			}
		}
		else {
			model = new DefaultTableModel(testcasedata , testCaseColumn);
		}
		table.setModel(model);
	}
}
