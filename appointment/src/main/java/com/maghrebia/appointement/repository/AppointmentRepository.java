package com.maghrebia.appointement.repository;

import com.maghrebia.appointement.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
}
