package com.moviebooking.dto;

import com.moviebooking.model.Seat;
import lombok.Data;

@Data
public class SeatDto {
    private Long id;
    private String seatNumber;
    private String seatRow;
    private Seat.SeatType seatType;
    private boolean available;

    public static SeatDto from(Seat seat) {
        SeatDto dto = new SeatDto();
        dto.setId(seat.getId());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setSeatRow(seat.getSeatRow());
        dto.setSeatType(seat.getSeatType());
        return dto;
    }
}
