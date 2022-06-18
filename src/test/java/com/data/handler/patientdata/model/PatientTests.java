package com.data.handler.patientdata.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.assertj.core.api.Assertions.assertThat;

import com.data.handler.patientdata.Gender;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PatientTests {

  private static Validator validator;

  @BeforeAll
  public static void initInstance() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void givenPatientNameValid_NoExceptionRaised() {
    Patient patient = Patient.builder()
        .id(1l).firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20)).build();
    Set<ConstraintViolation<Patient>> constraintViolations = validator.validate(patient);

    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void givenPatientFirstNameNotProvided_ExceptionRaised() {
    Patient patient = Patient.builder()
        .id(1l).lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20)).build();
    Set<ConstraintViolation<Patient>> constraintViolations = validator.validate(patient);

    assertEquals(1, constraintViolations.size());
    assertEquals("First Name is required field", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void givenPatientFirstNameSizeNotValid_ExceptionRaised() {
    Patient patient = Patient.builder()
        .id(1l).firstName("p").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20)).build();
    Set<ConstraintViolation<Patient>> constraintViolations = validator.validate(patient);

    assertEquals(1, constraintViolations.size());
    assertEquals("Size of First Name should be between 2 to 100 characters", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void givenPatientLastNameNotProvided_ExceptionRaised() {
    Patient patient = Patient.builder()
        .id(1l).firstName("Praveen").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20)).build();
    Set<ConstraintViolation<Patient>> constraintViolations = validator.validate(patient);

    assertEquals(1, constraintViolations.size());
    assertEquals("Last Name is required field", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void givenPatientLastNameSizeNotValid_ExceptionRaised() {
    Patient patient = Patient.builder()
        .id(1l).firstName("Praveen").lastName("K").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20)).build();
    Set<ConstraintViolation<Patient>> constraintViolations = validator.validate(patient);

    assertEquals(1, constraintViolations.size());
    assertEquals("Size of Last Name should be between 2 to 100 characters", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void givenPatientGenderNotProvided_ExceptionRaised() {
    Patient patient = Patient.builder()
        .id(1l).firstName("Praveen").lastName("Kumar").birthDay(LocalDate.now().minusYears(20)).build();
    Set<ConstraintViolation<Patient>> constraintViolations = validator.validate(patient);

    assertEquals(1, constraintViolations.size());
    assertEquals("Gender is required field", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void givenPatientDOBInvalid_ExceptionRaised() {
    Patient patient = Patient.builder()
        .id(1l).firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(17)).build();
    Set<ConstraintViolation<Patient>> constraintViolations = validator.validate(patient);

    assertEquals(1, constraintViolations.size());
    assertEquals("Patient age should be greater than 18", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void givenPatientDOBNotProvided_ExceptionRaised() {
    Patient patient = Patient.builder()
        .id(1l).firstName("Praveen").lastName("Kumar").gender(Gender.MALE).build();
    Set<ConstraintViolation<Patient>> constraintViolations = validator.validate(patient);

    assertEquals(2, constraintViolations.size());
    assertThat(constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()))
        .containsAll(List.of("Birthdate is required field", "Patient age should be greater than 18"));
  }

}
