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
    public static void outputAuditorium(int rows, int colums, char[][] auditorium, String newColLetter){
        System.out.println("  " + newColLetter);

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

    public static boolean isAvailable(int userRow, int userAdultTickets, int userChildTickets, int userSeniorTickets, char[][] auditorium, char userCharLet, String newColString){
        int startSeat = newColString.indexOf(userCharLet);
        int endSeat = startSeat + userAdultTickets + userChildTickets + userSeniorTickets;

        for(int i = startSeat; i < endSeat; i++){
            if(auditorium[userRow][i] ==  '#'){
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
    
    public static int[] getBestAvailable(int userRow, int totalTickets, char[][] auditorium){
        int[] bestSeats = new int[2]; // This will hold the start and end of the best available seats
        int maxCount = 0; // This will hold the maximum count of sequential available seats
        int count = 0; // This will hold the current count of sequential available seats
        int center = auditorium[0].length / 2; // This is the center of the row
        int bestDistance = auditorium[0].length; // This will hold the smallest distance from center
    
        for(int i = 0; i < auditorium[userRow].length; i++){
            if(auditorium[userRow][i] == '.'){
                count++;
                if(count >= totalTickets){
                    int distance = Math.abs(center - (i - count / 2)); // Distance from center to middle of sequence
                    if(distance < bestDistance || (distance == bestDistance && count > maxCount)){
                        bestDistance = distance;
                        maxCount = count;
                        bestSeats[0] = i - count + 1;
                        bestSeats[1] = i - count + totalTickets;
                    }
                }
            } else {
                count = 0;
            }
        }
    
        if(maxCount >= totalTickets){
            return bestSeats;
        } else {
            return null; // Return null if no suitable seats found
        }
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

        String newColLetter = colLetter.substring(0, col); 

        boolean valid = false;

        char[][] updatedAuditorium = new char[row][col];

        while(choice != 2){
            menu();
            choice = scnr.nextInt();
            if(choice == 2) break;
            outputAuditorium(row, col, auditorium, newColLetter);
            
            userRow = getValidInput(scnr, "Enter row: ", 1, row); // Get the row number
            userRow -= 1; // Decrement the row number by 1 to match the index of the array

            // Get the seat letter
            while(!valid){
                System.out.print("Enter seat: ");
                userCharLet = Character.toUpperCase(scnr.next().charAt(0));
                if(userCharLet >= 'A' && userCharLet <= newColLetter.charAt(col - 1)){ // col-1 because the newColLetter string is one less than the actual number of columns
                    valid = true;
                }
            }
            valid = false;

            // Get the number of tickets for each type of ticket
            userAdultTickets = getValidInput(scnr, "Enter adult tickets: ", 0, Integer.MAX_VALUE);
            userChildTickets = getValidInput(scnr, "Enter child tickets: ", 0, Integer.MAX_VALUE);
            userSeniorTickets = getValidInput(scnr, "Enter senior tickets: ", 0, Integer.MAX_VALUE);

            if(isAvailable(userRow, userAdultTickets, userChildTickets, userSeniorTickets, auditorium, userCharLet, newColLetter)){
                exit();
            }
            else{
                int[] bestSeats = getBestAvailable(userRow, userAdultTickets + userChildTickets + userSeniorTickets, auditorium);
                
                if(bestSeats != null){
                    char startingSeatLetter = (char)('A' + bestSeats[0]); // Convert the starting seat number to a letter
                    char endingSeatLetter = (char)('A' + bestSeats[1]); // Convert the ending seat number to a letter

                    System.out.println("Best available seats: " + (userRow + 1) + startingSeatLetter + " - " + (userRow + 1) + endingSeatLetter);
                    System.out.println("Reserve seats? (Y/N)");
                    char response = Character.toUpperCase(scnr.next().charAt(0));

                    if(response == 'Y'){
                        int startSeat = bestSeats[0];
                        int endSeat = bestSeats[1];

                        for (int i = startSeat; i < startSeat + userAdultTickets; i++) {
                            auditorium[userRow][i] = '#';
                            updatedAuditorium[userRow][i] = 'A';
                        }
                        for (int i = startSeat + userAdultTickets; i < startSeat + userAdultTickets + userChildTickets; i++) {
                            auditorium[userRow][i] = '#';
                            updatedAuditorium[userRow][i] = 'C';
                        }
                        for (int i = startSeat + userAdultTickets + userChildTickets; i <= endSeat; i++) {
                            auditorium[userRow][i] = '#';
                            updatedAuditorium[userRow][i] = 'S';
                        }
                    }
                    else{
                        System.out.println("no seats available");
                    }
                }
            }

        }
        try{
            PrintStream out = new PrintStream(new File("A1.txt"));
            for(int i = 0; i < row; i++){
                for(int j = 0; j < col; j++){
                    out.print(updatedAuditorium[i][j]);
                }
                out.println();
            }
            out.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Error writing to file");
            System.exit(0); // Exit the program
        }
            
            scnr.close();
    }
}