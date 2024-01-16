package com.dev.emsapispring.entities.dtos;

import com.dev.emsapispring.entities.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddAttendeeDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole userRole;

}
