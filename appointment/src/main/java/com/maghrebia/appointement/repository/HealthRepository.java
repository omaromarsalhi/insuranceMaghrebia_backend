package com.maghrebia.appointement.repository;

import com.maghrebia.appointement.model.Automobile;
import com.maghrebia.appointement.model.Health;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRepository extends JpaRepository<Health, Integer> {
}
