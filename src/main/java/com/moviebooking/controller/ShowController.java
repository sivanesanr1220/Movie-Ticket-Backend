package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.ShowDto;
import com.moviebooking.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShowDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Shows fetched", showService.getAllShows()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShowDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Show found", showService.getById(id)));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<ApiResponse<List<ShowDto>>> getByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(ApiResponse.success("Shows for movie", showService.getShowsByMovie(movieId)));
    }

    @GetMapping("/movie/{movieId}/date/{date}")
    public ResponseEntity<ApiResponse<List<ShowDto>>> getByMovieAndDate(
            @PathVariable Long movieId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success("Shows fetched", showService.getShowsByMovieAndDate(movieId, date)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShowDto>> create(@RequestBody ShowDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Show created", showService.create(dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShowDto>> update(@PathVariable Long id, @RequestBody ShowDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Show updated", showService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        showService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Show deleted", null));
    }
}
