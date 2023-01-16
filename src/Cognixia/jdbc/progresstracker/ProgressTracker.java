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
		
		char run = 'y';
		
		while (run != 'n') {
			
			System.out.println("Do you have an existing account with JUMP Tv? (type 'yes' or 'no'): ");
			String ans = in.next().toLowerCase();
			
			if (!ans.equals("no")) {
				
				user = login(conn, statement, prepStatement, result, user, in);
				System.out.println(user);
				
				if(user != null) { //if user is not null 
					
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
								addShow(conn, statement, prepStatement, result, user, in);
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
					}// Running the menu
					
					System.out.println("Thank you for using Jump Tv Watchlist!");					
				}					
				
//				System.out.println("Connect to app?: ");
//				ans = in.nextLine();			
			} else {
				
				createUser(conn, statement, prepStatement, result, user, in);// if login returns null				
			} // while ans is yes or no
			
			System.out.println("Connect to JUMP TV? (y or n): ");
			run = in.next().charAt(0);
		}//Run app loop (y or n);
		
		in.close();
	}//main
	
	
	public static User createUser(Connection conn, Statement statement, PreparedStatement prepStatement, ResultSet result, User user, Scanner in) {
		
		String username = "";
		String password = "";
		
		System.out.println("Please Enter a Username: ");
		username = in.next();
		System.out.println("Enter a Password: ");
		password = in.next();
		
		user = new User(username, password);
		
		String query = "insert into user(username, password) values('" + user.getUsername() + "', '"+ user.getPassword() + "');";
		System.out.println(query);
		
		try {
			
			statement = conn.createStatement();
			int row = statement.executeUpdate(query);
			System.out.println(row + " row/s was affected");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			System.out.println("An error has occured while creating your profile or user exists... ");
			e.printStackTrace();
		}		
		
		return user;
	}
	
	public static User login(Connection conn, Statement statement, PreparedStatement prepStatement, ResultSet result, User user, Scanner in) {
		
		System.out.println("================ Login ===============");
		System.out.println("username: ");
		String logUser = in.next();
		System.out.println("password: ");
		String logPass = in.next();
		user = new User(logUser, logPass);
//		System.out.println("user: " + user.getUsername() + "  password: " + user.getPassword());
		
		try {			
			
			prepStatement = conn.prepareStatement("select username, password from user where username= ? and password= ?");			
			
			String testLogUser = "";
			String testLogPass = "";
			
			result = prepStatement.executeQuery();
			
			if (result.next()) {
				
				testLogUser = result.getString(1);
				testLogPass = result.getString(2);	
			}			
			
			System.out.println("login: " + testLogUser);
			System.out.println("password: " + testLogPass);
			
			return user;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println();
			System.out.println("\nWrong Username/Password ============== (T.T)");
			System.out.println("Do you want to create a username and password? 'y' or 'n': ");
			char reply = in.next().charAt(0);
			
			if (reply != 'y') {
				
				System.out.println("Cannot login to server without username and password.... exiting application");
				user=null;
				return user;
			} else {
				
				createUser(conn, statement, prepStatement, result, user, in);
			}
			e.printStackTrace();
			return null;
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
		
		
		
	public static void addShow(Connection conn, Statement statement, PreparedStatement prepStatement, ResultSet result, User user,  Scanner readIn) {
		int id = 0;
		String yesNo = "";
		boolean exit = false;
		
		//Display shows that aren't being tracked
		
		while(true) {
			try{				
				
				statement = conn.createStatement();
				result  = statement.executeQuery("select * from tv_show ");
				
				while(result.next()) {
					
					System.out.println("Show ID: " + result.getInt(1) + "  Title: " + result.getString(2) + "  Description/Summary: \n" + result.getString(3));
				}
								
				System.out.println("Enter the id for the show you wish to add to your watch list: ");
				id = readIn.nextInt();
				
				String getId = "select tv_id from tv_show where tv_id = ?";					
				prepStatement = conn.prepareStatement(getId);
				prepStatement.setInt(1, id);
				
				result = prepStatement.executeQuery();
				
				//Check shows for valid id
				if(result.next()) {
					
					System.out.println("Title: " + result.getInt(1)); //puts out the value of the show
				}
				
				prepStatement = conn.prepareStatement("insert into has_show(username, tv_id) values (?,?)");
				prepStatement.setString(1, user.getUsername());
				prepStatement.setInt(2, id);
				
				result = prepStatement.executeQuery();
				break;
			}
			catch(InputMismatchException | SQLException e) {
				System.out.println();
				System.out.println("Invalid ID");
//				readIn.nextLine();
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
				addShow(conn, statement, prepStatement, result, user, readIn);
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
