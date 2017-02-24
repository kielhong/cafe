package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Created by kiel on 2017. 2. 19..
 */
public class CafeValidationTest {
    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void AlphanumericunderscoreCafeUrl_Should_Validate() {
        Cafe cafe = new Cafe("a1234_test_url", "test name");
        Set<ConstraintViolation<Cafe>> violations = validator.validate(cafe);
        then(violations.isEmpty())
                .isTrue();
    }

    @Test
    public void SpaceCafeUrl_Should_Violated() {
        Cafe cafe = new Cafe("test url", "test name");
        Set<ConstraintViolation<Cafe>> violations = validator.validate(cafe);
        then(violations.isEmpty())
                .isFalse();
    }

    @Test
    public void NumericCafeUrl_Should_Violated() {
        Cafe cafe = new Cafe("123456", "test name");
        Set<ConstraintViolation<Cafe>> violations = validator.validate(cafe);
        then(violations.isEmpty())
                .isFalse();
    }

    @Test
    public void BeginWithNumericCafeUrl_Should_Violated() {
        Cafe cafe = new Cafe("1asdf", "test name");
        Set<ConstraintViolation<Cafe>> violations = validator.validate(cafe);
        then(violations.isEmpty())
                .isFalse();
    }
}