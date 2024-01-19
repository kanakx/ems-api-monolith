package com.dev.emsapimonolith.services.interfaces;

import com.dev.emsapimonolith.entities.dtos.EditAttendeeDto;
import com.dev.emsapimonolith.entities.dtos.AttendeeDto;

import java.util.List;

public interface AttendeeService {

    List<AttendeeDto> findAll();
    AttendeeDto findById(Long id);
    AttendeeDto updateById(Long id, EditAttendeeDto updatedAttendeeDto);
    void deleteById(Long id);

}
