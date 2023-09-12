package version_control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VersionControlDB {
	private String db_database;
	private String db_user;
	private String db_password;
	public Connection connect;
	
	public VersionControlDB() {
		db_database="version_control";
		db_user="wayger";
		db_password="fupjo3";
	}
	
	public void connectDB() {
		//³sµ²jdbc driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			connect= DriverManager.getConnection("jdbc:mysql://140.123.102.101/"+db_database+"?user="+db_user+"&password="+db_password+"&useUnicode=true&characterEncoding=UTF-8");
		  //connect= DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db_database+"?user="+db_user+"&password="+db_password+"&useUnicode=true&characterEncoding=UTF-8");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("connect success!");
	}
	
	public void closeDB() {	
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
		System.out.println("connect close success!");
	}
	
	public void doNewSpecSQL() {
		
	}
	
	public void doSaveJavaCodeSQ() {
		
	}
}
