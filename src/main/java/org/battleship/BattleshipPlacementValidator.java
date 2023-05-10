package org.battleship;

import io.vavr.Function3;

import java.util.List;

import static org.battleship.BattleshipPlacementValidator.*;
import static org.battleship.BattleshipPlacementValidator.ValidationResult.*;


public interface BattleshipPlacementValidator
        extends Function3<List<String>, Integer, String[][], ValidationResult> {

    enum ValidationResult {
        VALID,
        INVALID_SIZE_OF_SHIP,
        SHIP_PLACED_INCORRECTLY,
        SHIP_PLACED_OVER_OTHER_SHIP,
        SHIP_PLACED_TOO_CLOSE_TO_OTHER_SHIP
    }

    /**
     * checks if given size of the ship matches required size
     *
     * @return VALID or INVALID_SIZE_OF_SHIP
     */
    static BattleshipPlacementValidator hasValidSize() {
        return (givenPositions, requiredSize, battlePlace) -> givenPositions.size() == requiredSize ?
                VALID : INVALID_SIZE_OF_SHIP;
    }

    /**
     * checks if the given coordinates are valid for vertical placement
     *
     * @return VALID or SHIP_PLACED_INCORRECTLY
     */
    static BattleshipPlacementValidator isVerticallyCorrect() {
        return (givenPositions, requiredLength, battlePlace) -> {
            for (int i = 0; i < givenPositions.size() - 1; i++) {
                //checks if letter is increasing by one
                if (!(Math.abs(givenPositions.get(i).charAt(0) - givenPositions.get(i + 1).charAt(0)) == 1)) {
                    return SHIP_PLACED_INCORRECTLY;
                }
                //checks if number is same all the time
                if (!(Integer.parseInt(givenPositions.get(i).substring(1)) - Integer.parseInt(givenPositions.get(i + 1).substring(1)) == 0)) {
                    return SHIP_PLACED_INCORRECTLY;
                }
            }
            return VALID;
        };
    }

    /**
     * checks if the given coordinates are valid for horizontal placement
     *
     * @return VALID or SHIP_PLACED_INCORRECTLY
     */
    static BattleshipPlacementValidator isHorizontallyCorrect() {
        return (givenPositions, requiredLength, battlePlace) -> {
            for (int i = 0; i < givenPositions.size() - 1; i++) {
                //checks if letter is same all the time
                if (!(Math.abs(givenPositions.get(i).charAt(0) - givenPositions.get(i + 1).charAt(0)) == 0)) {
                    return SHIP_PLACED_INCORRECTLY;
                }
                //checks if number is increasing by one
                if (!(Integer.parseInt(givenPositions.get(i).substring(1)) - Integer.parseInt(givenPositions.get(i + 1).substring(1)) == -1)) {
                    return SHIP_PLACED_INCORRECTLY;
                }
            }
            return VALID;
        };
    }

    /**
     * wrapping method that checks if the ship can be successfully placed horizontally or vertically to the battlefield
     * using isHorizontallyCorrect and isVerticallyCorrect methods
     *
     * @return VALID or SHIP_PLACED_INCORRECTLY
     */
    static BattleshipPlacementValidator canBePlacedToTheField() {
        return (givenPositions, requiredLength, battlePlace) -> {
            if (isHorizontallyCorrect().apply(givenPositions, requiredLength, battlePlace) == VALID ||
                    isVerticallyCorrect().apply(givenPositions, requiredLength, battlePlace) == VALID) {
                return VALID;
            }
            return SHIP_PLACED_INCORRECTLY;
        };
    }

    /**
     * checks if the coordinates do not replace another already placed ship
     *
     * @return VALID or SHIP_PLACED_OVER_ANOTHER_SHIP
     */
    static BattleshipPlacementValidator isPlacedOutsideOtherShips() {
        return (givenPositions, requiredLength, battlePlace) -> {
            for (String position : givenPositions) {
                int row = position.charAt(0) - 'A' + 1;
                int column = Integer.parseInt(position.substring(1));
                if (battlePlace[row][column].equals("O")) {
                    return SHIP_PLACED_OVER_OTHER_SHIP;
                }
            }
            return VALID;
        };
    }

    /**
     * checks if the coordinates do not touch existing ship
     *
     * @return VALID or SHIP_PLACED_TOO_CLOSE_TO_OTHER_SHIP
     */
    static BattleshipPlacementValidator isNotTooCloseToOtherShips() {
        return (givenPositions, requiredLength, battlePlace) -> {
            for (String position : givenPositions) {
                int row = position.charAt(0) - 'A' + 1;
                int column = Integer.parseInt(position.substring(1));
                if (battlePlace[row + 1][column].equals("O") ||
                        battlePlace[row - 1][column].equals("O") ||
                        battlePlace[row][column + 1].equals("O") ||
                        battlePlace[row][column - 1].equals("O")) {
                    return SHIP_PLACED_TOO_CLOSE_TO_OTHER_SHIP;
                }
            }
            return VALID;
        };
    }

    default BattleshipPlacementValidator and(BattleshipPlacementValidator other) {
        return (givenPositions, requiredLength, battlePlace) -> {
            ValidationResult result = this.apply(givenPositions, requiredLength, battlePlace);
            return result.equals(VALID) ? other.apply(givenPositions, requiredLength, battlePlace) : result;
        };
    }
}

