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

@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AutomobileRepository automobileRepository;

    public void save(AppointmentDto appointment) {
        var offerDetails=appointment.offerDetails();
        Automobile automobile = AppointmentMapper.toOfferDetailsEntity(offerDetails);
        var savedAutomobile = automobileRepository.save(automobile);

        Appointment appointmentEntity = AppointmentMapper.toEntity(appointment);
        appointmentEntity.setOfferDetailsId(savedAutomobile.getAutoId());
        System.out.println(appointmentEntity);
        appointmentRepository.save(appointmentEntity);
    }


}
