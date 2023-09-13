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

    public static void main(String[] args) {

        char[][] auditorium = new char[10][26]; // Declare and Initialize the auditorium 2d array to the max size

        Scanner scnr = new Scanner(System.in);

        // Declare and initialize variables to 0
        int col = 0, row = 0, adultTickets = 0, childTickets = 0, seniorTickets = 0, choice = 0;
        Double Sales = 0.0;

        String colLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

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
                        if(line.charAt(col) == 'A') adultTickets++;
                        else if(line.charAt(col) == 'C') childTickets++;
                        else if(line.charAt(col) == 'S') seniorTickets++;

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

        colLetter = colLetter.substring(0, col); // Change the colLetter to the correct amount of letters

        while(choice != 2){
            menu();
            choice = scnr.nextInt();
            if(choice == 2) continue;
            outputAuditorium(row, col, auditorium, colLetter);

        }

        scnr.close();
    }
}