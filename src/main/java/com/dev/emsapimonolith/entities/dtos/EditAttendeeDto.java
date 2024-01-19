package com.dev.emsapimonolith.entities.dtos;

import com.dev.emsapimonolith.entities.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditAttendeeDto {

    private String firstName;
    private String lastName;
    private String email;
    private UserRole userRole;

}
