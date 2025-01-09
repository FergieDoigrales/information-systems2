package com.fergie.lab1.util;

import com.fergie.lab1.models.Location;

public class LocationValidator {

    public static boolean validateLocation(Location location) {
        if (location.getX() == null) {
            System.out.println("Name cannot be null");
            return false;
        }
        return true;
    }
}
