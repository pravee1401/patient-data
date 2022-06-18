package com.data.handler.patientdata.service;

import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"app.config.schedule.time=*/10 * * * * *"})
public class PurgeProcessTests {

  @SpyBean
  private PurgeProcess purgeProcess;

  @Test
  public void deleteOlderPatientRecordsTest(){

    Awaitility.await().atMost(15, TimeUnit.SECONDS).untilAsserted(() ->
        verify(purgeProcess, Mockito.atLeastOnce()).deleteOlderPatientRecords());
  }
}
