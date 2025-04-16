package com.maghrebia.appointement.service;


import com.maghrebia.appointement.dto.AppointmentDto;
import com.maghrebia.appointement.dto.AutomobileDto;
import com.maghrebia.appointement.mapper.AppointmentMapper;
import com.maghrebia.appointement.model.Appointment;
import com.maghrebia.appointement.model.Automobile;
import com.maghrebia.appointement.repository.AppointmentRepository;
import com.maghrebia.appointement.repository.AutomobileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AutomobileRepository automobileRepository;

    public void save(AppointmentDto appointment) {
//        var offerDetails = appointment.offerDetails();
//        Automobile automobile = AppointmentMapper.toOfferDetailsEntity(offerDetails);
//        var savedAutomobile = automobileRepository.save(automobile);

        Appointment appointmentEntity = AppointmentMapper.toEntity(appointment);
//        appointmentEntity.setAutomobile(savedAutomobile);
        System.out.println(appointmentEntity);
        appointmentRepository.save(appointmentEntity);
    }

    public List<AppointmentDto> getAppointments() {
        var appointments = appointmentRepository.findAll();
        var automobiles = automobileRepository.findAll();
        List<AppointmentDto> appointmentsDto = new ArrayList<>();
        appointments.forEach(appointment -> {
            Automobile result = automobiles.stream()
                    .filter(auto -> auto.getAutoId().equals(appointment.getAutomobile().getAutoId()))
                    .findFirst()
                    .orElse(null);
            AppointmentDto dto;
            var quote = AppointmentMapper.toGeneratedQuoteDto(appointment.getGeneratedQuote());
            if (result != null) {
                dto = AppointmentMapper.toDto(appointment, AppointmentMapper.toOfferDetailsDto(result), quote);
            } else {
                dto = AppointmentMapper.toDto(appointment);
            }
            appointmentsDto.add(dto);
        });
        return appointmentsDto;
    }


}
