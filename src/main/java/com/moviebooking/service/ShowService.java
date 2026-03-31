package com.moviebooking.service;

import com.moviebooking.dto.ShowDto;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.*;
import com.moviebooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;

    public List<ShowDto> getAllShows() {
        return showRepository.findAll().stream().map(ShowDto::from).toList();
    }

    public List<ShowDto> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieIdAndShowDateGreaterThanEqual(movieId, LocalDate.now())
                .stream().map(ShowDto::from).toList();
    }

    public List<ShowDto> getShowsByMovieAndDate(Long movieId, LocalDate date) {
        return showRepository.findByMovieIdAndShowDate(movieId, date)
                .stream().map(ShowDto::from).toList();
    }

    public ShowDto getById(Long id) {
        return ShowDto.from(findById(id));
    }

    public ShowDto create(ShowDto dto) {
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        Screen screen = screenRepository.findById(dto.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));
        Show show = Show.builder()
                .movie(movie)
                .screen(screen)
                .showDate(dto.getShowDate())
                .showTime(dto.getShowTime())
                .ticketPrice(dto.getTicketPrice())
                .availableSeats(screen.getTotalSeats())
                .status(Show.ShowStatus.SCHEDULED)
                .build();
        return ShowDto.from(showRepository.save(show));
    }

    public ShowDto update(Long id, ShowDto dto) {
        Show show = findById(id);
        show.setShowDate(dto.getShowDate());
        show.setShowTime(dto.getShowTime());
        show.setTicketPrice(dto.getTicketPrice());
        if (dto.getStatus() != null) show.setStatus(dto.getStatus());
        return ShowDto.from(showRepository.save(show));
    }

    public void delete(Long id) {
        showRepository.delete(findById(id));
    }

    public Show findById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));
    }
}
