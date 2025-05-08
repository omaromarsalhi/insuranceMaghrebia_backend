package com.maghrebia.appointement.repository;

import com.maghrebia.appointement.model.Appointment;
import com.maghrebia.appointement.model.OfferType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findAllByOfferType(OfferType offerType);
}
