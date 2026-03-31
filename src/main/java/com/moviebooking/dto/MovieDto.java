package com.moviebooking.dto;

import com.moviebooking.model.Movie;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MovieDto {
    private Long id;

    @NotBlank
    private String title;

    private String description;
    private String genre;
    private String language;
    private int durationMinutes;
    private String director;
    private String cast;
    private String posterUrl;
    private double rating;
    private Movie.MovieStatus status;

    public static MovieDto from(Movie movie) {
        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setGenre(movie.getGenre());
        dto.setLanguage(movie.getLanguage());
        dto.setDurationMinutes(movie.getDurationMinutes());
        dto.setDirector(movie.getDirector());
        dto.setCast(movie.getCast());
        dto.setPosterUrl(movie.getPosterUrl());
        dto.setRating(movie.getRating());
        dto.setStatus(movie.getStatus());
        return dto;
    }
}
