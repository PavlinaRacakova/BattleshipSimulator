package org.battleship;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class BattleshipTest {

    String[][] testingBattlefield = {
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

    @Test
    void Should_ReturnFalse_When_TheShipIsNotOnGivenCoordinate() {
        //arrange
        Battleship battleship = new Battleship("Testing Ship", List.of("E1", "E2"), testingBattlefield);
        String givenCoordinate = "A3";
        boolean actualResult;
        //act
        actualResult = battleship.isItAHit(givenCoordinate, testingBattlefield);
        //assert
        Assertions.assertFalse(actualResult);
    }

    @Test
    void Should_ReturnTrue_When_TheShipIsOnGivenCoordinate() {
        //arrange
        List<String> testingCoordinatesOfShip = new ArrayList<>();
        testingCoordinatesOfShip.add("E1");
        testingCoordinatesOfShip.add("E2");
        Battleship battleship = new Battleship("Testing Ship", testingCoordinatesOfShip, testingBattlefield);
        String givenCoordinate = "E2";
        boolean actualResult;
        //act
        actualResult = battleship.isItAHit(givenCoordinate, testingBattlefield);
        //assert
        Assertions.assertTrue(actualResult);
    }

    @Test
    void Should_LowerLifeOfShipByOne_When_TheShipIsOnGivenCoordinate() {
        //arrange
        List<String> testingCoordinatesOfShip = new ArrayList<>();
        testingCoordinatesOfShip.add("E1");
        testingCoordinatesOfShip.add("E2");
        Battleship battleship = new Battleship("Testing Ship", testingCoordinatesOfShip, testingBattlefield);
        String givenCoordinate = "E2";
        int expectedResult = 1;
        //act
        battleship.isItAHit(givenCoordinate, testingBattlefield);
        int actualResult = battleship.life;
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_RemoveCoordinateFromList_When_TheShipIsOnGivenCoordinate() {
        //arrange
        List<String> testingCoordinatesOfShip = new ArrayList<>();
        testingCoordinatesOfShip.add("E1");
        testingCoordinatesOfShip.add("E2");
        Battleship battleship = new Battleship("Testing Ship", testingCoordinatesOfShip, testingBattlefield);
        String givenCoordinate = "E2";
        int expectedResult = 1;
        //act
        battleship.isItAHit(givenCoordinate, testingBattlefield);
        int actualResult = testingCoordinatesOfShip.size();
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnTrue_When_TheShipIsNotSunk() {
        //arrange
        Battleship battleship = new Battleship("Testing Ship", List.of("E1", "E2"), testingBattlefield);
        boolean actualResult;
        //act
        actualResult = battleship.isNotSunk();
        //assert
        Assertions.assertTrue(actualResult);
    }

    @Test
    void Should_ReturnFalse_When_TheShipIsAlreadySunk() {
        //arrange
        List<String> testingCoordinatesOfShip = new ArrayList<>();
        testingCoordinatesOfShip.add("E1");
        testingCoordinatesOfShip.add("E2");
        Battleship battleship = new Battleship("Testing Ship", testingCoordinatesOfShip, testingBattlefield);
        String givenCoordinate = "E1";
        String secondGivenCoordinate = "E2";
        boolean actualResult;
        //act
        battleship.isItAHit(givenCoordinate, testingBattlefield);
        battleship.isItAHit(secondGivenCoordinate, testingBattlefield);
        actualResult = battleship.isNotSunk();
        //assert
        Assertions.assertFalse(actualResult);
    }

}
