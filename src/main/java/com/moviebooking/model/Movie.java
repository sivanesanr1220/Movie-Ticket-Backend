package com.moviebooking.model;

//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import lombok.*;
//
//import java.util.List;
//
//@Entity
//@Table(name = "movies")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Movie {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank
//    private String title;
//
//    @Column(length = 1000)
//    private String description;
//
//    private String genre;
//
//    private String language;
//
//    private int durationMinutes;
//
//    private String director;
//
//    private String cast;
//
//    private String posterUrl;
//
//    private double rating;
//
//    @Enumerated(EnumType.STRING)
//    private MovieStatus status;
//
//    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
//    private List<Show> shows;
//
//    public enum MovieStatus {
//        UPCOMING, NOW_SHOWING, ENDED
//    }
//}



import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @Column(length = 1000)
    private String description;

    private String genre;

    private String language;

    private int durationMinutes;

    private String director;

    @Column(length = 500)
    private String cast;

    @Column(length = 1000)
    private String posterUrl;

    private double rating;

    @Enumerated(EnumType.STRING)
    private MovieStatus status;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Show> shows;

    public enum MovieStatus {
        UPCOMING, NOW_SHOWING, ENDED
    }
}