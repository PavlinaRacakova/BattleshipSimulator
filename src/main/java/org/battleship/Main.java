package org.battleship;

import static org.battleship.BattleshipPlacementValidator.ValidationResult.SHIP_PLACED_INCORRECTLY;
import static org.battleship.BattleshipPlacementValidator.ValidationResult.VALID;
import static org.battleship.BattleshipPlacementValidator.hasValidSize;
import static org.battleship.BattleshipPlacementValidator.isPlacedOutsideOtherShips;
import static org.battleship.BattleshipPlacementValidator.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //necessary variables
        Scanner sc = new Scanner(System.in);
        String usersInput;
        int invalidInputCounter = 0;
        ValidationResult validationResult = SHIP_PLACED_INCORRECTLY;
        List<String> coordinates;
        List<String> coordinatesGuessedByUser = new ArrayList<>();
        List<String> namesOfBattleships = new ArrayList<>(Arrays.asList(
                "the Aircraft Carrier", "the Cruiser", "the Submarine", "the Destroyer"));
        List<Integer> lengthsOfBattleships = new ArrayList<>(Arrays.asList(5, 3, 3, 2));
        List<Battleship> battleships = new ArrayList<>();

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
                {" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "}
        };

        printWelcomeText();

        //placement of battleships in the battlefield
        for (int i = 0; i < 4; i++) {
            while (true) {
                try {
                    System.out.printf("Enter the coordinates of %s (%d cells) divided by whitespace:\n(e.g. for a ship that requires 3 cells: E1 E2 E3)\n",
                            namesOfBattleships.get(i), lengthsOfBattleships.get(i));
                    usersInput = sc.nextLine().toUpperCase().trim();
                    coordinates = new ArrayList<>(List.of(usersInput.split("\\s")));

                    //checks if given coordinates are in bounds and consist only of allowed characters
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

                    //if coordinates are correct, this part creates new battleship, stores it and terminates the while loop
                    battleships.add(new Battleship(namesOfBattleships.get(i), coordinates, battlefield));
                    printBattlefield(battlefield);
                    break;
                } catch (ValidationException e) {
                    System.out.println("Something is wrong: " + validationResult + ". Please try again.");
                } catch (Exception e) {
                    System.out.println("These coordinates do not exist. Please try again.");
                }
            }
        }

        clearBattlefield(battlefield, battleships);
        clearScreen();
        printGameStartsText();

        //main game loop
        while (!battleships.isEmpty()) {

            usersInput = getCoordinatesFromUser(sc);

            if (coordinatesGuessedByUser.contains(usersInput)) {
                System.out.println("You have already used this coordinate.");
                continue;
            }

            coordinatesGuessedByUser.add(usersInput);

            boolean nothingWasHit = checkIfAnyShipWasHit(battleships, usersInput, battlefield);

            battleships.removeIf(ship -> !ship.isNotSunk());

            if (nothingWasHit) {
                battlefield[usersInput.charAt(0) - 'A' + 1][Integer.parseInt(usersInput.substring(1))] = "M";
                invalidInputCounter++;
                System.out.printf("You missed!\nMissed shots: %d/25\n", invalidInputCounter);
            }

            checkIfUserHasLost(invalidInputCounter);
            printBattlefield(battlefield);
        } //end of main game loop

        printCongratulation();
        sc.close();
    }


    public static String getCoordinatesFromUser(Scanner sc) {
        while (true) {
            try {
                System.out.println("Take a shot:");
                String usersInput = sc.nextLine().toUpperCase().trim();
                if (!(usersInput.matches("^[A-J][1-9]$") || usersInput.matches("^[A-J]10$"))) {
                    throw new Exception();
                }
                return usersInput;
            } catch (Exception e) {
                System.out.println("The coordinate was entered incorrectly. Please try again.");
            }
        }
    }

    public static boolean checkIfAnyShipWasHit(List<Battleship> battleships, String usersInput, String[][] battlefield) {
        for (Battleship ship : battleships) {
            if (ship.isItAHit(usersInput, battlefield)) {
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
                return false;
            }
        }
        return true;
    }

    public static void checkIfUserHasLost(int invalidInputCounter) {
        if (invalidInputCounter >= 25) {
            System.out.println("""
                                        
                    Too many attempts! You are out of artillery.
                                        
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

    public static void printBattlefield(String[][] battlefield) {
        System.out.println();
        for (String[] strings : battlefield) {
            for (String string : strings) {
                System.out.print(string);
            }
            System.out.println();
        }
    }

    public static void printWelcomeText() {
        System.out.println("""
                                __/___           \s
                          _____/______|          \s
                  _______/_____\\_______\\_____    \s
                  \\              < < <       |   \s
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                   
                WELCOME TO THE BATTLESHIP SIMULATOR

                The task of the first user is to enter the coordinates of the battleships.
                The second users task is to destroy all the ships before he/she runs out of ammunition.
                """);
    }

    public static void printGameStartsText() {
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
                ⠀⠈⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀
                                
                THE GAME STARTS
                                
                                
                """);
    }

    public static void printCongratulation() {
        System.out.println("""
                                
                Congratulations! You destroyed all the ships!
                                                   
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

    public static void clearBattlefield(String[][] battlefield, List<Battleship> battleships) {
        for (Battleship ship : battleships) {
            ship.getCoordinates().forEach(coordinate ->
                    battlefield[coordinate.charAt(0) - 'A' + 1][Integer.parseInt(coordinate.substring(1))] = "~");
        }
    }

    public static class ValidationException extends Exception {
        public ValidationException() {
            super();
        }
    }

}