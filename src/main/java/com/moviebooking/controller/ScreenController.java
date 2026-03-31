package com.moviebooking.controller;


import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.ScreenDto;
import com.moviebooking.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<ApiResponse<List<ScreenDto>>> getByTheatre(@PathVariable Long theatreId) {
        return ResponseEntity.ok(ApiResponse.success("Screens fetched", screenService.getByTheatre(theatreId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ScreenDto>> create(@RequestBody ScreenDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Screen created", screenService.create(dto)));
    }
}