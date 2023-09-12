package version_control;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.tree.TreeNode;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class VersionCompareWindow {

	public JFrame frame;
	private JTextField textField;
	private JTextField textField_3;
	private JTextField textField_4;
	private JComboBox comboBox_1;
	private JComboBox comboBox_2;
	private String program_name_now = "";
	private String program_name_file_now = "";
	private String program_path_1 = "";
	private String program_path_2 = "";
	//第一次action載入comboBox會錯誤
	boolean firsttimenoaction = false;
	DefaultMutableTreeNode node = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VersionCompareWindow window = new VersionCompareWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void switch_window(VersionCompareWindow window) {
		window.frame.setVisible(true);
	}
	
	/**
	 * Create the application.
	 */
	public VersionCompareWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("compare version");
		frame.setBounds(0, 0, 1910, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StartWindow window = new StartWindow();
				window.switch_window(window);
				frame.dispose();
			}
		});
		btnNewButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		btnNewButton.setBounds(14, 991, 152, 29);
		frame.getContentPane().add(btnNewButton);
		
		JTree tree = new JTree();
		tree.setModel(null);
		tree.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		tree.setBounds(14, 30, 140, 227);
		frame.getContentPane().add(tree);
		
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setBounds(14, 30, 200, 199);
		frame.getContentPane().add(scrollPane);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(228, 70, 580, 928);
		frame.getContentPane().add(scrollPane_1);
		
		JPanel panel = new JPanel();
		scrollPane_1.setViewportView(panel);
		
		JTextPane txtrPackageTcgenContext = new JTextPane();
		txtrPackageTcgenContext.setEditable(false);
		panel.add(txtrPackageTcgenContext);
		txtrPackageTcgenContext.setForeground(Color.BLACK);
		txtrPackageTcgenContext.setFont(new Font("Arial", Font.BOLD, 18));
		txtrPackageTcgenContext.setBounds(0, 13, 10, 10);
		
		JPanel panel_2 = new JPanel();
		scrollPane_1.setRowHeaderView(panel_2);
		
		JTextPane textPane_3 = new JTextPane();
		textPane_3.setEditable(false);
		textPane_3.setFont(new Font("Arial", Font.BOLD, 18));
		panel_2.add(textPane_3);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(1298, 70, 580, 928);
		frame.getContentPane().add(scrollPane_2);
		
		JPanel panel_1 = new JPanel();
		scrollPane_2.setViewportView(panel_1);
		//frame.getContentPane().add(txtrPackageTcgenContext);
		JPanel panel_3 = new JPanel();
		scrollPane_2.setRowHeaderView(panel_3);
		
		JTextPane textPane_4 = new JTextPane();
		textPane_4.setEditable(false);
		textPane_4.setFont(new Font("Arial", Font.BOLD, 18));
		panel_3.add(textPane_4);
		
		
		JTextPane textArea = new JTextPane();
		textArea.setEditable(false);
		textArea.setFont(new Font("Arial", Font.BOLD, 18));
		textArea.setBounds(228, 70, 580, 928);		
		//frame.getContentPane().add(textArea);
		panel_1.add(textArea);
		
		JTextPane textPane_2 = new JTextPane();
		textPane_2.setEditable(false);
		textPane_2.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		textPane_2.setBounds(822, 70, 462, 513);
		//frame.getContentPane().add(textPane_2);
		
		
		JButton btnWordCompare = new JButton("String Comparison");
		btnWordCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
	        		txtrPackageTcgenContext.setText(FileHandleToServer.readFile(program_path_1,program_name_file_now));
	        		textArea.setText(FileHandleToServer.readFile(program_path_2,program_name_file_now));
					TextCompare.textcompare(txtrPackageTcgenContext,textArea,textPane_2);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		btnWordCompare.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		btnWordCompare.setBounds(0, 539, 228, 44);
		frame.getContentPane().add(btnWordCompare);
		
		JButton btnCompare = new JButton("Editing Comparison");
		btnCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					txtrPackageTcgenContext.setText(FileHandleToServer.readFile(program_path_1,program_name_file_now));
	        		textArea.setText(FileHandleToServer.readFile(program_path_2,program_name_file_now));
					TextCompare.newdeletecompare(txtrPackageTcgenContext,textArea,textPane_2);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnCompare.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		btnCompare.setBounds(0, 596, 228, 44);
		frame.getContentPane().add(btnCompare);
		
		JComboBox<String> comboBox_1 = new JComboBox<String>();
		comboBox_1.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		comboBox_1.setBounds(14, 351, 200, 30);
		frame.getContentPane().add(comboBox_1);
		
		JComboBox<String> comboBox_2 = new JComboBox<String>();
		comboBox_2.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		comboBox_2.setBounds(14, 427, 200, 30);
		frame.getContentPane().add(comboBox_2);
		
		JLabel lblNewLabel = new JLabel("Project");
		lblNewLabel.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblNewLabel.setBounds(14, 242, 200, 20);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("first version");
		lblNewLabel_1.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblNewLabel_1.setBounds(14, 318, 200, 20);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblSecondVersion = new JLabel("second version");
		lblSecondVersion.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblSecondVersion.setBounds(14, 394, 200, 20);
		frame.getContentPane().add(lblSecondVersion);
		
		JLabel lblNewLabel_2 = new JLabel("version:");
		lblNewLabel_2.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblNewLabel_2.setBounds(228, 32, 354, 25);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblFileName = new JLabel("file name: ");
		lblFileName.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblFileName.setBounds(596, 32, 212, 25);
		frame.getContentPane().add(lblFileName);
		
		JLabel lblVersion = new JLabel("version:");
		lblVersion.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblVersion.setBounds(1298, 32, 368, 25);
		frame.getContentPane().add(lblVersion);
		
		JLabel lblFileName_1 = new JLabel("file name: ");
		lblFileName_1.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblFileName_1.setBounds(1680, 32, 198, 25);
		frame.getContentPane().add(lblFileName_1);
		
		JLabel lblVersion_1 = new JLabel("description: version ");
		lblVersion_1.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblVersion_1.setBounds(955, 806, 200, 25);
		frame.getContentPane().add(lblVersion_1);
		
		JLabel lblDescriptionVersion = new JLabel("description: version ");
		lblDescriptionVersion.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblDescriptionVersion.setBounds(955, 596, 200, 25);
		frame.getContentPane().add(lblDescriptionVersion);
		
		JLabel lblDifferent = new JLabel("difference");
		lblDifferent.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblDifferent.setBounds(1013, 32, 142, 25);
		frame.getContentPane().add(lblDifferent);
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		textPane.setBounds(822, 634, 462, 159);
		frame.getContentPane().add(textPane);
		
		JTextPane textPane_1 = new JTextPane();
		textPane_1.setEditable(false);
		textPane_1.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		textPane_1.setBounds(822, 844, 462, 154);
		frame.getContentPane().add(textPane_1);
		
		JScrollPane scrollPane_3 = new JScrollPane(textPane);
		scrollPane_3.setBounds(822, 633, 462, 159);
		frame.getContentPane().add(scrollPane_3);
		
		
		JScrollPane scrollPane_4 = new JScrollPane(textPane_1);
		scrollPane_4.setBounds(822, 841, 462, 157);
		frame.getContentPane().add(scrollPane_4);
		
		
		JScrollPane scrollPane_5 = new JScrollPane(textPane_2);
		scrollPane_5.setBounds(822, 70, 462, 513);
		frame.getContentPane().add(scrollPane_5);
		
		JLabel lblOperation = new JLabel("category");
		lblOperation.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		lblOperation.setBounds(14, 489, 200, 37);
		frame.getContentPane().add(lblOperation);
		
		
		
	/*	JTextPane textPane_3 = new JTextPane();
		textPane_3.setEditable(false);
		textPane_3.setText("");
		textPane_3.setFont(new Font("Arial", Font.BOLD, 18));
		textPane_3.setBounds(228, 71, 29, 927);
		frame.getContentPane().add(textPane_3);*/
		
	/*	JTextPane textPane_4 = new JTextPane();
		textPane_4.setEditable(false);
		textPane_4.setFont(new Font("Arial", Font.BOLD, 18));
		textPane_4.setBounds(1298, 70, 30, 927);
		frame.getContentPane().add(textPane_4);*/
		
	/*	JScrollPane scrollPane_6 = new JScrollPane(textPane_4);
		scrollPane_6.setBounds(1300, 70, 30, 928);
		frame.getContentPane().add(scrollPane_6);
		scrollPane_6.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);*/
	/*	JScrollPane scrollPane_7 = new JScrollPane(textPane_3);
		scrollPane_7.setBounds(228, 70, 30, 928);
		frame.getContentPane().add(scrollPane_7);
		scrollPane_7.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);*/
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		comboBox.setBounds(14, 275, 200, 30);
		frame.getContentPane().add(comboBox);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                	try {
    					txtrPackageTcgenContext.setText("");  textArea.setText("");  textPane.setText("");
    					textPane_1.setText("");  textPane_2.setText("");
    					comboBox_1.removeAllItems(); comboBox_2.removeAllItems();
    					VersionControlDB dbconnect = new VersionControlDB();
    					dbconnect.connectDB();
    					Statement stmt = dbconnect.connect.createStatement();
    					ResultSet rs = stmt.executeQuery("SELECT `specification_version`, `create_time` FROM `specification_version` WHERE `specification_name` = '"+comboBox.getSelectedItem()+"' AND `user` = '"+LoginWindow.account+"'");
    					program_name_now = comboBox.getSelectedItem().toString();
    					build_tree(tree,comboBox.getSelectedItem().toString());
    					if(firsttimenoaction) {
    						while(rs.next()) {
    							comboBox_1.addItem("version " + rs.getString("specification_version"));
    							comboBox_2.addItem("version " + rs.getString("specification_version"));
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
	
		try {	//take comboBox content and jtree
			VersionControlDB dbconnect = new VersionControlDB();
			dbconnect.connectDB();
			Statement stmt = dbconnect.connect.createStatement();
			Statement stmt2 = dbconnect.connect.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `specification_name` WHERE `user` = '"+LoginWindow.account + "'");
			rs.next();
			build_tree(tree,rs.getString("specification_name"));			
					
			comboBox.addItem(rs.getString("specification_name"));
			ResultSet rs2 = stmt2.executeQuery("SELECT * FROM `specification_version` WHERE `specification_name` = '"+rs.getString("specification_name")+"' AND `user` = '"+LoginWindow.account+"'");
			while(rs2.next()) {
				comboBox_1.addItem("version " + rs2.getString("specification_version"));
				comboBox_2.addItem("version " + rs2.getString("specification_version"));
			}
			while(rs.next()) {
				comboBox.addItem(rs.getString("specification_name"));
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
		
		//test listen for JTree
		tree.addTreeSelectionListener(new TreeSelectionListener() {
		      public void valueChanged(TreeSelectionEvent e) {
		        node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
		        if(node.toString().contains(".")) { //不是選到root
		        	txtrPackageTcgenContext.setText("");
		        	textArea.setText("");
		        	lblDescriptionVersion.setText("description: version ");
		        	lblVersion_1.setText("description: version ");
		        	lblNewLabel_2.setText("version: ");
		        	lblFileName.setText("file name: ");
		        	lblVersion.setText("version: ");
		        	lblFileName_1.setText("file name: ");
		        	textPane_3.setText("");
		        	textPane_4.setText("");
		        	try {
				        if(!comboBox_1.getSelectedItem().toString().contains("0.0.0")) {
				        	//example-> path: grade//version 1 name: grade.ocl
				        	try {
				        		program_name_file_now = node.toString();
				        		program_path_1 = node.getParent().toString()+"//"+comboBox_1.getSelectedItem().toString();
				        		String filestring = FileHandleToServer.readFile(program_path_1, program_name_file_now);
				        		txtrPackageTcgenContext.setText(filestring.toString());
				        		TextCompare.addtextlinenumber(textPane_3,txtrPackageTcgenContext);
				        		VersionControlDB dbconnect = new VersionControlDB();
								dbconnect.connectDB();
								Statement stmt = dbconnect.connect.createStatement();  
								String[] version = (comboBox_1.getSelectedItem().toString()).split(" ");
			        			ResultSet rs = stmt.executeQuery("SELECT `description`,`create_time` FROM `specification_version` WHERE `specification_name`='"+comboBox.getSelectedItem().toString()+"' AND `specification_version`='"+version[1]+"' AND `user` = '"+LoginWindow.account+"'");
			        			rs.next();
			        			lblDescriptionVersion.setText("description: version "+comboBox_1.getSelectedIndex());
			        			lblNewLabel_2.setText("version: "+version[1]+" ("+rs.getString("create_time")+")");
					        	lblFileName.setText("file name: "+node.toString());
			        			textPane.setText(rs.getString("description"));
			        			rs.close();	rs = null;
			        			stmt.close(); stmt = null;
			        			dbconnect.closeDB();
				        	} catch(Exception ex) {
				        		System.out.println(ex);
				        	}
				        }
			        	
			        	if(!comboBox_2.getSelectedItem().toString().contains("0.0.0")) {
			        		try {
			        			program_name_file_now = node.toString();
				        		program_path_2 = node.getParent().toString()+"//"+comboBox_2.getSelectedItem().toString();
			        			String filestring = FileHandleToServer.readFile(program_path_2, program_name_file_now);
			        			textArea.setText(filestring.toString());
			        			TextCompare.addtextlinenumber(textPane_4,textArea);
			        			VersionControlDB dbconnect = new VersionControlDB();
								dbconnect.connectDB();
								Statement stmt = dbconnect.connect.createStatement();     
								String[] version = (comboBox_2.getSelectedItem().toString()).split(" ");
			        			ResultSet rs = stmt.executeQuery("SELECT `description`,`create_time` FROM `specification_version` WHERE `specification_name`='"+comboBox.getSelectedItem().toString()+"' AND `specification_version`='"+version[1]+"' AND `user` = '"+LoginWindow.account+"'");
			        			rs.next();
			        			lblVersion_1.setText("description: version "+comboBox_2.getSelectedIndex());
			        			lblVersion.setText("version: "+version[1]+" ("+rs.getString("create_time")+")");
			        			lblFileName_1.setText("file name: "+node.toString());
			        			textPane_1.setText(rs.getString("description"));
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
		
		this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//關閉事件->都不做事(不關閉)
		frame.addWindowListener(new WindowAdapter() 
		{
		    public void windowClosing(WindowEvent event) {
		    	int result=JOptionPane.showConfirmDialog(frame,
		                   "Are you sure you want to leave the version control window?",
		                   "Exit",
		                   JOptionPane.YES_NO_OPTION,
		                   JOptionPane.WARNING_MESSAGE);
		        if (result==JOptionPane.YES_OPTION) {frame.dispose();}	//選擇YES才關閉frame
		    
		}});
		
	}
	
	public static void build_tree(JTree t, String project_name) {		
		DefaultMutableTreeNode node_project = new DefaultMutableTreeNode(project_name);
		DefaultMutableTreeNode node_OCL = new DefaultMutableTreeNode(project_name+".ocl");
		node_project.add(node_OCL);
		DefaultMutableTreeNode node_UML = new DefaultMutableTreeNode(project_name+".uml");
		node_project.add(node_UML);
		DefaultMutableTreeNode node_JavaScript = new DefaultMutableTreeNode(project_name+".java");
		node_project.add(node_JavaScript);	
		DefaultMutableTreeNode node_TestScript = new DefaultMutableTreeNode(project_name+"Test.txt");
		node_project.add(node_TestScript);	
	/*	DefaultMutableTreeNode node_ast2uml = new DefaultMutableTreeNode(project_name+"AST2UML.uml");
		node_project.add(node_ast2uml);	*/
		
		DefaultTreeModel model = new DefaultTreeModel(node_project);
		t.setModel(model);
	}
}
