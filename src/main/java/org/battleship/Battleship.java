package org.battleship;

import java.util.List;

public class Battleship {

    String name;
    List<String> coordinates;
    int life;

    public Battleship(String name, List<String> coordinates, String[][] battlefield) {
        this.name = name;
        this.coordinates = coordinates;
        life = coordinates.size();
        //adds itself on the battlefield
        for (String coordinate : coordinates) {
            battlefield[coordinate.charAt(0) - 'A' + 1][Integer.parseInt(coordinate.substring(1))] = "O";
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getCoordinates() {
        return coordinates;
    }

    /**
     * this method checks if the given coordinate hits the ship. If so, it will lesser ships life and change the value in battlefield
     * @param coordinate
     * @param battlefield
     * @return boolean
     */
    public boolean isItAHit(String coordinate, String[][] battlefield) {
        if (!coordinates.contains(coordinate)) {
            return false;
        } else {
            life--;
            battlefield[coordinate.charAt(0) - 'A' + 1][Integer.parseInt(coordinate.substring(1))] = "X";
            coordinates.remove(coordinate);
            return true;
        }
    }

    public boolean isNotSunk() {
        return life > 0;
    }
}
