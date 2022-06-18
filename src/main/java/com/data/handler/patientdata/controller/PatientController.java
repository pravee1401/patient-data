package com.data.handler.patientdata.controller;

import com.data.handler.patientdata.exception.ResourceNotFoundException;
import com.data.handler.patientdata.model.Patient;
import com.data.handler.patientdata.service.PatientService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1")
public class PatientController {

  private PatientService patientService;

  @GetMapping("/patients")
  public ResponseEntity<List<Patient>> getAllPatients() {
    Optional<List<Patient>> patients = patientService.getAllPatientsOrderByLastNameAsc();
    if (patients.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(patients.get(), HttpStatus.OK);
    }
  }

  @GetMapping("/patients/female")
  public ResponseEntity<List<Patient>> getFemalePatients() {
    Optional<List<Patient>> patients = patientService.getListOfFemalePatients();
    if (patients.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(patients.get(), HttpStatus.OK);
    }
  }

  @GetMapping("/patients/{id}")
  public ResponseEntity<Patient> getPatientById(@PathVariable("id") @Positive Long id){
    Patient patient = patientService.findPatientById(id)
        .orElseThrow(() -> new ResourceNotFoundException(String.format("Patient with Id %s not found", id)));
    return new ResponseEntity<>(patient, HttpStatus.OK);
  }

  @GetMapping("/patients/byName")
  public ResponseEntity<List<Patient>> getPatientsByFirstAndLastName(@RequestParam(name = "firstName") @NotBlank String firstName,
                                                                     @RequestParam(name = "lastName") @NotBlank String lastName) {
    Optional<List<Patient>> patients = patientService.findPatientByFirstNameAndLastName(firstName, lastName);
    if (patients.isEmpty()) {
      throw new ResourceNotFoundException(String.format("Patient with firstName %s and lastName %s not found", firstName, lastName));
    }else{
      return new ResponseEntity<>(patients.get(), HttpStatus.OK);
    }
  }

  @PostMapping("/patients")
  public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
    return new ResponseEntity<>(patientService.createPatient(patient), HttpStatus.CREATED);
  }

  @DeleteMapping("/patients/{id}")
  public ResponseEntity<String> deletePatient(@PathVariable("id") @Positive Long id) {
    Patient patient = patientService.findPatientById(id)
        .orElseThrow(() -> new ResourceNotFoundException(String.format("Patient with Id %s not found", id)));
    patientService.deletePatient(patient);

    return new ResponseEntity<>("Patient deleted successfully", HttpStatus.OK);
  }
}
