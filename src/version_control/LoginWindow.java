package version_control;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginWindow {

	public JFrame frame;
	private JTextField account_textField;
	private JPasswordField passwordField;
	public static String account = "";
	public static String password = "";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow window = new LoginWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void switch_window(LoginWindow window) {
		window.frame.setVisible(true);
	}
	
	
	/**
	 * Create the application.
	 */
	public LoginWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("login");
		frame.setBounds(100, 100, 450, 347);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				account = account_textField.getText().toString();
				password = passwordField.getText().toString();
				
				//加密password---------------
		        try {
		        	VersionControlDB dbconnect = new VersionControlDB();
					dbconnect.connectDB();
					Statement stmt = dbconnect.connect.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM `account` WHERE `account` = '"+account+"'");
					
					if(rs.next()) { //帳號存在
						SecretKeySpec key = PasswordEncryption.createSecretKey(password.toCharArray());
						try {
							//解密 密碼錯誤代表解不出來
							PasswordEncryption.decrypt(rs.getString("password"), key);
							
							//解出來 密碼正確
							rs.close();	rs = null;
							stmt.close(); stmt = null;
							dbconnect.closeDB();
							StartWindow window = new StartWindow();
							window.switch_window(window);
							frame.dispose();	
							
						} catch(Exception ex) { //密碼錯誤
							rs.close();	rs = null;
							stmt.close(); stmt = null;
							dbconnect.closeDB();
							JOptionPane.showMessageDialog(null, "The password is wrong!", "error", 
									JOptionPane.ERROR_MESSAGE);
						}			
					}
					else { //帳號不存在
						rs.close();	rs = null;
						stmt.close(); stmt = null;
						dbconnect.closeDB();
						JOptionPane.showMessageDialog(null, "The account doesn't exist!", "error", 
								JOptionPane.ERROR_MESSAGE);
					}
		        } catch (Exception e) {
					// TODO Auto-generated catch block
		        	System.out.println("fail:" + e);
		        }			
			}
		});
		loginButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		loginButton.setBounds(241, 247, 126, 33);
		frame.getContentPane().add(loginButton);
		
		JButton signUpButton = new JButton("Sign up");
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SetUpWindow window = new SetUpWindow();
				window.switch_window(window);
				frame.dispose();
			}
		});
		signUpButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		signUpButton.setBounds(71, 247, 126, 33);
		frame.getContentPane().add(signUpButton);
		
		JLabel fogetPasswordNewLabel = new JLabel("Forget Password?");
		fogetPasswordNewLabel.setForeground(Color.GRAY);
		fogetPasswordNewLabel.setFont(new Font("微軟正黑體 Light", Font.BOLD, 15));
		fogetPasswordNewLabel.setBounds(225, 203, 142, 31);
		frame.getContentPane().add(fogetPasswordNewLabel);
		fogetPasswordNewLabel.addMouseMotionListener(new MouseMotionAdapter() {
	        //override the method
	        public void mouseDragged(MouseEvent arg0) {
	        	//....
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
}
