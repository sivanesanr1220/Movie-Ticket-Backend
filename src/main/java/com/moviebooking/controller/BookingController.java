package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.BookingDto;
import com.moviebooking.dto.BookingRequest;
import com.moviebooking.service.BookingService;
import com.moviebooking.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingDto>> book(
            @Valid @RequestBody BookingRequest request,
            Authentication auth) {
        BookingDto booking = bookingService.createBooking(auth.getName(), request);
        emailService.sendBookingConfirmation(auth.getName(), booking);
        return ResponseEntity.ok(ApiResponse.success("Booking confirmed!", booking));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<BookingDto>>> getMyBookings(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.success("Your bookings", bookingService.getUserBookings(auth.getName())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Booking found", bookingService.getBookingById(id)));
    }

    @GetMapping("/reference/{ref}")
    public ResponseEntity<ApiResponse<BookingDto>> getByReference(@PathVariable String ref) {
        return ResponseEntity.ok(ApiResponse.success("Booking found", bookingService.getByReference(ref)));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingDto>> cancel(@PathVariable Long id, Authentication auth) {
        BookingDto booking = bookingService.cancelBooking(id, auth.getName());
        emailService.sendCancellationEmail(auth.getName(), booking);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled", booking));
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<BookingDto>> confirmPayment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Payment confirmed", bookingService.confirmPayment(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BookingDto>>> getAllBookings() {
        return ResponseEntity.ok(ApiResponse.success("All bookings", bookingService.getAllBookings()));
    }
}
