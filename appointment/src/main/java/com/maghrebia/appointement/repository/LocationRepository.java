package com.maghrebia.appointement.repository;

import com.maghrebia.appointement.model.Automobile;
import com.maghrebia.appointement.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
