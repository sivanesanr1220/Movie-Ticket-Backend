package com.moviebooking.service;

import com.moviebooking.dto.MovieDto;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Movie;
import com.moviebooking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream().map(MovieDto::from).toList();
    }

    public List<MovieDto> getNowShowing() {
        return movieRepository.findByStatus(Movie.MovieStatus.NOW_SHOWING).stream().map(MovieDto::from).toList();
    }

    public List<MovieDto> getUpcoming() {
        return movieRepository.findByStatus(Movie.MovieStatus.UPCOMING).stream().map(MovieDto::from).toList();
    }

    public MovieDto getMovieById(Long id) {
        return MovieDto.from(findById(id));
    }

    public List<MovieDto> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title).stream().map(MovieDto::from).toList();
    }

    public List<MovieDto> getByGenre(String genre) {
        return movieRepository.findByGenre(genre).stream().map(MovieDto::from).toList();
    }

    public MovieDto createMovie(MovieDto dto) {
        Movie movie = buildFromDto(dto, new Movie());
        return MovieDto.from(movieRepository.save(movie));
    }

    public MovieDto updateMovie(Long id, MovieDto dto) {
        Movie movie = buildFromDto(dto, findById(id));
        return MovieDto.from(movieRepository.save(movie));
    }

    public void deleteMovie(Long id) {
        movieRepository.delete(findById(id));
    }

    private Movie findById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    private Movie buildFromDto(MovieDto dto, Movie movie) {
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setGenre(dto.getGenre());
        movie.setLanguage(dto.getLanguage());
        movie.setDurationMinutes(dto.getDurationMinutes());
        movie.setDirector(dto.getDirector());
        movie.setCast(dto.getCast());
        movie.setPosterUrl(dto.getPosterUrl());
        movie.setRating(dto.getRating());
        movie.setStatus(dto.getStatus() != null ? dto.getStatus() : Movie.MovieStatus.UPCOMING);
        return movie;
    }
}
