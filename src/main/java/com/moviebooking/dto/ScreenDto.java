package com.moviebooking.dto;


import lombok.Data;

@Data
public class ScreenDto {
    private Long id;
    private String screenName;
    private int totalSeats;
    private Long theatreId;
    private String theatreName;
}