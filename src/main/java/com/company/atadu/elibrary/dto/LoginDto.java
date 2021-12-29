package com.company.atadu.elibrary.dto;

import lombok.Data;

@Data
public class LoginDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isNotLocked;
    private String token;
}
