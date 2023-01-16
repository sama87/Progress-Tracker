package Cognixia.JDBC.ProgressTracker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
		String status = "";
		int temp = 0;
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
			System.out.println("Do you have an existing account with JUMP Tv? ('yes' or 'no'): ");
			String ans = in.next().toLowerCase();
			
			if (!ans.equals("no")) {
				
				user = login(conn, statement, prepStatement, result, user, in);
				
				if(user != null) { //if user is not null 
					
					in.nextLine();
					boolean running = true;
					
					while(running) {
						
						try{
							//Display tracked shows

							statement = conn.createStatement();
							result = statement.executeQuery("select tv_show.tv_id, name, description, percentage_completed from has_show "
									+ "inner join tv_show on tv_show.tv_id = has_show.tv_id; ");
							System.out.println("\nShows currently on watch list:\n");
							
							while(result.next()) {
								status = "";
								temp = result.getInt(4);
								if (temp == 0) status = "Not Completed";
								else if (temp == 100) status = "Completed";
								else status = "In Progress";
								System.out.println("Show ID: " + result.getInt(1) + "   Title: " + result.getString(2)+ "   Status: " + status + " "+ result.getInt(4) + "%   Description: \n" + result.getString(3)
								);
							}
						}
						catch (SQLException e) {
							e.printStackTrace();
						}
						
						System.out.println("\n*****************");
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
								updateProgress(conn, statement, prepStatement, result, user, in);
								break;
							case("2"):
								addShow(conn, statement, prepStatement, result, user, in);
								break;
							case("3"):
								dropShow(conn, statement, prepStatement, result, user, in);
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
		System.out.println("user: " + user.getUsername() + "  password: " + user.getPassword());
		
		
		String testLogUser = "";
		String testLogPass = "";
		
		try {			
			
			prepStatement = conn.prepareStatement("select username, password from user where username= ? and password= ?");
			prepStatement.setString(1, user.getUsername());
			prepStatement.setString(2, user.getPassword());
			
			
			
			result = prepStatement.executeQuery();
			
			while (result.next()) {
				
				testLogUser = result.getString(1);
				testLogPass = result.getString(2);	
			}				
			
			//execute
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println();			
		}
		
		if ((!testLogUser.equals(user.getUsername())) || (!testLogPass.equals(user.getPassword()))){
		
			System.out.println("Invalid Username/Password");
			System.out.println("/nDo you want to create username and password with Jump TV ('y' or 'n'): ");
			char reply = in.next().charAt(0);
			
			if (reply == 'y'){
				
				createUser(conn, statement, prepStatement, result, user, in);
			} else {
				
				user = null;
			}
		} else {
			
			return user;
		}
		
		return user; // is null if creating username and password = 'n'
		
	}
		
	public static void updateProgress(Connection conn, Statement statement, PreparedStatement prepStatement, ResultSet result, User user,Scanner readIn) {
		int id;
		int epsSeen;
		int epsExisting;
		int percent = 0;
		String yesNo = "";
		boolean exit = false;
		ResultSet rs2 = null;

		while(true) {
			try{
				//Display tracked shows

				statement = conn.createStatement();
				result = statement.executeQuery("select tv_show.tv_id, name, description, percentage_completed from has_show "
						+ "inner join tv_show on tv_show.tv_id = has_show.tv_id; ");
				System.out.println("\nShows currently on watch list:\n");
				
				while(result.next()) {
					System.out.println("Show ID: " + result.getInt(1) + "   Title: " + result.getString(2)+ "   Completed: " + result.getInt(4) + "%   Description: \n" + result.getString(3)
					);
				}
				
				
				System.out.println("\nEnter the id for the show you wish to update: ");
				id = readIn.nextInt();
				readIn.nextLine();
				//Check tracked shows for valid id, update if valid
				
				result = statement.executeQuery("select tv_show.tv_id, name from has_show "
						+ "inner join tv_show on tv_show.tv_id = has_show.tv_id; ");
				while(result.next()) {
					if (id == result.getInt(1)  ) {
						System.out.println("How many episodes of " + result.getString(2) + " have you seen?");
						epsSeen = readIn.nextInt();
						readIn.nextLine();
						
						rs2 = statement.executeQuery("select COUNT(episode_id) from episode "
								+ "inner join tv_show on tv_show.tv_id = episode.tv_id "
								+ "where tv_show.tv_id = " + id + ";");
						rs2.next();
						epsExisting = rs2.getInt(1);
						
						if (epsSeen > epsExisting) {
							throw new Exception ("There aren't that many episodes!");
//							System.out.println("There aren't that many episodes!");
//							id = 0;
//							break;
						}
						else {
							percent = (epsSeen * 100) / epsExisting;
							System.out.println(percent);
							statement.executeUpdate("update has_show set percentage_completed = " + percent + " where username = \"" +
							user.getUsername() + "\" AND tv_id = " + id + ";");

						}
						
						System.out.println("Your progress has been updated: ");
						rs2 = statement.executeQuery("select tv_show.tv_id, name, description, percentage_completed from tv_show " +
						"inner join has_show on tv_show.tv_id = has_show.tv_id where has_show.tv_id = " + id + ";");
						rs2.next();
						System.out.println("Show ID: " + rs2.getInt(1) + "   Title: " + rs2.getString(2) + 
								"   Completed: " + rs2.getInt(4) + "%   Description: \n" + rs2.getString(3));
						
						id = 0;
						break;
					}			
				}
				if(id != 0) System.out.println("Show ID " +  id + " is invalid. No changes where made");
				break;
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Must be a number!");
				readIn.nextLine();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			
		}//while
		
		//Update the episodes watched in DB
		
		
		while(exit == false) {
			System.out.println("Would you like to update another show? (Y/N)");
			yesNo = readIn.nextLine();
			yesNo = yesNo.toUpperCase();
			if (yesNo.charAt(0) == 'Y') {
				updateProgress(conn, statement, prepStatement, result, user,readIn);
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
				result  = statement.executeQuery("Select tv_show.tv_id, name, description FROM tv_show"
						+ " left outer join has_show on has_show.tv_id = tv_show.tv_id"
						+ " where has_show.tv_id is null;");
				
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
				

				statement.executeUpdate("Insert into has_show(username, tv_id)\r\n"
						+ "values (\"" + user.getUsername() + "\" , " + id + ");");
				//String userName ="\"" + user.getUsername() + "\"";
				//prepStatement.setString(1, userName);
				//prepStatement.setInt(2, id);
				
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
	
	public static void dropShow(Connection conn, Statement statement, PreparedStatement prepStatement, ResultSet result, User user,Scanner readIn) {
		int id;
		String yesNo = "";
		boolean exit = false;
	
		while(true) {
			try{
				//Display shows that are being tracked
				statement = conn.createStatement();
				result = statement.executeQuery("select tv_show.tv_id, name, description, percentage_completed from has_show "
						+ "inner join tv_show on tv_show.tv_id = has_show.tv_id; ");
				System.out.println("\nShows currently on watch list:\n");
				
				
				while(result.next()) {
					System.out.println("Show ID: " + result.getInt(1) + "   Title: " + result.getString(2)+ "   Completed: " + result.getInt(4) + "%   Description: \n" + result.getString(3)
					);
				}
				
				System.out.println("\nEnter the id for the show you wish to remove from your watch list: ");
				id = readIn.nextInt();
				
				//Remove selection from watch list 
				statement = conn.createStatement();
				result = statement.executeQuery("select tv_show.tv_id, name from has_show "
						+ "inner join tv_show on tv_show.tv_id = has_show.tv_id; ");
				while(result.next()) {
					if (id == result.getInt(1)  ) {
						System.out.println(result.getString(2) + " has been removed from your watch list");
						statement.executeUpdate("DELETE FROM has_show WHERE tv_id = " + id);
						id = 0;
						break;
					}			
				}
				if(id != 0) System.out.println("Show ID " +  id + " is invalid. No changes where made");

				break;
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Must be a number!");
				readIn.nextLine();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}//while
		
		readIn.nextLine();
		
	
		
		System.out.println();
		while(exit == false) {
			System.out.println("Would you like to remove another show? (Y/N)");
			yesNo = readIn.nextLine();
			yesNo = yesNo.toUpperCase();
			if (yesNo.charAt(0) == 'Y') {
				dropShow(conn, statement, prepStatement, result, user, readIn);
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
