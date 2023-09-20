// Sawol Tesfaghebriel
// SYT210001

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintStream;

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
    
    public static int bestAvailable(char[][] auditorium_array, int r, int t, int c) { // r = row, t = total tickets, c = columns
        int seats_selection = -1;
        int dis = Integer.MAX_VALUE;

        // Loop through the row
        for (int i = 0; i <= auditorium_array[r].length - t; i++) {
            boolean seatsAvailable = true;
            // Checks if consecutive seats are available
            for (int j = 0; j < t; j++) {
                if (auditorium_array[r][i + j] != '.') { 
                    seatsAvailable = false;
                    break;
                }
            }
            if (seatsAvailable) {
                int columns = c -1;
                int midOfRow = columns / 2;
                int midOfSeats = i + t / 2;
    
                int currentDis = Math.abs(midOfRow - midOfSeats);
    
                if (currentDis < dis) {
                    dis = currentDis;
                    seats_selection = i;
                } 
                else if (currentDis == dis) {
                    if (i < seats_selection){
                        dis = currentDis;
                        seats_selection = i;
                    }
                }
            }
        }
    
        return seats_selection;
    }

    public static void main(String[] args) {

        char[][] auditorium = new char[10][26]; // Declare and Initialize the auditorium 2d array to the max size
        char[][] updatedAuditorium = new char[10][26]; // Declare and Initialize the updated auditorium 2d array to the max size which will be used to output to A1.txt
        
        Scanner scnr = new Scanner(System.in);

        // Declare and initialize variables to 0
        int col = 0, row = 0, totAdultTickets = 0, totchildTickets = 0, totSeniorTickets = 0, choice = 0, userAdultTickets = 0, userChildTickets = 0, userSeniorTickets = 0, userRow = 0;

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
                        auditorium[row][col] = '#';
                        updatedAuditorium[row][col] = line.charAt(col);
                    }
                    else{
                        auditorium[row][col] = '.';
                        updatedAuditorium[row][col] = line.charAt(col);
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

            // Check if the seats are available that the user requested and reserve them
            if(isAvailable(userRow, userAdultTickets, userChildTickets, userSeniorTickets, auditorium, userCharLet, newColLetter)){
                int startSeat = newColLetter.indexOf(userCharLet);
                // update the auditorium array and the updated auditorium array
                for (int i = startSeat; i < startSeat + userAdultTickets; i++) {
                    auditorium[userRow][i] = '#';
                    updatedAuditorium[userRow][i] = 'A';
                }
                for (int i = startSeat + userAdultTickets; i < startSeat + userAdultTickets + userChildTickets; i++) {
                    auditorium[userRow][i] = '#';
                    updatedAuditorium[userRow][i] = 'C';
                }
                for (int i = startSeat + userAdultTickets + userChildTickets; i < startSeat + userAdultTickets + userChildTickets + userSeniorTickets; i++) {
                    auditorium[userRow][i] = '#';
                    updatedAuditorium[userRow][i] = 'S';
                }
                continue;
            }
            else{
                int bestSeat = bestAvailable(auditorium, userRow, userAdultTickets + userChildTickets + userSeniorTickets, col); // Get the best available seat
                
                if(bestSeat != -1){
                    char startingSeatLetter = (char)('A' + bestSeat); // Convert the starting seat number to a letter
                    char endingSeatLetter = (char)('A' + bestSeat + userAdultTickets + userChildTickets + userSeniorTickets - 1); // Convert the ending seat number to a letter

                    char response = 'Y'; // default response is yes
                    
                    // Output the best available seats and ask the user if they want to reserve them
                    System.out.println("Best available seats: " + (userRow + 1) + startingSeatLetter + " - " + (userRow + 1) + endingSeatLetter);
                    System.out.println("Reserve seats? (Y/N)");
                    response = Character.toUpperCase(scnr.next().charAt(0));

                    // If the user wants to reserve the seats then update the auditorium array and the updated auditorium array
                    if(response == 'Y'){
                        for (int i = bestSeat; i < bestSeat + userAdultTickets; i++) {
                            auditorium[userRow][i] = '#';
                            updatedAuditorium[userRow][i] = 'A';
                        }
                        for (int i = bestSeat + userAdultTickets; i < bestSeat + userAdultTickets + userChildTickets; i++) {
                            auditorium[userRow][i] = '#';
                            updatedAuditorium[userRow][i] = 'C';
                        }
                        for (int i = bestSeat + userAdultTickets + userChildTickets; i < bestSeat + userAdultTickets + userChildTickets + userSeniorTickets; i++) {
                            auditorium[userRow][i] = '#';
                            updatedAuditorium[userRow][i] = 'S';
                        }
                    }
                }
                else{
                    System.out.println("no seats available");
                }
            }

        }        
        try{ // Try to write to the file

            // Output the updated auditorium to A1.txt
            PrintStream out = new PrintStream(new File("A1.txt")); // Create a new file called A1.txt to output the updated auditorium
            for(int i = 0; i < row; i++){
                for(int j = 0; j < col; j++){
                    if(updatedAuditorium[i][j] == 'A') totAdultTickets++;
                    else if(updatedAuditorium[i][j] == 'C') totchildTickets++;
                    else if(updatedAuditorium[i][j] == 'S') totSeniorTickets++;
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

        // Output the total sales and tickets
        System.out.println("Total Seats: " + (row * col));
        System.out.println("Total Tickets: " + (totAdultTickets + totchildTickets + totSeniorTickets));
        System.out.println("Adult Tickets: " + totAdultTickets);
        System.out.println("Child Tickets: " + totchildTickets);
        System.out.println("Senior Tickets: " + totSeniorTickets);
        System.out.printf("Total Sales: $%.2f\n", (totAdultTickets * 10.00) + (totchildTickets * 5.00) + (totSeniorTickets * 7.50));

            scnr.close();
    }
}