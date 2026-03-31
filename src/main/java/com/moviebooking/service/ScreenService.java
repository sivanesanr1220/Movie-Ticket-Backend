package com.moviebooking.service;



import com.moviebooking.dto.ScreenDto;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Screen;
import com.moviebooking.model.Theatre;
import com.moviebooking.repository.ScreenRepository;
import com.moviebooking.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final TheatreRepository theatreRepository;

    public List<ScreenDto> getByTheatre(Long theatreId) {
        return screenRepository.findByTheatreId(theatreId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public ScreenDto create(ScreenDto dto) {
        Theatre theatre = theatreRepository.findById(dto.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));
        Screen screen = Screen.builder()
                .screenName(dto.getScreenName())
                .totalSeats(dto.getTotalSeats())
                .theatre(theatre)
                .build();
        return toDto(screenRepository.save(screen));
    }

    private ScreenDto toDto(Screen screen) {
        ScreenDto dto = new ScreenDto();
        dto.setId(screen.getId());
        dto.setScreenName(screen.getScreenName());
        dto.setTotalSeats(screen.getTotalSeats());
        if (screen.getTheatre() != null) {
            dto.setTheatreId(screen.getTheatre().getId());
            dto.setTheatreName(screen.getTheatre().getName());
        }
        return dto;
    }
}