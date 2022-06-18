package com.data.handler.patientdata.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class PatientUtilTests {

  @Test
  public void getCurrentDateTest() {
    assertEquals(PatientUtil.getCurrentDate(), LocalDate.now());
    try (MockedStatic<PatientUtil> mockedStatic = mockStatic(PatientUtil.class)) {
      mockedStatic
          .when(PatientUtil::getCurrentDate).thenReturn(LocalDate.now().minusYears(10));
      assertEquals(PatientUtil.getCurrentDate(), LocalDate.now().minusYears(10));
    }
  }
}
