package com.data.handler.patientdata.service;

import com.data.handler.patientdata.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientService {

  Optional<List<Patient>> getAllPatientsOrderByLastNameAsc();

  Optional<Patient> findPatientById(Long id);

  Optional<List<Patient>> findPatientByFirstNameAndLastName(String firstName, String lastName);

  Patient createPatient(Patient patient);

  Optional<List<Patient>> getListOfFemalePatients();

  void deletePatient(Patient patient);

}
