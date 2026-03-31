package com.moviebooking.repository;

import com.moviebooking.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void findByStatus_returnsCorrectMovies() {
        movieRepository.save(Movie.builder().title("Movie A").status(Movie.MovieStatus.NOW_SHOWING).build());
        movieRepository.save(Movie.builder().title("Movie B").status(Movie.MovieStatus.UPCOMING).build());

        List<Movie> nowShowing = movieRepository.findByStatus(Movie.MovieStatus.NOW_SHOWING);
        assertThat(nowShowing).hasSize(1);
        assertThat(nowShowing.get(0).getTitle()).isEqualTo("Movie A");
    }

    @Test
    void findByTitleContainingIgnoreCase_works() {
        movieRepository.save(Movie.builder().title("Avengers Endgame").status(Movie.MovieStatus.NOW_SHOWING).build());
        movieRepository.save(Movie.builder().title("Spider-Man").status(Movie.MovieStatus.NOW_SHOWING).build());

        List<Movie> result = movieRepository.findByTitleContainingIgnoreCase("avengers");
        assertThat(result).hasSize(1);
    }

    @Test
    void findByGenre_returnsCorrect() {
        movieRepository.save(Movie.builder().title("Action Movie").genre("Action").status(Movie.MovieStatus.NOW_SHOWING).build());
        movieRepository.save(Movie.builder().title("Comedy Movie").genre("Comedy").status(Movie.MovieStatus.NOW_SHOWING).build());

        List<Movie> result = movieRepository.findByGenre("Action");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGenre()).isEqualTo("Action");
    }
}
