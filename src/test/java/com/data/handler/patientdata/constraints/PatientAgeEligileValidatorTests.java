package com.data.handler.patientdata.constraints;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class PatientAgeEligileValidatorTests {

  @Test
  public void patientInvalidNameTest() {
    PatientAgeEligibleValidator validator = new PatientAgeEligibleValidator();
    assertTrue(validator.isValid(LocalDate.now().minusYears(20), null));
    assertFalse(validator.isValid(LocalDate.now().minusYears(17), null));
  }

}
