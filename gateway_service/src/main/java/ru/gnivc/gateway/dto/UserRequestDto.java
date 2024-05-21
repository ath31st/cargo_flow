package ru.gnivc.gateway.dto;

import lombok.Data;

@Data
public class UserRequestDto {
  private String username;
  private String password;
  private String role;
}