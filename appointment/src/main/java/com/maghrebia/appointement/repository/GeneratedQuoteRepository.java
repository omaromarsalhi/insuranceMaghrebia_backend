package com.maghrebia.appointement.repository;

import com.maghrebia.appointement.model.GeneratedQuote;
import com.maghrebia.appointement.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneratedQuoteRepository extends JpaRepository<GeneratedQuote, Integer> {
}
