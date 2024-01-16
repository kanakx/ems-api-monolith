package com.dev.emsapispring.services.interfaces;

import com.dev.emsapispring.entities.dtos.AddAttendeeDto;
import com.dev.emsapispring.entities.dtos.AttendeeDto;

import java.util.List;

public interface AttendeeService {

    List<AttendeeDto> findAll();
    AttendeeDto findById(Long id);
    AttendeeDto update(Long id, AddAttendeeDto updatedAttendeeDto);
    void deleteById(Long id);

}
