
package com.moviebooking.service;

import com.moviebooking.dto.SeatDto;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.*;
import com.moviebooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final ShowRepository showRepository;

    public List<SeatDto> getSeatsByScreen(Long screenId) {
        return seatRepository.findByScreenId(screenId).stream()
                .map(SeatDto::from).toList();
    }

    public List<SeatDto> getAvailableSeatsForShow(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
        Long screenId = show.getScreen().getId();

        List<Seat> available = seatRepository.findAvailableSeatsByShowId(screenId, showId);

        return available.stream().map(seat -> {
            SeatDto dto = SeatDto.from(seat);
            dto.setAvailable(true);
            return dto;
        }).toList();
    }

    public void addSeatsToScreen(Long screenId, int rows, int seatsPerRow) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        char[] rowLabels = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (int r = 0; r < rows; r++) {
            for (int s = 1; s <= seatsPerRow; s++) {
                Seat seat = Seat.builder()
                        .seatRow(String.valueOf(rowLabels[r]))
                        .seatNumber(rowLabels[r] + String.valueOf(s))
                        .seatType(r < 3 ? Seat.SeatType.VIP
                                : r < 6 ? Seat.SeatType.PREMIUM
                                : Seat.SeatType.REGULAR)
                        .screen(screen)
                        .build();
                seatRepository.save(seat);
            }
        }
    }
}