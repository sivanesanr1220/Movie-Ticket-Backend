package com.moviebooking.service;

import com.moviebooking.dto.MovieDto;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Movie;
import com.moviebooking.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie sampleMovie;

    @BeforeEach
    void setUp() {
        sampleMovie = Movie.builder()
                .id(1L).title("Test Movie").genre("Action")
                .language("English").durationMinutes(120)
                .status(Movie.MovieStatus.NOW_SHOWING).rating(7.5).build();
    }

    @Test
    void getAllMovies_returnsAllMovies() {
        when(movieRepository.findAll()).thenReturn(List.of(sampleMovie));
        List<MovieDto> result = movieService.getAllMovies();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Movie");
    }

    @Test
    void getMovieById_found() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(sampleMovie));
        MovieDto result = movieService.getMovieById(1L);
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getMovieById_notFound_throwsException() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> movieService.getMovieById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void createMovie_savesAndReturns() {
        when(movieRepository.save(any(Movie.class))).thenReturn(sampleMovie);
        MovieDto dto = new MovieDto();
        dto.setTitle("Test Movie"); dto.setStatus(Movie.MovieStatus.NOW_SHOWING);
        MovieDto result = movieService.createMovie(dto);
        assertThat(result.getTitle()).isEqualTo("Test Movie");
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void deleteMovie_callsDelete() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(sampleMovie));
        movieService.deleteMovie(1L);
        verify(movieRepository, times(1)).delete(sampleMovie);
    }

    @Test
    void getNowShowing_returnsFilteredMovies() {
        when(movieRepository.findByStatus(Movie.MovieStatus.NOW_SHOWING)).thenReturn(List.of(sampleMovie));
        List<MovieDto> result = movieService.getNowShowing();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(Movie.MovieStatus.NOW_SHOWING);
    }
}
