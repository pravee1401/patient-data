package com.data.handler.patientdata.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ErrorMessage {

  private final int statusCode;
  private final LocalDateTime timestamp;
  private final List<String> messages;
  private final String description;
}
