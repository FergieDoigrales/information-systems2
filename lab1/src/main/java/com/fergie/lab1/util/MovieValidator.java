package com.fergie.lab1.util;


import com.fergie.lab1.models.Movie;

public class MovieValidator {

    public static boolean validateMovie(Movie movie) {
        if (movie.getName() == null || movie.getName().isEmpty()) {
            System.out.println("Name cannot be empty or null");
            return false;
        }

        if (movie.getCoordinates() == null) {
            System.out.println("Coordinates cannot be null");
            return false;
        }

        if (movie.getBudget() == null || movie.getBudget() <= 0) {
            System.out.println("Budget must be greater than 0");
            return false;
        }

        if (movie.getOscarsCount() <= 0) {
            System.out.println("Oscars count cannot be negative");
            return false;
        }

        if (movie.getTotalBoxOffice() == null || movie.getTotalBoxOffice() < 0) {
            System.out.println("TotalBoxOffice count cannot be negative or null");
            return false;
        }

        if (movie.getOperator() == null) {
            System.out.println("Operator cannot be null");
            return false;
        }

        if (movie.getLength() != null &&  movie.getLength() < 0) {
            System.out.println("Length cannot be negative");
            return false;
        }

        if (movie.getGoldenPalmCount() != null && movie.getGoldenPalmCount() <= 0) {
            System.out.println("GoldenPalmCount cannot be negative");
            return false;
        }

        return true;
    }
}
