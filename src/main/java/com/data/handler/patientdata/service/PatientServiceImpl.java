package com.data.handler.patientdata.service;

import com.data.handler.patientdata.Gender;
import com.data.handler.patientdata.model.Patient;
import com.data.handler.patientdata.repository.PatientRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

  private final PatientRepository patientRepository;

  @Override
  public Optional<List<Patient>> getAllPatientsOrderByLastNameAsc() {
    log.info("getAllPatients >> Fetching list of all patients started");
    Optional<List<Patient>> patients = patientRepository.findAllByOrderByLastNameAsc();
    log.info("getAllPatients << Fetching list of all patients completed");
    return patients;
  }

  @Override
  public Optional<List<Patient>> getListOfFemalePatients() {
    log.info("getListOfFemalePatients >> Fetching list of Female patients started");
    Optional<List<Patient>> patients = patientRepository.findAllByGenderOrderByLastNameAsc(Gender.FEMALE);
    log.info("getListOfFemalePatients << Fetching list of Female patients completed");
    return patients;
  }

  @Override
  public Optional<Patient> findPatientById(Long id) {
    log.info("findPatientById >> find patient by id {} started", id);
    Optional<Patient> patient = patientRepository.findById(id);
    log.info("findPatientById << find patient by id {} completed", id);
    return patient;
  }

  public Optional<List<Patient>> findPatientByFirstNameAndLastName(String firstName, String lastName) {
    log.info("findPatientByFirstNameAndLastName >> finding patient with firstName {} and lastName {} started", firstName, lastName);
    Optional<List<Patient>> patients = patientRepository.findAllByFirstNameAndLastNameOrderByLastNameAsc(firstName, lastName);
    log.info("findPatientByFirstNameAndLastName << finding patient with firstName {} and lastName {} completed", firstName, lastName);
    return patients;
  }

  @Override
  public Patient createPatient(Patient patient) {
    log.info("createPatient >> creating patient record for {}-{} started",patient.getFirstName(), patient.getLastName());
    Patient patientCreated = patientRepository.save(patient);
    log.info("createPatient << creating patient record for {}-{} completed",patient.getFirstName(), patient.getLastName());
    return patientCreated;
  }

  @Override
  public void deletePatient(Patient patient) {
    log.info("deletePatient >> deleting patient record for {}-{} started",patient.getFirstName(), patient.getLastName());
    patientRepository.delete(patient);
    log.info("deletePatient << deleting patient record completed");
  }


}
