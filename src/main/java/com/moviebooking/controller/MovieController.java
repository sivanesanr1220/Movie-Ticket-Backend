package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.MovieDto;
import com.moviebooking.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MovieDto>>> getAllMovies() {
        return ResponseEntity.ok(ApiResponse.success("Movies fetched", movieService.getAllMovies()));
    }

    @GetMapping("/now-showing")
    public ResponseEntity<ApiResponse<List<MovieDto>>> getNowShowing() {
        return ResponseEntity.ok(ApiResponse.success("Now showing movies", movieService.getNowShowing()));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<MovieDto>>> getUpcoming() {
        return ResponseEntity.ok(ApiResponse.success("Upcoming movies", movieService.getUpcoming()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Movie found", movieService.getMovieById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MovieDto>>> search(@RequestParam String title) {
        return ResponseEntity.ok(ApiResponse.success("Search results", movieService.searchByTitle(title)));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<ApiResponse<List<MovieDto>>> getByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(ApiResponse.success("Movies by genre", movieService.getByGenre(genre)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MovieDto>> create(@Valid @RequestBody MovieDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Movie created", movieService.createMovie(dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MovieDto>> update(@PathVariable Long id, @Valid @RequestBody MovieDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Movie updated", movieService.updateMovie(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok(ApiResponse.success("Movie deleted", null));
    }
}
