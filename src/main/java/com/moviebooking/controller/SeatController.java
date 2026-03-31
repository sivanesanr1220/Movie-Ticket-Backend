package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.SeatDto;
import com.moviebooking.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<ApiResponse<List<SeatDto>>> getSeatsByScreen(@PathVariable Long screenId) {
        return ResponseEntity.ok(ApiResponse.success("Seats fetched", seatService.getSeatsByScreen(screenId)));
    }

    @GetMapping("/show/{showId}/available")
    public ResponseEntity<ApiResponse<List<SeatDto>>> getAvailableSeats(@PathVariable Long showId) {
        return ResponseEntity.ok(ApiResponse.success("Available seats", seatService.getAvailableSeatsForShow(showId)));
    }

    @PostMapping("/screen/{screenId}/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> generateSeats(
            @PathVariable Long screenId,
            @RequestParam(defaultValue = "10") int rows,
            @RequestParam(defaultValue = "12") int seatsPerRow) {
        seatService.addSeatsToScreen(screenId, rows, seatsPerRow);
        return ResponseEntity.ok(ApiResponse.success("Seats generated successfully", null));
    }
}
