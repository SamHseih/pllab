package version_control;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;

public class NewSpecWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewSpecWindow window = new NewSpecWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void switch_window(NewSpecWindow window) {
		window.frame.setVisible(true);
	}
	
	/**
	 * Create the application.
	 */
	public NewSpecWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("new project");
		frame.setBounds(100, 100,450, 266);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextPane textPane = new JTextPane();
		textPane.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		textPane.setBounds(128, 83, 268, 36);
		frame.getContentPane().add(textPane);
		
		JLabel lblNewLabel = new JLabel("project name: ");
		lblNewLabel.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		lblNewLabel.setBounds(14, 83, 110, 36);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartWindow window = new StartWindow();
				window.switch_window(window);
				frame.dispose();
			}
		});
		btnNewButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		btnNewButton.setBounds(319, 179, 99, 27);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("set up");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					VersionControlDB dbconnect = new VersionControlDB();
					dbconnect.connectDB();
					Statement stmt = dbconnect.connect.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT `specification_name` FROM `specification_name` WHERE `user` = '"+LoginWindow.account+"'");
					boolean CheckSameProjectName = false;
					while(rs.next()) {
						if(rs.getString("specification_name").toLowerCase().equalsIgnoreCase(textPane.getText())) {
							CheckSameProjectName = true;
							break;
						}
					}
					if(CheckSameProjectName == true) {
						JOptionPane.showMessageDialog(null, "Project name is already exist!", "name error", 
								JOptionPane.ERROR_MESSAGE);
					}
					else {
						int res=JOptionPane
								.showConfirmDialog(null, "You want to new project name\n"+textPane.getText(), "check", JOptionPane.YES_NO_OPTION);
							if(res==JOptionPane.YES_OPTION){
								Date dNow = new Date( );
							      SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

								Statement stmt2 = dbconnect.connect.createStatement();
								stmt2.executeUpdate("INSERT INTO `specification_name`(`specification_name`, `user`) VALUES ('"+textPane.getText()+"','"+LoginWindow.account+"')");
							    stmt2.executeUpdate("INSERT INTO `specification_version`(`specification_name`, `specification_version`, `create_time`, `javacode_state`, `user`) VALUES ('"+textPane.getText()+"','0','"+ft.format(dNow)+"','Version 0 can not implemented!','"+LoginWindow.account+"')");
								
								rs.close();	rs = null;
								stmt.close(); stmt = null;
								stmt2.close(); stmt2 = null;
								dbconnect.closeDB();
								System.out.println("connect close success!");
								
								JOptionPane.showMessageDialog(null, "set up success!", "success", 
										JOptionPane.INFORMATION_MESSAGE);
								StartWindow window = new StartWindow();
								window.switch_window(window);
								frame.dispose();
							}
					}
				} catch(Exception ex) {
					System.out.println("connect wrong!");
					System.out.println(e);
				}
			}
		});
		btnNewButton_1.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		btnNewButton_1.setBounds(204, 179, 99, 27);
		frame.getContentPane().add(btnNewButton_1);
		
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
