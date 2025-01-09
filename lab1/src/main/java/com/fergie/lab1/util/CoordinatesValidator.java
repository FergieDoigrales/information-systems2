package com.fergie.lab1.util;

import com.fergie.lab1.models.Coordinates;

public class CoordinatesValidator {

    public static boolean validateCoordinates(Coordinates coordinates) {
        if (coordinates.getX() == null) {
            System.out.println("Name cannot be null");
            return false;
        }
        if (coordinates.getY() == null) {
            System.out.println("Coordinates cannot be null");
            return false;
        }
        return true;
    }

}
