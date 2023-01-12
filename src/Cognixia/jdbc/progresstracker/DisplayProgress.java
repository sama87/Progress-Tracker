package Cognixia.jdbc.progresstracker;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DisplayProgress {

	public static void main(String[] args) {
		boolean running = true;
		Scanner readIn = new Scanner(System.in);
		
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
			menuInput = readIn.nextLine();
			switch(menuInput) {
				case("1"):
					updateProgress(readIn);
					break;
				case("2"):
					addShow(readIn);
					break;
				case("3"):
					dropShow(readIn);
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
		readIn.close();
	}//main
	
	
	
	
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

}//class
