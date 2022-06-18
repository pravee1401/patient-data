package com.data.handler.patientdata.constraints;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PatientAgeEligibleValidator implements ConstraintValidator<PatientAgeEligible, LocalDate> {

  @Override
  public boolean isValid(LocalDate patientBirthDate, ConstraintValidatorContext constraintValidatorContext) {
    if(patientBirthDate == null){
      return false;
    }
    int patientsEligibleAge = 18;
    return ChronoUnit.YEARS.between(patientBirthDate, LocalDate.now()) >= patientsEligibleAge;
  }
}
