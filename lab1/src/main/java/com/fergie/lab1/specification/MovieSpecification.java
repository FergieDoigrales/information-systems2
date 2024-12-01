package com.fergie.lab1.specification;

import com.fergie.lab1.models.Movie;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MovieSpecification implements Specification<Movie> {

    private final String query;

    public MovieSpecification(String query) {
        this.query = query;
    }

    @Override
    public Predicate toPredicate(@NotNull Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (this.query.trim().isEmpty()) {
            return cb.conjunction();
        }
        String likeQuery = "%" + this.query.toLowerCase() + "%";
        String queryLower = this.query.trim().toLowerCase();

        List<Predicate> predicates = new ArrayList<>();

        if (isLong(this.query)) {
            predicates.add(cb.equal(root.get("movieId"), Long.valueOf(this.query)));
            predicates.add(cb.equal(root.get("authorID"), Long.valueOf(this.query)));
            predicates.add(cb.equal(root.get("length"), Long.valueOf(this.query)));
            predicates.add(cb.equal(root.get("coordinates").get("y"), Long.valueOf(this.query)));
        }
        if (isInteger(this.query)) {
            predicates.add(cb.equal(root.get("oscarsCount"), Integer.parseInt(this.query)));
            predicates.add(cb.equal(root.get("goldenPalmCount"), Integer.valueOf(this.query)));
            predicates.add(cb.equal(root.get("coordinates").get("x"), Integer.valueOf(this.query)));
        }
        if (isDouble(this.query)) {
            predicates.add(cb.equal(root.get("budget"), Double.valueOf(this.query)));
        }
        if (isFloat(this.query)) {
            predicates.add(cb.equal(root.get("totalBoxOffice"), Float.valueOf(this.query)));
        }

        predicates.add(cb.equal(cb.lower(root.get("name")), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("genre").as(String.class)), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("mpaaRating").as(String.class)), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("director").get("name")), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("operator").get("name")), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("screenwriter").get("name")), queryLower));

        predicates.add(cb.equal(cb.lower(root.get("director").get("eyeColor").as(String.class)), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("director").get("nationality").as(String.class)), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("director").get("hairColor").as(String.class)), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("director").get("passportID").as(String.class)), queryLower));

        predicates.add(cb.equal(cb.lower(root.get("operator").get("eyeColor").as(String.class)), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("operator").get("hairColor").as(String.class)), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("operator").get("nationality").as(String.class)), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("operator").get("passportID").as(String.class)), queryLower));

        predicates.add(cb.equal(cb.lower(root.get("screenwriter").get("eyeColor").as(String.class)), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("screenwriter").get("hairColor").as(String.class)), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("screenwriter").get("nationality").as(String.class)), queryLower));
        predicates.add(cb.equal(cb.lower(root.get("screenwriter").get("passportID").as(String.class)), queryLower));

        return cb.or(predicates.toArray(new Predicate[0]));

    }
    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException | ArithmeticException e) {
            return false;
        }
    }
    private boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException | ArithmeticException e) {
            return false;
        }

    }
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException | ArithmeticException e) {
            return false;
        }
    }

    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException | ArithmeticException e) {
            return false;
        }
    }


}
