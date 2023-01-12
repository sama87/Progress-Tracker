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
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

public class ProgressTracker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner in = new Scanner(System.in);
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
		System.out.println("Do you have an existing account with JUMP Tv?: ");
		String ans = in.next().toLowerCase();
		
		if (ans.equals("yes")) {
			
			if(login(conn, prepStatement, result, user, in)) {
				
				in.nextLine();
				boolean running = true;
				
				while(running) {
					System.out.println("*****************");
					System.out.println("JUMP Tv Watchlist");
					System.out.println("*****************");
					System.out.println();
					

					//Display shows being watched
					
					String menuInput = "0";
					
					System.out.println("1) Update show progress");
					System.out.println("2) Add a new show to watchlist");
					System.out.println("3) Remove a show from watchlist");
					System.out.println("4) Exit Program");
					System.out.println();
					System.out.println("Enter the number of your selection: ");
					menuInput = in.nextLine();
					switch(menuInput) {
						case("1"):
							updateProgress(in);
							break;
						case("2"):
							addShow(in);
							break;
						case("3"):
							dropShow(in);
							break;
						case("4"):
							running = false;
							break;
						default:
							System.out.println();
							System.out.println("Invalid input! Please enter an option from the menu!");
							System.out.println();
					}//switch

					System.out.println();
					
				}//while
				
				System.out.println("Thank you for using Jump Tv Watchlist!");
				in.close();
			}//main
			
		} else {
			
			createUser(conn, statement, prepStatement, result, user, in);
		}
	}
	
	
	public static boolean createUser(Connection conn, Statement statement, PreparedStatement prepStatement, ResultSet result, User user, Scanner in) {
		
		boolean login = false;
		
		String username = "";
		String password = "";
		
		System.out.println("Please Enter a Username: ");
		username = in.next();
		System.out.println("Enter a Password: ");
		password = in.next();
		
		in.close();
		
		user = new User(username, password);
		
		String query = "insert into users values('" + user.getUsername() + "', '"+ user.getPassword() + "');";
		System.out.println(query);
		
		try {
			
			statement = conn.createStatement();
			int row = statement.executeUpdate(query);
			System.out.println(row + " row/s was affected");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			login = false;
			e.printStackTrace();
		}		
			
		return login;
	}
	
	public static boolean login(Connection conn, PreparedStatement prepStatement, ResultSet result, User user, Scanner in) {
		
		System.out.println("================ Login ===============");
		System.out.println("username: ");
		String logUser = in.next();
		System.out.println("password: ");
		String logPass = in.next();
		
		try {
			
			
			prepStatement = conn.prepareStatement("select * from users where userName=? and userPassword=?");
			prepStatement.setString(1, logUser);
			prepStatement.setString(2, logPass);
			
			result = prepStatement.executeQuery();
			
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			System.out.println("Wrong Username/Password ============== (T.T)");
			e.printStackTrace();
			return false;
		}
	}
		
	public static void updateProgress(Scanner readIn) {
		int id;
		String yesNo = "";
		boolean exit = false;
		//Display tracked shows
		System.out.println("Enter the id for the show you wish to update: ");
		while(true) {
			try{
				id = readIn.nextInt();
				readIn.nextLine();
				//Check tracked shows for valid id
				break;
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Must be a number!");
				readIn.nextLine();
			}
		}//while
		
		//Update the episodes watched in DB
		
		
		while(exit == false) {
			System.out.println("Would you like to update another show? (Y/N)");
			yesNo = readIn.nextLine();
			yesNo = yesNo.toUpperCase();
			if (yesNo.charAt(0) == 'Y') {
				updateProgress(readIn);
				break;
			}
			else if (yesNo.charAt(0) == 'N') exit = true;
			
			else {
				System.out.println();
				System.err.println("Invalid input! Please enter Y for yes or N for no!");
				System.out.println();
			}

		}//while
		
	}//updateProgress
		
		
		
	public static void addShow(Scanner readIn) {
		int id;
		String yesNo = "";
		boolean exit = false;
		//Display shows that arent being tracked
		System.out.println("Enter the id for the show you wish to add to your watch list: ");
		while(true) {
			try{
				id = readIn.nextInt();
				
				//Check shows for valid id
				break;
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Must be a number!");
				readIn.nextLine();
			}
		}//while
		
		readIn.nextLine();
		
		//add the new show to users watchlist
		
		System.out.println();
		while(exit == false) {
			System.out.println("Would you like to add another show? (Y/N)");
			yesNo = readIn.nextLine();
			yesNo = yesNo.toUpperCase();
			if (yesNo.charAt(0) == 'Y') {
				addShow(readIn);
				break;
			}
			else if (yesNo.charAt(0) == 'N') exit = true;
			
			else {
				System.out.println();
				System.err.println("Invalid input! Please enter Y for yes or N for no!");
				System.out.println();
			}
			
		}//while
		
	}//addShow
	
	public static void dropShow(Scanner readIn) {
		int id;
		String yesNo = "";
		boolean exit = false;
		//Display shows that are being tracked
		System.out.println("Enter the id for the show you wish to remove from your watch list: ");
		while(true) {
			try{
				id = readIn.nextInt();
				
				//Check shows for valid id
				break;
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Must be a number!");
				readIn.nextLine();
			}
		}//while
		
		readIn.nextLine();
		
		//add the new show to users watchlist
		
		System.out.println();
		while(exit == false) {
			System.out.println("Would you like to remove another show? (Y/N)");
			yesNo = readIn.nextLine();
			yesNo = yesNo.toUpperCase();
			if (yesNo.charAt(0) == 'Y') {
				dropShow(readIn);
				break;
			}
			else if (yesNo.charAt(0) == 'N') exit = true;
			
			else {
				System.out.println();
				System.err.println("Invalid input! Please enter Y for yes or N for no!");
				System.out.println();
			}
			
		}//while
		
	}//addShow

}
