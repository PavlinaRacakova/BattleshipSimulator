package org.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.battleship.BattleshipPlacementValidator.ValidationResult.SHIP_PLACED_INCORRECTLY;
import static org.battleship.BattleshipPlacementValidator.ValidationResult.VALID;
import static org.battleship.BattleshipPlacementValidator.hasValidSize;
import static org.battleship.BattleshipPlacementValidator.isPlacedOutsideOtherShips;
import static org.battleship.BattleshipPlacementValidator.*;

public class Main {


    public static void main(String[] args) {
        //necessary variables
        Scanner sc = new Scanner(System.in);
        String usersInput;
        ValidationResult validationResult = SHIP_PLACED_INCORRECTLY;
        List<String> coordinates;
        List<String> namesOfBattleships = new ArrayList<>(List.of
                ("the Aircraft Carrier (5 cells)", "the Cruiser (3 cells)", "the Submarine (3 cells)", "the Destroyer (2 cells)"));
        List<Integer> lengthsOfBattleships = new ArrayList<>(List.of(5, 3, 3, 2));
        String[][] battlefield = {
                {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", " "},
                {"A", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~", " "},
                {"B", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~", " "},
                {"C", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~", " "},
                {"D", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~", " "},
                {"E", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~", " "},
                {"F", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~", " "},
                {"G", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~", " "},
                {"H", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~", " "},
                {"I", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~", " "},
                {"J", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~", " "},
                {" "," "," "," "," "," "," "," "," "," "," "}
        };

        for (int i = 0; i < 4; i++) {
            while (true) {
                try {
                    System.out.printf("Enter the coordinates of %s divided by whitespace:\n(e.g. for a ship that requires 3 cells: E1 E2 E3)\n", namesOfBattleships.get(i));
                    usersInput = sc.nextLine().toUpperCase();
                    coordinates = new ArrayList<>(List.of(usersInput.split("\\s")));
                    //checks if given coordinates are in bounds and consist of allowed characters
                    for (String s : coordinates) {
                        if (!(s.matches("^[A-J][1-9]$") || s.matches("^[A-J]10$"))) {
                            throw new Exception();
                        }
                    }
                    //validates the coordinates
                    validationResult = hasValidSize()
                            .and(canBePlacedToTheField())
                            .and(isPlacedOutsideOtherShips())
                            .and(isNotTooCloseToOtherShips())
                            .apply(coordinates, lengthsOfBattleships.get(i), battlefield);

                    if (validationResult != VALID) {
                        throw new ValidationException();
                    }

                    for (String coordinate : coordinates) {
                        battlefield[coordinate.charAt(0) - 'A' + 1][Integer.parseInt(coordinate.substring(1))] = "O";
                    }
                    printBattlefield(battlefield);
                    break;
                } catch (ValidationException e) {
                    System.out.println("Something is wrong: " + validationResult + ". Please try again.");
                } catch (Exception e) {
                    System.out.println("These coordinates do not exist. Please try again.");
                }
            }
        }


        sc.close();

    }

    public static void printBattlefield(String[][] board) {
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    public static class ValidationException extends Exception {
        public ValidationException() {
            super();
        }
    }

}