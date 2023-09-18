// Sawol Tesfaghebriel
// SYT210001

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintStream;
import java.lang.Exception;

public class Main {

    // Outputs the menu
    public static void menu(){
        System.out.print("1. Reserve Seats\n2. Exit\nEnter Choice: ");
    }

    // Outputs the auditorium to the console
    public static void outputAuditorium(int rows, int colums, char[][] auditorium, String colLetter){
        System.out.println("  " + colLetter);

        for(int i = 0; i < rows; i++){
            System.out.print(i+1 + " ");
            for(int j = 0; j < colums; j++){
                System.out.print(auditorium[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int getValidInput(Scanner scnr, String prompt, int minValue, int maxValue) {
        int userInput;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            userInput = scnr.nextInt();
            if (userInput >= minValue && userInput <= maxValue) {
                valid = true;
                return userInput;
            }
        }
        return -1; // This line should never be reached
    }

    public static boolean isAvailable(int userRow, int userAdultTickets, int userChildTickets, int userSeniorTickets, char[][] auditorium, char userCharLet){
        int startSeat = userCharLet - 'A';
        int endSeat = startSeat + userAdultTickets + userChildTickets + userSeniorTickets;

        for(int i = startSeat; i < endSeat; i++){
            if(auditorium[userRow][i] != '.'){
                return false;
            }
        }
        return true; // If the seats are available

    }
    // function to output the updated auditorium to A1.txt and output total sales and tickets
    public static void exit(){
        System.out.println("Thank you for using the program"); // placeholder
        System.exit(0);
    }
    

    public static void main(String[] args) {

        char[][] auditorium = new char[10][26]; // Declare and Initialize the auditorium 2d array to the max size

        Scanner scnr = new Scanner(System.in);

        // Declare and initialize variables to 0
        int col = 0, row = 0, totAdultTickets = 0, totchildTickets = 0, totSeniorTickets = 0, choice = 0, userAdultTickets = 0, userChildTickets = 0, userSeniorTickets = 0, userRow = 0;
        Double Sales = 0.0;

        String colLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        char userCharLet = 'A';

        System.out.print("Enter file name: ");
        String fileName = scnr.next();
        File inFile = new File(fileName);

        // If the file exist
        try{
            FileInputStream fileStream = new FileInputStream(inFile);
            Scanner fileScnr = new Scanner(fileStream);
            while(fileScnr.hasNextLine()){
                String line = fileScnr.nextLine();
                if(line.isBlank()) continue;

                for(col = 0; col < line.length(); col++){
                    if(line.charAt(col) != '.'){
                        if(line.charAt(col) == 'A') totAdultTickets++;
                        else if(line.charAt(col) == 'C') totchildTickets++;
                        else if(line.charAt(col) == 'S') totSeniorTickets++;

                        auditorium[row][col] = '#';
                    }
                    else{
                        auditorium[row][col] = '.';
                    }
                }
                row++;                
            }

            fileScnr.close();
        }
        // If the file does not exist
        catch(FileNotFoundException e){
            System.out.println("File Not found");
            System.exit(0); // Exit the program
        }

        colLetter = colLetter.substring(0, col); 

        boolean valid = false;
        while(choice != 2){
            menu();
            choice = scnr.nextInt();
            if(choice == 2) exit();
            outputAuditorium(row, col, auditorium, colLetter);
            
            userRow = getValidInput(scnr, "Enter row: ", 1, row); // Get the row number

            // Get the seat letter
            while(!valid){
                System.out.print("Enter seat: ");
                userCharLet = Character.toUpperCase(scnr.next().charAt(0));
                if(userCharLet >= 'A' && userCharLet <= colLetter.charAt(col - 1)){ // col-1 because the colLetter string is one less than the actual number of columns
                    valid = true;
                }
            }
            valid = false;

            // Get the number of tickets for each type of ticket
            userAdultTickets = getValidInput(scnr, "Enter adult tickets: ", 0, Integer.MAX_VALUE);
            userChildTickets = getValidInput(scnr, "Enter child tickets: ", 0, Integer.MAX_VALUE);
            userSeniorTickets = getValidInput(scnr, "Enter senior tickets: ", 0, Integer.MAX_VALUE);

            if(isAvailable(userRow, userAdultTickets, userChildTickets, userSeniorTickets, auditorium, userCharLet)){
                exit();
            }
            else{
                System.out.println("The seats are not available"); // placeholder
            }

        }
            //System.out.println(userRow + " " + userCharLet + " " + userAdultTickets+ " " + userChildTickets + " " + userSeniorTickets); // For testing purposes
            
            scnr.close();
    }
}