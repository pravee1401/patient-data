package com.data.handler.patientdata.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import com.data.handler.patientdata.Gender;
import com.data.handler.patientdata.model.Patient;
import com.data.handler.patientdata.util.PatientUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PatientRepositoryTests {

  @Autowired
  private PatientRepository patientRepository;

  private Patient patient;

  @BeforeEach
  public void setup() {
    patient = Patient.builder()
        .firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20))
        .build();
  }

  @DisplayName("JUnit test for get all patients operation")
  @Test
  public void givenPatientsList_whenFindAll_thenPatientsList() {

    // given - precondition or setup
    Patient patient2 = Patient.builder()
        .firstName("Palki").lastName("Sharma").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build();
    Patient patient3 = Patient.builder()
        .firstName("Christina").lastName("Schneider").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build();

    patientRepository.save(patient2);
    patientRepository.save(patient3);
    patientRepository.save(patient);

    // when -  action or the behaviour that we are going test
    Optional<List<Patient>> patientList = patientRepository.findAllByOrderByLastNameAsc();

    // then - verify the output
    assertThat(patientList).isNotEmpty();
    assertThat(patientList.get().size()).isEqualTo(3);

    List<String> lastNamesFetchedFromDB = patientList.get().stream().map(Patient::getLastName).collect(Collectors.toList());
    org.hamcrest.MatcherAssert.assertThat("List equality with order",
        lastNamesFetchedFromDB, containsInRelativeOrder("Kumar","Schneider","Sharma"));

  }

  @DisplayName("JUnit test for get all patients operation")
  @Test
  public void givenPatientsList_whenFindAllFemalePatients_thenReturnFemalePatientsList() {

    // given - precondition or setup
    Patient patient1 = Patient.builder()
        .firstName("Sameera").lastName("Key").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build();
    Patient patient2 = Patient.builder()
        .firstName("Julian").lastName("Adamson").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(25))
        .build();

    patientRepository.save(patient);
    patientRepository.save(patient1);
    patientRepository.save(patient2);

    // when -  action or the behaviour that we are going test
    Optional<List<Patient>> patientList = patientRepository.findAllByGenderOrderByLastNameAsc(Gender.FEMALE);

    // then - verify the output
    assertThat(patientList).isNotEmpty();
    assertThat(patientList.get().size()).isEqualTo(2);

  }

  @DisplayName("JUnit test for get patient by id operation")
  @Test
  public void givenPatientObject_whenFindById_thenReturnPatientObject() {
    // given - precondition or setup
    patientRepository.save(patient);

    // when -  action or the behaviour that we are going test
    List<Patient> patients = patientRepository.findAll();
    Optional<Patient> patientReturned = patientRepository.findById(patients.get(0).getId());

    // then - verify the output
    assertThat(patientReturned).isNotEmpty();
    assertThat(patientReturned.get().getFirstName()).isEqualTo("Praveen");
  }

  @DisplayName("JUnit test for get patient by first and last name")
  @Test
  public void givenPatientFirstAndLastName_whenFindByFirstAndLastName_thenReturnPatientObject() {
    // given - precondition or setup
    patientRepository.save(patient);

    // when -  action or the behaviour that we are going test
    Optional<List<Patient>> patientReturned = patientRepository.findAllByFirstNameAndLastNameOrderByLastNameAsc("Praveen", "Kumar");

    // then - verify the output
    assertThat(patientReturned).isNotEmpty();
    assertThat(patientReturned.get().get(0).getFirstName()).isEqualTo("Praveen");
    assertThat(patientReturned.get().get(0).getLastName()).isEqualTo("Kumar");
  }

  @DisplayName("JUnit test for save patient operation")
  @Test
  public void givenPatientObject_whenSave_thenReturnSavedPatient() {

    //given - precondition or setup

    // when - action or the behaviour that we are going test
    Patient savedPatient = patientRepository.save(patient);

    // then - verify the output
    assertThat(savedPatient).isNotNull();
    assertThat(savedPatient.getId()).isGreaterThan(0);
    assertThat(savedPatient.getFirstName()).isEqualTo("Praveen");
  }

  @DisplayName("JUnit test for delete patient operation")
  @Test
  public void givenPatientObject_whenDelete_thenRemovePatient() {
    // given - precondition or setup
    patientRepository.save(patient);

    // when -  action or the behaviour that we are going test
    patientRepository.delete(patient);
    Optional<Patient> patientOptional = patientRepository.findById(patient.getId());

    // then - verify the output
    assertThat(patientOptional).isEmpty();
  }

  @DisplayName("JUnit test for deleting records created older than an year before")
  @Test
  public void givenPatientList_whenDeleteRecordsCreatedOlderThanOneYear_thenDelete() {

    // given - precondition or setup
    try (MockedStatic<PatientUtil> mockedStatic = mockStatic(PatientUtil.class)) {
      //Patient record created older than an year before
      mockedStatic
          .when(PatientUtil::getCurrentDate).thenReturn(LocalDate.now().minusYears(2));
      assertEquals(PatientUtil.getCurrentDate(), LocalDate.now().minusYears(2));

      Patient patient1 = Patient.builder()
          .firstName("Julian").lastName("Adamson").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(25))
          .build();
      patientRepository.save(patient1);
    }

    Patient patient2 = Patient.builder()
        .firstName("Sameera").lastName("Key").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build();
    Patient patient3 = Patient.builder()
        .firstName("Jaydan").lastName("Smyth").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(25))
        .build();

    patientRepository.save(patient2);
    patientRepository.save(patient3);
    Optional<List<Patient>> patients = patientRepository.findAllByOrderByLastNameAsc();
    assertThat(patients).isNotEmpty();
    assertThat(patients.get().size()).isEqualTo(3);

    // when -  action or the behaviour that we are going test
    patientRepository.deleteAllByCreatedOnBefore(LocalDate.now().minusYears(1));

    // then - verify the output
    patients = patientRepository.findAllByOrderByLastNameAsc();
    assertThat(patients).isNotEmpty();
    assertThat(patients.get().size()).isEqualTo(2);  //One record which is created older than one year is deleted
  }


}
