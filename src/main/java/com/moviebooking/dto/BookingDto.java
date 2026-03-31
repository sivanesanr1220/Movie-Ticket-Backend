package com.moviebooking.dto;

import com.moviebooking.model.Booking;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDto {
    private Long id;
    private String bookingReference;
    private Long userId;
    private String userName;
    private Long showId;
    private String movieTitle;
    private String theatreName;
    private String showDate;
    private String showTime;
    private List<String> seatNumbers;
    private int numberOfTickets;
    private double totalAmount;
    private LocalDateTime bookingTime;
    private Booking.BookingStatus status;
    private Booking.PaymentStatus paymentStatus;

    public static BookingDto from(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setBookingReference(booking.getBookingReference());
        dto.setUserId(booking.getUser().getId());
        dto.setUserName(booking.getUser().getName());
        dto.setShowId(booking.getShow().getId());
        dto.setMovieTitle(booking.getShow().getMovie().getTitle());
        dto.setTheatreName(booking.getShow().getScreen().getTheatre().getName());
        dto.setShowDate(booking.getShow().getShowDate().toString());
        dto.setShowTime(booking.getShow().getShowTime().toString());
        dto.setSeatNumbers(booking.getSeats().stream()
                .map(s -> s.getSeatNumber()).toList());
        dto.setNumberOfTickets(booking.getNumberOfTickets());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setBookingTime(booking.getBookingTime());
        dto.setStatus(booking.getStatus());
        dto.setPaymentStatus(booking.getPaymentStatus());
        return dto;
    }
}
