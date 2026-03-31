package com.moviebooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviebooking.dto.MovieDto;
import com.moviebooking.model.Movie;
import com.moviebooking.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private MovieService movieService;

    @Test
    @WithMockUser
    void getAllMovies_returns200() throws Exception {
        MovieDto dto = new MovieDto();
        dto.setId(1L); dto.setTitle("Test Movie"); dto.setGenre("Action");
        when(movieService.getAllMovies()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Test Movie"));
    }

    @Test
    @WithMockUser
    void getMovieById_returns200() throws Exception {
        MovieDto dto = new MovieDto();
        dto.setId(1L); dto.setTitle("Test Movie");
        when(movieService.getMovieById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Test Movie"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createMovie_asAdmin_returns200() throws Exception {
        MovieDto dto = new MovieDto();
        dto.setTitle("New Movie"); dto.setStatus(Movie.MovieStatus.NOW_SHOWING);
        when(movieService.createMovie(any())).thenReturn(dto);

        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void createMovie_asUser_returns403() throws Exception {
        MovieDto dto = new MovieDto();
        dto.setTitle("New Movie");

        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }
}
