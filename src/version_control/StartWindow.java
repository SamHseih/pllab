package version_control;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class StartWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartWindow window = new StartWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void switch_window(StartWindow window) {
		window.frame.setVisible(true);
	}
	/**
	 * Create the application.
	 */
	public StartWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("version control");
		frame.setBounds(100, 100, 339, 446);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("save version");
		btnNewButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					SaveWindow window = new SaveWindow();
					window.switch_window(window);
					frame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(54, 106, 213, 43);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnVersionCompare = new JButton("version compare");
		btnVersionCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VersionCompareWindow window = new VersionCompareWindow();
				window.switch_window(window);
				frame.dispose();
			}
		});
		btnVersionCompare.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		btnVersionCompare.setBounds(54, 218, 213, 43);
		frame.getContentPane().add(btnVersionCompare);
		
		JButton btnNewSpec = new JButton("new specification");
		btnNewSpec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewSpecWindow window = new NewSpecWindow();
				window.switch_window(window);
				frame.dispose();
			}
		});
		btnNewSpec.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		btnNewSpec.setBounds(54, 50, 213, 43);
		frame.getContentPane().add(btnNewSpec);
		
		JButton btnSaveJavaCode = new JButton("save java code");
		btnSaveJavaCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SaveJavaCodeWindow window = new SaveJavaCodeWindow();
				window.switch_window(window);
				frame.dispose();
			}
		});
		btnSaveJavaCode.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		btnSaveJavaCode.setBounds(54, 274, 213, 43);
		frame.getContentPane().add(btnSaveJavaCode);
		
		JButton backToLogin = new JButton("back to login");
		backToLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginWindow window = new LoginWindow();
				window.switch_window(window);
				frame.dispose();
			}
		});
		backToLogin.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		backToLogin.setBounds(54, 330, 213, 43);
		frame.getContentPane().add(backToLogin);
		
		JButton downloadButton = new JButton("download version");
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DownLoadWindow window = new DownLoadWindow();
				window.switch_window(window);
				frame.dispose();
			}
		});
		downloadButton.setFont(new Font("微軟正黑體 Light", Font.BOLD, 18));
		downloadButton.setBounds(54, 162, 213, 43);
		frame.getContentPane().add(downloadButton);
		
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
