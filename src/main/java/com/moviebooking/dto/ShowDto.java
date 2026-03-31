package com.moviebooking.dto;

import com.moviebooking.model.Show;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShowDto {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long screenId;
    private String screenName;
    private String theatreName;
    private String theatreCity;
    private LocalDate showDate;
    private LocalTime showTime;
    private double ticketPrice;
    private int availableSeats;
    private Show.ShowStatus status;

    public static ShowDto from(Show show) {
        ShowDto dto = new ShowDto();
        dto.setId(show.getId());
        dto.setMovieId(show.getMovie().getId());
        dto.setMovieTitle(show.getMovie().getTitle());
        dto.setScreenId(show.getScreen().getId());
        dto.setScreenName(show.getScreen().getScreenName());
        dto.setTheatreName(show.getScreen().getTheatre().getName());
        dto.setTheatreCity(show.getScreen().getTheatre().getCity());
        dto.setShowDate(show.getShowDate());
        dto.setShowTime(show.getShowTime());
        dto.setTicketPrice(show.getTicketPrice());
        dto.setAvailableSeats(show.getAvailableSeats());
        dto.setStatus(show.getStatus());
        return dto;
    }
}
