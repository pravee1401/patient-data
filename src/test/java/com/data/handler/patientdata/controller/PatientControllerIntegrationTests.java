package com.data.handler.patientdata.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.data.handler.patientdata.Gender;
import com.data.handler.patientdata.model.Patient;
import com.data.handler.patientdata.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableJpaAuditing
public class PatientControllerIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PatientRepository patientRepository;

  @BeforeEach
  public void setup() {
    patientRepository.deleteAll();
  }

  @Test
  public void givenListOfPatients_whenGetAllPatientsOrderByLastNameAscending_thenReturnPatientsListSorted() throws Exception {
    // given - precondition or setup
    List<Patient> listOfPatients = new ArrayList<>();
    listOfPatients.add(Patient.builder()
        .firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20))
        .build());
    listOfPatients.add(Patient.builder()
        .firstName("Palki").lastName("Sharma").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build());
    listOfPatients.add(Patient.builder()
        .firstName("Christina").lastName("Schneider").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build());
    patientRepository.saveAll(listOfPatients);

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/api/v1/patients"));

    // then - verify the output
    response.andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.size()", is(listOfPatients.size())))
        .andExpect(jsonPath("$[*].lastName", containsInRelativeOrder("Kumar", "Schneider", "Sharma")));
  }

  @Test
  public void givenListOfPatients_whenGetAllFemalePatients_thenReturnFemalePatientsList() throws Exception {
    // given - precondition or setup
    List<Patient> listOfPatients = new ArrayList<>();
    listOfPatients.add(Patient.builder()
        .firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20))
        .build());
    listOfPatients.add(Patient.builder()
        .firstName("Palki").lastName("Sharma").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build());
    listOfPatients.add(Patient.builder()
        .firstName("Christina").lastName("Schneider").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build());
    patientRepository.saveAll(listOfPatients);

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/api/v1/patients/female"));

    // then - verify the output
    response.andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.size()", is(2)));

    Set<String> genderSet = new HashSet<>(JsonPath.read(response.andReturn().getResponse().getContentAsString(), "$[*].gender"));
    assertThat(genderSet.size()).isEqualTo(1);
    assertThat(genderSet.iterator().next()).isEqualTo(Gender.FEMALE.getGender());
  }

  @Test
  public void givenPatientId_whenGetPatientById_thenReturnPatientObject() throws Exception {
    // given - precondition or setup
    Patient patient = Patient.builder()
        .firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20))
        .build();
    patientRepository.save(patient);
    long savedPatientId = patientRepository.findAll().stream().findFirst().map(Patient::getId).get();

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/api/v1/patients/{id}", savedPatientId));

    // then - verify the output
    response.andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.firstName", is(patient.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(patient.getLastName())))
        .andExpect(jsonPath("$.gender", is(patient.getGender().name())))
        .andExpect(jsonPath("$.birthDay", is(patient.getBirthDay().toString())));
  }

  @Test
  public void givenInvalidPatientId_whenGetPatientById_thenReturnEmpty() throws Exception {
    // given - precondition or setup
    long nonExistentPatientId = 1L;

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/api/v1/patients/{id}", nonExistentPatientId));

    // then - verify the output
    response.andExpect(status().isNotFound())
        .andDo(print())
        .andExpect(jsonPath("$.messages", is(List.of(String.format("Patient with Id %s not found", nonExistentPatientId)))));
  }

  @Test
  public void givenPatientFirstAndLastName_whenGetPatientByFirstAndLastName_thenReturnPatientObjects() throws Exception {
    // given - precondition or setup
    Patient patient = Patient.builder()
        .firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20))
        .build();
    patientRepository.save(patient);

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/api/v1/patients/byName")
        .param("firstName", "Praveen")
        .param("lastName", "Kumar"));

    // then - verify the output
    response.andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$[0].firstName", is(patient.getFirstName())))
        .andExpect(jsonPath("$[0].lastName", is(patient.getLastName())))
        .andExpect(jsonPath("$[0].gender", is(patient.getGender().name())))
        .andExpect(jsonPath("$[0].birthDay", is(patient.getBirthDay().toString())));
  }

  @Test
  public void givenPatientObject_whenCreatePatient_thenReturnSavedPatient() throws Exception {

    // given - precondition or setup
    Patient patient = Patient.builder()
        .firstName("Christina").lastName("Schneider").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build();

    // when - action or behaviour that we are going test
    ResultActions response = mockMvc.perform(post("/api/v1/patients")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(patient)));

    // then - verify the result or output using assert statements
    response.andDo(print()).
        andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName", is(patient.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(patient.getLastName())))
        .andExpect(jsonPath("$.gender", is(patient.getGender().name())))
        .andExpect(jsonPath("$.birthDay", is(patient.getBirthDay().toString())));
  }

  @Test
  public void givenPatientId_whenDeletePatient_thenReturn200() throws Exception {
    // given - precondition or setup
    Patient patient = Patient.builder()
        .firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20))
        .build();
    patientRepository.save(patient);
    long savedPatientId = patientRepository.findAll().stream().findFirst().map(Patient::getId).get();

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(delete("/api/v1/patients/{id}", savedPatientId));

    // then - verify the output
    response.andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", is("Patient deleted successfully")));
  }

  @Test
  public void givenInvalidPatientId_whenDeletePatient_thenReturnNotFound() throws Exception {
    // given - precondition or setup
    long nonExistentPatientId = 1L;

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(delete("/api/v1/patients/{id}", nonExistentPatientId));

    // then - verify the output
    response.andExpect(status().isNotFound())
        .andDo(print())
        .andExpect(jsonPath("$.messages", is(List.of(String.format("Patient with Id %s not found", nonExistentPatientId)))));
  }

}
