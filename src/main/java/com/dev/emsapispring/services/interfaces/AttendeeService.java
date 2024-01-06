package com.dev.emsapispring.services.interfaces;

import com.ems.emsdataservicespring.entities.dtos.AttendeeDto;

public interface AttendeeService {

    AttendeeDto findById(Long id);
    AttendeeDto findByIdUser(String idUser);

}
