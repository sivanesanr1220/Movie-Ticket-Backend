package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.TheatreDto;
import com.moviebooking.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
public class TheatreController {

    private final TheatreService theatreService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TheatreDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Theatres fetched", theatreService.getAllTheatres()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TheatreDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Theatre found", theatreService.getById(id)));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse<List<TheatreDto>>> getByCity(@PathVariable String city) {
        return ResponseEntity.ok(ApiResponse.success("Theatres in " + city, theatreService.getByCity(city)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TheatreDto>> create(@RequestBody TheatreDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Theatre created", theatreService.create(dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TheatreDto>> update(@PathVariable Long id, @RequestBody TheatreDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Theatre updated", theatreService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        theatreService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Theatre deleted", null));
    }
}
