// Sawol Tesfaghebriel
// SYT210001

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintStream;
import java.lang.Exception;

public class Main {
    public static void main(String[] args) {

        char[][] auditorium = new char[10][26];
        Scanner scnr = new Scanner(System.in);
        int col = 0, row = 0, adultTickets = 0, childTickets = 0, seniorTickets = 0;
        String colLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        System.out.print("Enter file name: ");
        String fileName = scnr.next();
        File inFile = new File(fileName);
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
        catch(FileNotFoundException e){
            System.out.println("File Not found");
            System.exit(0);
        }

            System.out.println(col + " " + row);
            System.out.println(adultTickets + " " + childTickets + " " + seniorTickets);


        scnr.close();
    }
}