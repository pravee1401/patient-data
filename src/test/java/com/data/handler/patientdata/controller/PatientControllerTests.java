package com.data.handler.patientdata.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
import com.data.handler.patientdata.service.PatientService;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest
public class PatientControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PatientService patientService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void givenListOfPatients_whenGetAllPatientsOrderByLastNameAscending_thenReturnPatientsListSorted() throws Exception {
    // given - precondition or setup
    List<Patient> listOfPatients = new ArrayList<>();
    listOfPatients.add(Patient.builder()
        .id(1l).firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20))
        .build());
    listOfPatients.add(Patient.builder()
        .id(2l).firstName("Palki").lastName("Sharma").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build());
    listOfPatients.add(Patient.builder()
        .id(3l).firstName("Christina").lastName("Schneider").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build());
    listOfPatients = listOfPatients.stream()
        .sorted(Comparator.comparing(Patient::getLastName)).collect(Collectors.toList());
    given(patientService.getAllPatientsOrderByLastNameAsc()).willReturn(Optional.of(listOfPatients));

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/api/v1/patients"));

    // then - verify the output
    response.andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.size()", is(listOfPatients.size())))
        .andExpect(jsonPath("$[*].lastName", containsInRelativeOrder("Kumar","Schneider","Sharma")));
  }


  @Test
  public void givenListOfPatients_whenGetAllFemalePatients_thenReturnFemalePatientsList() throws Exception {
    // given - precondition or setup
    List<Patient> listOfPatients = new ArrayList<>();
    listOfPatients.add(Patient.builder()
        .id(2l).firstName("Palki").lastName("Sharma").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build());
    listOfPatients.add(Patient.builder()
        .id(3l).firstName("Christina").lastName("Schneider").gender(Gender.FEMALE).birthDay(LocalDate.now().minusYears(20))
        .build());
    given(patientService.getListOfFemalePatients()).willReturn(Optional.of(listOfPatients));

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/api/v1/patients/female"));

    // then - verify the output
    response.andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.size()", is(listOfPatients.size())));

    Set<String> genderSet = new HashSet<>(JsonPath.read(response.andReturn().getResponse().getContentAsString(), "$[*].gender"));
    assertThat(genderSet.size()).isEqualTo(1);
    assertThat(genderSet.iterator().next()).isEqualTo(Gender.FEMALE.getGender());
  }

  @Test
  public void givenPatientId_whenGetPatientById_thenReturnPatientObject() throws Exception {
    // given - precondition or setup
    long patientId = 1L;
    Patient patient = Patient.builder()
        .id(patientId).firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20))
        .build();
    given(patientService.findPatientById(patientId)).willReturn(Optional.of(patient));

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/api/v1/patients/{id}", patientId));

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
    long patientId = 1L;
    given(patientService.findPatientById(patientId)).willReturn(Optional.empty());

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/api/v1/patients/{id}", patientId));

    // then - verify the output
    response.andExpect(status().isNotFound())
        .andDo(print())
        .andExpect(jsonPath("$.messages", is(List.of("Patient with Id 1 not found"))));
  }

  @Test
  public void givenPatientFirstAndLastName_whenGetPatientByFirstAndLastName_thenReturnPatientObjects() throws Exception {
    // given - precondition or setup
    long patientId = 1L;
    Patient patient = Patient.builder()
        .id(1l).firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20))
        .build();
    given(patientService.findPatientByFirstNameAndLastName("Praveen", "Kumar")).willReturn(Optional.of(List.of(patient)));

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
        .id(1l).firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20))
        .build();
    given(patientService.createPatient(any(Patient.class)))
        .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(post("/api/v1/patients")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(patient)));

    // then - verify the output
    response.andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName",
            is(patient.getFirstName())))
        .andExpect(jsonPath("$.lastName",
            is(patient.getLastName())))
        .andExpect(jsonPath("$.gender",
            is(patient.getGender().name())))
        .andExpect(jsonPath("$.birthDay",
            is(patient.getBirthDay().toString())));
  }

  @Test
  public void givenPatientId_whenDeletePatient_thenReturn200() throws Exception {
    // given - precondition or setup
    long patientId = 1L;
    Patient patient = Patient.builder()
        .id(patientId).firstName("Praveen").lastName("Kumar").gender(Gender.MALE).birthDay(LocalDate.now().minusYears(20))
        .build();
    given(patientService.findPatientById(patientId)).willReturn(Optional.of(patient));
    willDoNothing().given(patientService).deletePatient(any(Patient.class));

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(delete("/api/v1/patients/{id}", patientId));

    // then - verify the output
    response.andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$", is("Patient deleted successfully")));
  }

  @Test
  public void givenInvalidPatientId_whenDeletePatient_thenReturnNotFound() throws Exception {
    // given - precondition or setup
    long patientId = 1L;
    given(patientService.findPatientById(patientId)).willReturn(Optional.empty());
    willDoNothing().given(patientService).deletePatient(any(Patient.class));

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(delete("/api/v1/patients/{id}", patientId));

    // then - verify the output
    response.andExpect(status().isNotFound())
        .andDo(print())
        .andExpect(jsonPath("$.messages", is(List.of("Patient with Id 1 not found"))));
  }
}
