package com.dev.emsapispring.services.interfaces;

import com.dev.emsapispring.entities.dtos.EditAttendeeDto;
import com.dev.emsapispring.entities.dtos.AttendeeDto;

import java.util.List;

public interface AttendeeService {

    List<AttendeeDto> findAll();
    AttendeeDto findById(Long id);
    AttendeeDto update(Long id, EditAttendeeDto updatedAttendeeDto);
    void deleteById(Long id);

}
