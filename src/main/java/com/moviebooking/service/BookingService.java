
package com.moviebooking.service;

import com.moviebooking.dto.BookingDto;
import com.moviebooking.dto.BookingRequest;
import com.moviebooking.exception.BadRequestException;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.*;
import com.moviebooking.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public BookingDto createBooking(String userEmail, BookingRequest request) {
        log.info("Creating booking for user: {}, showId: {}", userEmail, request.getShowId());

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        if (show.getStatus() != Show.ShowStatus.SCHEDULED) {
            throw new BadRequestException("This show is not available for booking");
        }

        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());
        if (seats.size() != request.getSeatIds().size()) {
            throw new BadRequestException("One or more seats not found");
        }

        Long screenId = show.getScreen().getId();
        boolean allValid = seats.stream().allMatch(s -> s.getScreen().getId().equals(screenId));
        if (!allValid) {
            throw new BadRequestException("Some seats do not belong to this show's screen");
        }

        List<Seat> availableSeats = seatRepository.findAvailableSeatsByShowId(screenId, show.getId());
        List<Long> availableIds = availableSeats.stream().map(Seat::getId).toList();
        boolean allAvailable = seats.stream().allMatch(s -> availableIds.contains(s.getId()));
        if (!allAvailable) {
            throw new BadRequestException("One or more selected seats are already booked");
        }

        double totalAmount = show.getTicketPrice() * seats.size();

        Booking booking = Booking.builder()
                .bookingReference(generateReference())
                .user(user)
                .show(show)
                .seats(seats)
                .numberOfTickets(seats.size())
                .totalAmount(totalAmount)
                .bookingTime(LocalDateTime.now())
                .status(Booking.BookingStatus.CONFIRMED)
                .paymentStatus(Booking.PaymentStatus.UNPAID)
                .build();

        show.setAvailableSeats(show.getAvailableSeats() - seats.size());
        showRepository.save(show);

        Booking saved = bookingRepository.save(booking);
        log.info("Booking saved with id: {}, now fetching with details", saved.getId());

        // Re-fetch with all associations eagerly loaded
        return bookingRepository.findByIdWithDetails(saved.getId())
                .map(BookingDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found after save"));
    }

    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long id) {
        return BookingDto.from(
                bookingRepository.findByIdWithDetails(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id))
        );
    }

    @Transactional(readOnly = true)
    public BookingDto getByReference(String ref) {
        return BookingDto.from(
                bookingRepository.findByBookingReference(ref)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + ref))
        );
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return bookingRepository.findByUserId(user.getId())
                .stream().map(BookingDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAllWithDetails()
                .stream().map(BookingDto::from).toList();
    }

    @Transactional
    public BookingDto cancelBooking(Long id, String userEmail) {
        Booking booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new BadRequestException("You are not authorized to cancel this booking");
        }
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setPaymentStatus(Booking.PaymentStatus.REFUNDED);

        Show show = booking.getShow();
        show.setAvailableSeats(show.getAvailableSeats() + booking.getNumberOfTickets());
        showRepository.save(show);

        Booking saved = bookingRepository.save(booking);
        return bookingRepository.findByIdWithDetails(saved.getId())
                .map(BookingDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found after cancel"));
    }

    @Transactional
    public BookingDto confirmPayment(Long id) {
        Booking booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
        booking.setPaymentStatus(Booking.PaymentStatus.PAID);
        Booking saved = bookingRepository.save(booking);
        return bookingRepository.findByIdWithDetails(saved.getId())
                .map(BookingDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found after payment"));
    }

    private String generateReference() {
        return "BOOK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}