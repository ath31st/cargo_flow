package ru.gnivc.portal.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class Response {
  String timestamp;
  HttpStatus status;
  String error;
}