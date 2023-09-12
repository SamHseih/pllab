package version_control;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SetUpWindow {

	private JFrame frame;
	private JTextField account_textField;
	private JPasswordField passwordField;
	private JButton SetUpButton;
	private JButton backButton;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SetUpWindow window = new SetUpWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void switch_window(SetUpWindow window) {
		window.frame.setVisible(true);
	}
	
	/**
	 * Create the application.
	 */
	public SetUpWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 314);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("set up");
		frame.getContentPane().setLayout(null);
		
		
		JLabel account_label = new JLabel("Email or Username:");
		account_label.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		account_label.setBounds(71, 33, 195, 29);
		frame.getContentPane().add(account_label);
		
		JLabel password_label = new JLabel("Password:");
		password_label.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		password_label.setBounds(71, 118, 195, 29);
		frame.getContentPane().add(password_label);
		
		account_textField = new JTextField();
		account_textField.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		account_textField.setBounds(71, 75, 296, 30);
		frame.getContentPane().add(account_textField);
		account_textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		passwordField.setBounds(71, 160, 296, 30);
		frame.getContentPane().add(passwordField);
		
		SetUpButton = new JButton("Set Up");
		SetUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					VersionControlDB dbconnect = new VersionControlDB();
					dbconnect.connectDB();
					Statement stmt = dbconnect.connect.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT `account` FROM `account` WHERE `account` = '"+account_textField.getText()+"'");
					
					if(!rs.next()) { //帳號沒有重複
						int result=JOptionPane.showConfirmDialog(frame,
				                   "Are you sure you want to set up this account?\n"+
				                   "accoint: "+account_textField.getText()+"\n"+
				                   "password: "+passwordField.getText()+"\n",
				                   "set up",
				                   JOptionPane.YES_NO_OPTION,
				                   JOptionPane.WARNING_MESSAGE);
						if (result==JOptionPane.YES_OPTION) {
							//加密 密碼
							SecretKeySpec key = PasswordEncryption.createSecretKey(passwordField.getText().toCharArray());
							String encryptedPassword = PasswordEncryption.encrypt(passwordField.getText(), key);
							
							Statement stmt2 = dbconnect.connect.createStatement();
			    			stmt2.executeUpdate("INSERT INTO `account`(`account`, `password`) VALUES"
			    					             + " ('"+account_textField.getText()+"','"+encryptedPassword+"')");
			    			
							stmt2.close(); stmt2 = null;
							rs.close();	rs = null;
							stmt.close(); stmt = null;
							dbconnect.closeDB();
							
							JOptionPane.showMessageDialog(null,"Set up success!","success", JOptionPane.CLOSED_OPTION);
							LoginWindow window = new LoginWindow();
							window.switch_window(window);
							frame.dispose();
						}			
					}
					else {  //帳號重複
						rs.close();	rs = null;
						stmt.close(); stmt = null;
						dbconnect.closeDB();
						JOptionPane.showMessageDialog(null, "The account is already in use!", "error", 
								JOptionPane.ERROR_MESSAGE);
					}					
				} catch (Exception ex) {
					System.out.println("fail:" + e);
				}							
			}
		});
		SetUpButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		SetUpButton.setBounds(71, 217, 111, 37);
		frame.getContentPane().add(SetUpButton);
		
		backButton = new JButton("back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginWindow window = new LoginWindow();
				window.switch_window(window);
				frame.dispose();
			}
		});
		backButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		backButton.setBounds(256, 217, 111, 37);
		frame.getContentPane().add(backButton);
			
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
}
