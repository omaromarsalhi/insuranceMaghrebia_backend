package com.maghrebia.appointement.repository;

import com.maghrebia.appointement.model.Appointment;
import com.maghrebia.appointement.model.Automobile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomobileRepository extends JpaRepository<Automobile, Integer> {
}
