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
        int invalidInputCounter = 0;
        ValidationResult validationResult = SHIP_PLACED_INCORRECTLY;
        List<String> coordinates;
        List<String> usedCoordinatesByUser = new ArrayList<>();
        List<String> namesOfBattleships = new ArrayList<>(List.of
                ("the Aircraft Carrier", "the Cruiser", "the Submarine", "the Destroyer"));
        List<Integer> lengthsOfBattleships = new ArrayList<>(List.of(5, 3, 3, 2));
        List<Battleship> battleships = new ArrayList<>();

        String[][] mainBattlefield = {
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
                {" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "}
        };

        String[][] gameBattlefield = {
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
                {" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "}
        };

        System.out.println("""
                                __/___           \s
                          _____/______|          \s
                  _______/_____\\_______\\_____    \s
                  \\              < < <       |   \s
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");
        System.out.println("\nWELCOME TO THE BATTLESHIP SIMULATOR\n");

        //placement of battleships in the battlefield
        for (int i = 0; i < 4; i++) {
            while (true) {
                try {
                    System.out.printf("Enter the coordinates of %s (%d cells) divided by whitespace:\n(e.g. for a ship that requires 3 cells: E1 E2 E3)\n",
                            namesOfBattleships.get(i), lengthsOfBattleships.get(i));
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
                            .apply(coordinates, lengthsOfBattleships.get(i), mainBattlefield);

                    if (validationResult != VALID) {
                        throw new ValidationException();
                    }
                    battleships.add(new Battleship(namesOfBattleships.get(i), coordinates, mainBattlefield));
                    printBattlefield(mainBattlefield);
                    break;
                } catch (ValidationException e) {
                    System.out.println("Something is wrong: " + validationResult + ". Please try again.");
                } catch (Exception e) {
                    System.out.println("These coordinates do not exist. Please try again.");
                }
            }
        }

        clearScreen();
        System.out.println("""
                ⣿⣿⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣿⣿⡆
                ⠉⠹⣿⣷⣀⠀⠀⠀⠀⢰⣶⡤⠀⢤⣴⡶⠀⠀⠀⠀⢀⣼⣿⠟⠉⠁
                ⠀⠀⠈⠻⣿⣦⣄⠀⢀⣼⡟⠓⠀⠘⠛⣷⣄⠀⢀⣴⣿⡿⠁⠀⠀⠀
                ⠀⠀⠀⠀⠙⢿⣿⣶⣿⠉⠀⠀⠀⠀⠀⠈⢻⣷⣿⡿⠋⠀⠀⠀⠀⠀
                ⠀⢀⡀⢀⣠⡾⠿⡹⢆⠠⡀⠀⠀⠀⢀⠔⣡⠞⡹⢷⡄⡀⢀⡀⠀⠀
                ⠀⠸⡯⣿⠛⠁⠀⠀⢈⠲⣌⠢⡀⡠⣡⠞⡡⠊⠀⠀⠛⣿⢿⡛⠀⠀
                ⠀⠀⠁⠀⠀⠀⠀⠀⠀⠑⢌⡱⢊⡴⢃⠌⠀⠄⠀⠀⠀⠀⠀⠁⠀⠀
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠜⠔⢋⢔⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠀⠀⠀⡐⡡⢂⠔⠉⠢⡙⢌⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠀⡠⠊⠈⡠⠀⠀⠀⠀⠈⠢⡁⠌⢆⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⢀⠔⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠑⠄⠀⠀⠀⠀⠀
                ⠀⠀⠀⡠⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠠⡈⢢⠀⠀⠀⠀
                ⠀⠀⣔⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡡⡀⠀⠀
                ⠀⠈⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀""");
        System.out.println("THE GAME STARTS\n");

        //main game loop
        while (!battleships.isEmpty()) {

            while (true) {
                try {
                    System.out.println("Take a shot:");
                    usersInput = sc.nextLine().toUpperCase();
                    if (!(usersInput.matches("^[A-J][1-9]$") || usersInput.matches("^[A-J]10$"))) {
                        throw new Exception();
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("The coordinate was entered incorrectly. Please try again.");
                }
            }

            if (usedCoordinatesByUser.contains(usersInput)) {
                System.out.println("You have already used this coordinate.");
                continue;
            }

            usedCoordinatesByUser.add(usersInput);

            boolean nothingWasHit = true;

            for (Battleship ship : battleships) {
                if (ship.isItAHit(usersInput, gameBattlefield)) {
                    if (ship.isNotSunk()) {
                        System.out.println("It was a hit!");
                    } else {
                        System.out.printf("%s was destroyed!\n", ship.getName());
                        System.out.println("""
                                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣄⠈⠛⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⣴⣄⠀⢀⣤⣶⣦⣀⠀⠀⣰⣿⣿⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⢸⣿⣷⣌⠻⢿⣩⡿⢷⣄⠙⢿⠟⠀⠀⠀⠀⠀⠰⣄⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠈⣿⣿⣿⣷⣄⠙⢷⣾⠟⣷⣄⠀⠀⠀⠀⣠⣿⣦⠈⠀⠀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⢿⣿⣿⣿⣿⣷⣄⠙⢿⣏⣹⣷⣄⠀⢴⣿⣿⠃⠀⠀⠀⠀⢀⡀⠀⠀
                                ⠀⠀⠀⠸⣦⡙⠻⣿⣿⣿⣿⣷⣄⠙⢿⣤⡿⢷⣄⠙⠃⠀⠀⠀⠀⣀⡈⠻⠂⠀
                                ⠀⠀⠀⠀⠈⠻⣦⡈⠻⣿⣿⣿⣿⣷⣄⠙⢷⣾⠛⣷⣄⠀⠀⢀⣴⣿⣿⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠈⠻⣦⡈⠛⠛⠻⣿⣿⣷⣄⠙⠛⠋⢹⣷⣄⠈⠻⠛⠃⠀⠀⠀
                                ⠀⢀⣴⣿⣧⡀⠀⠀⠈⢁⣼⣿⣄⠙⢿⡿⠋⣠⣿⣧⡀⠠⡿⠗⢀⣼⣿⣦⡀⠀
                                ⠀⠟⠛⠉⠙⠻⣶⣤⣶⠟⠋⠉⠛⢷⣦⣴⡾⠛⠉⠙⠻⣶⣤⣶⠟⠋⠉⠛⠻⠀
                                ⠀⣶⣿⣿⣿⣦⣄⣉⣠⣶⣿⣿⣷⣦⣈⣁⣴⣾⣿⣿⣶⣄⣉⣠⣶⣿⣿⣿⣶⠀
                                ⠀⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠀""");
                    }
                    nothingWasHit = false;
                }
            }

            battleships.removeIf(ship -> !ship.isNotSunk());

            if (nothingWasHit) {
                gameBattlefield[usersInput.charAt(0) - 'A' + 1][Integer.parseInt(usersInput.substring(1))] = "M";
                invalidInputCounter++;
                System.out.println("You missed!");
                System.out.printf("Missed shots: %d/30\n", invalidInputCounter);
            }

            checkIfUserHasLost(invalidInputCounter);
            printBattlefield(gameBattlefield);

        }

        System.out.println("Congratulations! You won!");
        System.out.println("""
                                                   .''.      \s
                       .''.      .        *''*    :_\\/_:     .\s
                      :_\\/_:   _\\(/_  .:.*_\\/_*   : /\\ :  .'.:.'.
                  .''.: /\\ :   ./)\\   ':'* /\\ * :  '..'.  -=:o:=-
                 :_\\/_:'.:::.    ' *''*    * '.\\'/.' _\\(/_'.':'.'
                 : /\\ : :::::     *_\\/_*     -= o =-  /)\\    '  *
                  '..'  ':::'     * /\\ *     .'/.\\'.   '
                      *            *..*         :
                        *
                        *\
                """);

    }

    public static void printBattlefield(String[][] board) {
        System.out.println();
        for (String[] strings : board) {
            for (String string : strings) {
                System.out.print(string);
            }
            System.out.println();
        }
    }

    public static void checkIfUserHasLost(int invalidInputCounter) {
        if (invalidInputCounter >= 30) {
            System.out.println("Too many attempts! You are out of artillery.");
            System.out.println("""
                        ┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼
                        ███▀▀▀██┼███▀▀▀███┼███▀█▄█▀███┼██▀▀▀
                        ██┼┼┼┼██┼██┼┼┼┼┼██┼██┼┼┼█┼┼┼██┼██┼┼┼
                        ██┼┼┼▄▄▄┼██▄▄▄▄▄██┼██┼┼┼▀┼┼┼██┼██▀▀▀
                        ██┼┼┼┼██┼██┼┼┼┼┼██┼██┼┼┼┼┼┼┼██┼██┼┼┼
                        ███▄▄▄██┼██┼┼┼┼┼██┼██┼┼┼┼┼┼┼██┼██▄▄▄
                        ┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼
                        ███▀▀▀███┼▀███┼┼██▀┼██▀▀▀┼██▀▀▀▀██▄┼
                        ██┼┼┼┼┼██┼┼┼██┼┼██┼┼██┼┼┼┼██┼┼┼┼┼██┼
                        ██┼┼┼┼┼██┼┼┼██┼┼██┼┼██▀▀▀┼██▄▄▄▄▄▀▀┼
                        ██┼┼┼┼┼██┼┼┼██┼┼█▀┼┼██┼┼┼┼██┼┼┼┼┼██┼
                        ███▄▄▄███┼┼┼─▀█▀┼┼─┼██▄▄▄┼██┼┼┼┼┼██▄""");
            System.exit(0);
        }
    }

    public static void clearScreen() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public static class ValidationException extends Exception {
        public ValidationException() {
            super();
        }
    }

}