package com.moviebooking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "shows")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "screen_id")
    private Screen screen;

    private LocalDate showDate;

    private LocalTime showTime;

    private double ticketPrice;

    private int availableSeats;

    @Enumerated(EnumType.STRING)
    private ShowStatus status;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public enum ShowStatus {
        SCHEDULED, CANCELLED, COMPLETED
    }
}
