package com.data.handler.patientdata.repository;

import com.data.handler.patientdata.Gender;
import com.data.handler.patientdata.model.Patient;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface PatientRepository extends JpaRepository<Patient, Long> {

  Optional<List<Patient>> findAllByOrderByLastNameAsc();

  Optional<List<Patient>> findAllByGenderOrderByLastNameAsc(Gender gender);

  Optional<List<Patient>> findAllByFirstNameAndLastNameOrderByLastNameAsc(String firstName, String lastName);

  @Modifying
  void deleteAllByCreatedOnBefore(LocalDate createdAt);
}
