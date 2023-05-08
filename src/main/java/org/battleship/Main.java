package org.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<String> coordinates;
        String usersInput;
        Scanner sc = new Scanner(System.in);
        String[][] battlefield = {
                {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
                {"A", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
                {"B", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
                {"C", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
                {"D", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
                {"E", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
                {"F", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
                {"G", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
                {"H", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
                {"I", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
                {"J", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"}
        };


        while (true) {
            try {
                System.out.println("Give positons");
                usersInput = sc.nextLine().toUpperCase();
                coordinates = new ArrayList<>(List.of(usersInput.split("\\s")));
                //checks if given coordinates are in bounds and consist of allowed characters
                for (String s : coordinates) {
                    if (!s.matches("^[A-J][1-9]|10$")) {
                        throw new Exception();
                    }
                }
                break;
            } catch (Exception e) {
                System.out.println("The coordinates were entered incorrectly. Please try again.");
            }
        }




        sc.close();

    }
}