package Cognixia.jdbc.progresstracker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputFilter.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class ProgressTracker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Properties config = new Properties();
		Connection conn = null;
		final String URL;
		final String USERNAME;
		final String PASSWORD;
		User user = null;
		
		//Test Connection to cognixia database project
		
		try{
			
			config.load(new FileInputStream("resources/ProgressTracker.properties"));
			
			URL = config.getProperty("url");
			USERNAME = config.getProperty("username");
			PASSWORD = config.getProperty("password");
			
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
			System.out.println("Connected to the Database....");
			System.out.println("url: " + URL + " | username: " + USERNAME + " | password " + PASSWORD );
		} catch (FileNotFoundException e) {			
			// TODO Auto-generated catch block
			
			System.out.println("Cannot Read Properties... File not found");
			e.printStackTrace();
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//initiating statements and result set for querying
		Statement statement = null;
		PreparedStatement prepStatement = null;
		ResultSet result = null;
		
		
		//pass condition to login
		
		if(login(conn, prepStatement, user)) {
			
			
		} else {
			
			createUser(conn, statement, prepStatement, result, user);
		}
	}
	
	public static boolean createUser(Connection conn, Statement statement, PreparedStatement prepStatement, ResultSet result, User user) {
		Scanner in = new Scanner(System.in);
		boolean login = false;
		
		String username = "";
		String password = "";
		
		System.out.println("Please Enter a Username: ");
		username = in.next();
		System.out.println("Enter a Password: ");
		password = in.next();
		
		in.close();
		
		user = new User(username, password);
		
		String query = "create user '" + user.getUsername() + "'@'localhost' identified with mysql_native_password by '" + user.getPassword() + "'";
		String setPrivileges = "grant all privileges on progresstracker.* to '" + user.getUsername() + "'@'localhost';";
		System.out.println(setPrivileges);
		System.out.println(query);
		
		try {
			
			statement = conn.createStatement();
			int row = statement.executeUpdate(query);
			System.out.println(row + " row/s was affected");
			
			statement = conn.createStatement();
			int priv = statement.executeUpdate(setPrivileges);
			System.out.println(priv + " -----> You can now access shows from the list");
			login = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			login = false;
			e.printStackTrace();
		}		
			
		return login;
	}
	
	public static void displayUserData(Connection conn, PreparedStatement prepStatement, ResultSet result, User user) {
		
		String getUserData = "";
		
		try {
			
			prepStatement = conn.prepareStatement("prepStatement");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}

}
