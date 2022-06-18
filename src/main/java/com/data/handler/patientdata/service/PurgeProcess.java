package com.data.handler.patientdata.service;

import com.data.handler.patientdata.repository.PatientRepository;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PurgeProcess {

  private final PatientRepository patientRepository;

  @Value("${patient.records.retention.years}")
  private String patientRecordsRetentionPeriodInYears;

  @Scheduled(cron = "${app.config.schedule.time}")
  public void deleteOlderPatientRecords() {
    long retentionPeriodInYears = Long.parseLong(patientRecordsRetentionPeriodInYears);

    log.info("deleteOlderPatientRecords >> Deleting records older than {} years started", retentionPeriodInYears);
    patientRepository.deleteAllByCreatedOnBefore(LocalDate.now().minusYears(retentionPeriodInYears));
    log.info("deleteOlderPatientRecords << Deleting records older than {} years completed", retentionPeriodInYears);
  }
}
