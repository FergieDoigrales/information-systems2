package com.fergie.lab1.util;

import com.fergie.lab1.models.LocationDetails;

public class LocationDetailsValidator {

    public static boolean isValidLocationDetails(LocationDetails locationDetails) {
        return locationDetails.getAddress() != null;
    }
}
