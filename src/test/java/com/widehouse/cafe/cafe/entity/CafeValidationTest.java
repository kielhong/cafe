package com.widehouse.cafe.cafe.entity;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created by kiel on 2017. 2. 19..
 */
class CafeValidationTest {
    private static Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void cafeUrl_withAlphaNumericUnderscore_thenValidate() {
        Cafe cafe = new Cafe("a1234_test_url", "test name");

        Set<ConstraintViolation<Cafe>> violations = validator.validate(cafe);

        then(violations.isEmpty()).isTrue();
    }

    @Test
    void cafeUrl_withSpace_thenInvalid() {
        Cafe cafe = new Cafe("test url", "test name");

        Set<ConstraintViolation<Cafe>> violations = validator.validate(cafe);

        then(violations.isEmpty()).isFalse();
    }

    @Test
    void cafeUrl_withAllNumeric_thenViolated() {
        Cafe cafe = new Cafe("123456", "test name");

        Set<ConstraintViolation<Cafe>> violations = validator.validate(cafe);

        then(violations.isEmpty()).isFalse();
    }

    @Test
    void cafeUrl_withNumericBeginingUrl_thenViolated() {
        Cafe cafe = new Cafe("1asdf", "test name");

        Set<ConstraintViolation<Cafe>> violations = validator.validate(cafe);

        then(violations.isEmpty()).isFalse();
    }
}
