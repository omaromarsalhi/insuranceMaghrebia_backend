package com.maghrebia.appointement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String appointmentId;

    private String firstName;
    private String lastName;
    private String email;
    private Long phone;
    private LocalDateTime dob;
    private Long cin;

    @Enumerated(EnumType.STRING)
    private OfferType offerType;

    private String offerDetailsId;

    @ManyToOne(targetEntity = GeneratedQuote.class,cascade = CascadeType.PERSIST)
    private GeneratedQuote generatedQuote;
}