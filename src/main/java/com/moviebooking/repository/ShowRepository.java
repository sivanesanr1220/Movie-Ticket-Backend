package com.moviebooking.repository;

import com.moviebooking.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovieId(Long movieId);
    List<Show> findByMovieIdAndShowDate(Long movieId, LocalDate showDate);
    List<Show> findByScreenId(Long screenId);
    List<Show> findByShowDate(LocalDate showDate);
    List<Show> findByMovieIdAndShowDateGreaterThanEqual(Long movieId, LocalDate date);
}
