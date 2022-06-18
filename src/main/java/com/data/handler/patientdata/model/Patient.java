package com.data.handler.patientdata.model;

import com.data.handler.patientdata.Gender;
import com.data.handler.patientdata.constraints.PatientAgeEligible;
import com.data.handler.patientdata.util.PatientUtil;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @NotBlank(message = "First Name is required field")
  @Size(min = 2, max = 100, message = "Size of First Name should be between 2 to 100 characters")
  @Column(name = "first_name")
  private String firstName;

  @NotBlank(message = "Last Name is required field")
  @Size(min = 2, max = 100, message = "Size of Last Name should be between 2 to 100 characters")
  @Column(name = "last_name")
  private String lastName;

  @NotNull(message = "Gender is required field")
  @Column(name = "gender")
  private Gender gender;

  @Past(message = "Date of Birth must be in the past")
  @NotNull(message = "Birthdate is required field")
  @PatientAgeEligible(message = "Patient age should be greater than 18")
  @Column(name = "birthday")
  private LocalDate birthDay;

  @Column(name = "created_on")
  private LocalDate createdOn;

  @PrePersist
  void setCreatedDate(){
    setCreatedOn(PatientUtil.getCurrentDate());
  }

}
