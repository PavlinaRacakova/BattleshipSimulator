package org.battleship;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import org.battleship.BattleshipPlacementValidator.*;

import static org.battleship.BattleshipPlacementValidator.ValidationResult.*;

class BattleshipPlacementValidatorTest {

    final List<String> verticallyCorrectCoordinates = Arrays.asList("F1", "G1", "H1");
    final List<String> horizontallyCorrectCoordinates = Arrays.asList("B8", "B9", "B10");
    final List<String> absolutelyWrongCoordinates = Arrays.asList("A5", "G5", "H6");

    @Test
    void Should_ReturnVALID_When_NumberOfCoordinatesMatchesGivenNumber() {
        //arrange
        ValidationResult expectedResult = VALID;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.hasValidSize()
                .apply(verticallyCorrectCoordinates, 3, BattleshipTest.testingBattlefield);
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
                .apply(verticallyCorrectCoordinates, 5, BattleshipTest.testingBattlefield);
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
                .apply(verticallyCorrectCoordinates, 3, BattleshipTest.testingBattlefield);
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
                .apply(horizontallyCorrectCoordinates, 3, BattleshipTest.testingBattlefield);
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
                .apply(absolutelyWrongCoordinates, 3, BattleshipTest.testingBattlefield);
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
                .apply(horizontallyCorrectCoordinates, 3, BattleshipTest.testingBattlefield);
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
                .apply(verticallyCorrectCoordinates, 3, BattleshipTest.testingBattlefield);
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
                .apply(absolutelyWrongCoordinates, 3, BattleshipTest.testingBattlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnVALID_When_CoordinatesAreValidForEitherHorizontalOrVerticalPlacement() {
        //arrange
        ValidationResult expectedResult = VALID;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.canBePlacedToTheField()
                .apply(horizontallyCorrectCoordinates, 3, BattleshipTest.testingBattlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnSHIP_PLACED_INCORRECTLY_When_CoordinatesAreNotVerticallyNorHorizontallyCorrect() {
        //arrange
        ValidationResult expectedResult = SHIP_PLACED_INCORRECTLY;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.canBePlacedToTheField()
                .apply(absolutelyWrongCoordinates, 3, BattleshipTest.testingBattlefield);
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
                .apply(horizontallyCorrectCoordinates, 3, BattleshipTest.testingBattlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnSHIP_PLACED_OVER_ANOTHER_SHIP_When_CoordinatesOverlapExistingShip() {
        //arrange
        ValidationResult expectedResult = SHIP_PLACED_OVER_OTHER_SHIP;
        List<String> coordinatesOverShip = List.of("I7", "I8", "I9");
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.isPlacedOutsideOtherShips()
                .apply(coordinatesOverShip, 3, BattleshipTest.testingBattlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnVALID_When_CoordinatesDontTouchExistingShip() {
        //arrange
        ValidationResult expectedResult = VALID;
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.isNotTooCloseToOtherShips()
                .apply(verticallyCorrectCoordinates, 3, BattleshipTest.testingBattlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void Should_ReturnSHIP_PLACED_TOO_CLOSE_TO_OTHER_SHIP_When_CoordinatesTouchExistingShip() {
        //arrange
        ValidationResult expectedResult = SHIP_PLACED_TOO_CLOSE_TO_OTHER_SHIP;
        List<String> coordinatesOverShip = List.of("F8", "G8", "H8");
        ValidationResult actualResult;
        //act
        actualResult = BattleshipPlacementValidator.isNotTooCloseToOtherShips()
                .apply(coordinatesOverShip, 3, BattleshipTest.testingBattlefield);
        //assert
        Assertions.assertEquals(expectedResult, actualResult);
    }
}