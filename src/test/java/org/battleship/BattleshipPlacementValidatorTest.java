package org.battleship;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.battleship.BattleshipPlacementValidator.*;

import static org.battleship.BattleshipPlacementValidator.ValidationResult.*;

class BattleshipPlacementValidatorTest {

    final String[][] battlefield = {
            {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"A", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"B", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"C", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"D", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"E", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"F", "~", "~", "~", "~", "~", "~", "~", "~", "O", "~"},
            {"G", "~", "~", "~", "~", "~", "~", "~", "~", "O", "~"},
            {"H", "~", "~", "~", "~", "~", "~", "~", "~", "O", "~"},
            {"I", "~", "~", "~", "~", "~", "~", "~", "~", "O", "~"},
            {"J", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"}
    };

    final List<String> verticallyCorrectCoordinates = List.of("F1", "G1", "H1");
    final List<String> horizontallyCorrectCoordinates = List.of("C8", "C9", "C10");
    final List<String> absolutelyWrongCoordinates = List.of("A5", "G5", "H6");

    @Test
    void Should_ReturnVALID_When_NumberOfCoordinatesMatchesGivenNumber() {
        //arrange
        ValidationResult expectedResult = VALID;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.hasValidSize()
                .apply(verticallyCorrectCoordinates, 3, battlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnINVALID_SIZE_OF_SHIP_When_NumberOfCoordinatesIsDifferentThanGivenNumber() {
        //arrange
        ValidationResult expectedResult = INVALID_SIZE_OF_SHIP;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.hasValidSize()
                .apply(verticallyCorrectCoordinates, 5, battlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }


    @Test
    void Should_ReturnVALID_When_CoordinatesAreValidForVerticalPlacement() {
        //arrange
        ValidationResult expectedResult = VALID;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.isVerticallyCorrect()
                .apply(verticallyCorrectCoordinates, 3, battlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnSHIP_PLACED_INCORRECTLY_When_CoordinatesAreValidForHorizontalPlacement() {
        //arrange
        ValidationResult expectedResult = SHIP_PLACED_INCORRECTLY;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.isVerticallyCorrect()
                .apply(horizontallyCorrectCoordinates, 3, battlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnSHIP_PLACED_INCORRECTLY_When_CoordinatesAreAbsolutelyInvalidToPlaceVertically() {
        //arrange
        ValidationResult expectedResult = SHIP_PLACED_INCORRECTLY;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.isVerticallyCorrect()
                .apply(absolutelyWrongCoordinates, 3, battlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnVALID_When_CoordinatesAreValidForHorizontalPlacement() {
        //arrange
        ValidationResult expectedResult = VALID;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.isHorizontallyCorrect()
                .apply(horizontallyCorrectCoordinates, 3, battlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnSHIP_PLACED_INCORRECTLY_When_CoordinatesAreValidForVerticalPlacement() {
        //arrange
        ValidationResult expectedResult = SHIP_PLACED_INCORRECTLY;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.isHorizontallyCorrect()
                .apply(verticallyCorrectCoordinates, 3, battlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnSHIP_PLACED_INCORRECTLY_When_CoordinatesAreAbsolutelyInvalidToPlaceHorizontally() {
        //arrange
        ValidationResult expectedResult = SHIP_PLACED_INCORRECTLY;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.isHorizontallyCorrect()
                .apply(absolutelyWrongCoordinates, 3, battlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnVALID_When_CoordinatesDontOverlapExistingShip() {
        //arrange
        ValidationResult expectedResult = VALID;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.isPlacedOutsideOtherShips()
                .apply(horizontallyCorrectCoordinates, 3, battlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnSHIP_PLACED_OVER_ANOTHER_SHIP_When_CoordinatesOverlapExistingShip() {
        //arrange
        ValidationResult expectedResult = SHIP_PLACED_OVER_ANOTHER_SHIP;
        List<String> coordinatesOverShip = List.of("I7", "I8", "I9");
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.isPlacedOutsideOtherShips()
                .apply(coordinatesOverShip, 3, battlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }
}