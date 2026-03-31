package com.moviebooking.dto;

import com.moviebooking.model.Seat;
import com.moviebooking.model.Theatre;
import lombok.Data;

@Data
public class TheatreDto {
    private Long id;
    private String name;
    private String city;
    private String address;

    public static TheatreDto from(Theatre t) {
        TheatreDto dto = new TheatreDto();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setCity(t.getCity());
        dto.setAddress(t.getAddress());
        return dto;
    }
}
