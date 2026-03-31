package com.moviebooking.service;

import com.moviebooking.dto.TheatreDto;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Theatre;
import com.moviebooking.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheatreService {

    private final TheatreRepository theatreRepository;

    public List<TheatreDto> getAllTheatres() {
        return theatreRepository.findAll().stream().map(TheatreDto::from).toList();
    }

    public List<TheatreDto> getByCity(String city) {
        return theatreRepository.findByCityIgnoreCase(city).stream().map(TheatreDto::from).toList();
    }

    public TheatreDto getById(Long id) {
        return TheatreDto.from(findById(id));
    }

    public TheatreDto create(TheatreDto dto) {
        Theatre theatre = Theatre.builder()
                .name(dto.getName())
                .city(dto.getCity())
                .address(dto.getAddress())
                .build();
        return TheatreDto.from(theatreRepository.save(theatre));
    }

    public TheatreDto update(Long id, TheatreDto dto) {
        Theatre theatre = findById(id);
        theatre.setName(dto.getName());
        theatre.setCity(dto.getCity());
        theatre.setAddress(dto.getAddress());
        return TheatreDto.from(theatreRepository.save(theatre));
    }

    public void delete(Long id) {
        theatreRepository.delete(findById(id));
    }

    private Theatre findById(Long id) {
        return theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
    }
}
