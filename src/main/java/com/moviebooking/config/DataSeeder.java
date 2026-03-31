//package com.moviebooking.config;
//
//import com.moviebooking.model.*;
//import com.moviebooking.repository.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class DataSeeder implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final MovieRepository movieRepository;
//    private final TheatreRepository theatreRepository;
//    private final ScreenRepository screenRepository;
//    private final SeatRepository seatRepository;
//    private final ShowRepository showRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) {
//        if (userRepository.count() > 0) return; 
//
//        log.info("Seeding sample data...");
//
//        // Users
//        User admin = userRepository.save(User.builder()
//                .name("Admin User").email("admin@movie.com")
//                .password(passwordEncoder.encode("admin123"))
//                .role(User.Role.ADMIN).build());
//
//        userRepository.save(User.builder()
//                .name("John Doe").email("john@example.com")
//                .password(passwordEncoder.encode("user123"))
//                .role(User.Role.USER).build());
//
//        // Movies
//        Movie m1 = movieRepository.save(Movie.builder()
//                .title("Avengers: Endgame").genre("Action").language("English")
//                .durationMinutes(181).director("Russo Brothers")
//                .description("The Avengers assemble for one final battle.").rating(8.4)
//                .status(Movie.MovieStatus.NOW_SHOWING).posterUrl("https://example.com/avengers.jpg").build());
//
//        Movie m2 = movieRepository.save(Movie.builder()
//                .title("Interstellar").genre("Sci-Fi").language("English")
//                .durationMinutes(169).director("Christopher Nolan")
//                .description("A team travels through a wormhole in space.").rating(8.6)
//                .status(Movie.MovieStatus.NOW_SHOWING).posterUrl("https://example.com/interstellar.jpg").build());
//
//        movieRepository.save(Movie.builder()
//                .title("KGF Chapter 3").genre("Action").language("Kannada")
//                .durationMinutes(160).director("Prashanth Neel")
//                .description("Rocky's saga continues.").rating(0.0)
//                .status(Movie.MovieStatus.UPCOMING).build());
//
//        // Theatre & Screens
//        Theatre theatre = theatreRepository.save(Theatre.builder()
//                .name("PVR Cinemas").city("Chennai").address("Phoenix Mall, Velachery").build());
//
//        Screen screen1 = screenRepository.save(Screen.builder()
//                .screenName("Screen 1").totalSeats(60).theatre(theatre).build());
//
//        Screen screen2 = screenRepository.save(Screen.builder()
//                .screenName("Screen 2").totalSeats(60).theatre(theatre).build());
//
//        // Generate seats
//        generateSeats(screen1);
//        generateSeats(screen2);
//
//        // Shows
//        showRepository.save(Show.builder()
//                .movie(m1).screen(screen1)
//                .showDate(LocalDate.now()).showTime(LocalTime.of(10, 0))
//                .ticketPrice(250.0).availableSeats(60)
//                .status(Show.ShowStatus.SCHEDULED).build());
//
//        showRepository.save(Show.builder()
//                .movie(m1).screen(screen1)
//                .showDate(LocalDate.now()).showTime(LocalTime.of(14, 30))
//                .ticketPrice(300.0).availableSeats(60)
//                .status(Show.ShowStatus.SCHEDULED).build());
//
//        showRepository.save(Show.builder()
//                .movie(m2).screen(screen2)
//                .showDate(LocalDate.now()).showTime(LocalTime.of(18, 0))
//                .ticketPrice(280.0).availableSeats(60)
//                .status(Show.ShowStatus.SCHEDULED).build());
//
//        log.info("Seeding complete! Admin: admin@movie.com / admin123 | User: john@example.com / user123");
//    }
//
//    private void generateSeats(Screen screen) {
//        char[] rows = "ABCDEF".toCharArray();
//        for (char row : rows) {
//            for (int i = 1; i <= 10; i++) {
//                Seat.SeatType type = (row == 'A' || row == 'B') ? Seat.SeatType.VIP
//                        : (row == 'C' || row == 'D') ? Seat.SeatType.PREMIUM
//                        : Seat.SeatType.REGULAR;
//                seatRepository.save(Seat.builder()
//                        .rowSeat(String.valueOf(row))
//                        .seatNumber(row + String.valueOf(i))
//                        .seatType(type).screen(screen).build());
//            }
//        }
//    }
//}

//package com.moviebooking.config;
//
//import com.moviebooking.model.*;
//import com.moviebooking.repository.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class DataSeeder implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final MovieRepository movieRepository;
//    private final TheatreRepository theatreRepository;
//    private final ScreenRepository screenRepository;
//    private final SeatRepository seatRepository;
//    private final ShowRepository showRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) {
//        if (userRepository.count() > 0) return;
//
//        log.info("Seeding sample data...");
//
//        // Users
//        userRepository.save(User.builder()
//                .name("Admin User").email("admin@movie.com")
//                .password(passwordEncoder.encode("admin123"))
//                .role(User.Role.ADMIN).active(true).build());
//
//        userRepository.save(User.builder()
//                .name("John Doe").email("john@example.com")
//                .password(passwordEncoder.encode("user123"))
//                .role(User.Role.USER).active(true).build());
//
//        // Movies
//        Movie m1 = movieRepository.save(Movie.builder()
//                .title("Avengers: Endgame").genre("Action").language("English")
//                .durationMinutes(181).director("Russo Brothers")
//                .description("The Avengers assemble for one final battle.").rating(8.4)
//                .status(Movie.MovieStatus.NOW_SHOWING)
//                .posterUrl("https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg")
//                .build());
//
//        Movie m2 = movieRepository.save(Movie.builder()
//                .title("Interstellar").genre("Sci-Fi").language("English")
//                .durationMinutes(169).director("Christopher Nolan")
//                .description("A team travels through a wormhole in space.").rating(8.6)
//                .status(Movie.MovieStatus.NOW_SHOWING)
//                .posterUrl("https://upload.wikimedia.org/wikipedia/en/b/bc/Interstellar_film_poster.jpg")
//                .build());
//
//        movieRepository.save(Movie.builder()
//                .title("KGF Chapter 3").genre("Action").language("Kannada")
//                .durationMinutes(160).director("Prashanth Neel")
//                .description("Rocky's saga continues.").rating(0.0)
//                .status(Movie.MovieStatus.UPCOMING).build());
//
//        // Theatre & Screens
//        Theatre theatre = theatreRepository.save(Theatre.builder()
//                .name("PVR Cinemas").city("Chennai").address("Phoenix Mall, Velachery").build());
//
//        Screen screen1 = screenRepository.save(Screen.builder()
//                .screenName("Screen 1").totalSeats(60).theatre(theatre).build());
//
//        Screen screen2 = screenRepository.save(Screen.builder()
//                .screenName("Screen 2").totalSeats(60).theatre(theatre).build());
//
//        // Generate seats — FIX: use .row() not .rowSeat()
//        generateSeats(screen1);
//        generateSeats(screen2);
//
//        // Shows
//        showRepository.save(Show.builder()
//                .movie(m1).screen(screen1)
//                .showDate(LocalDate.now()).showTime(LocalTime.of(10, 0))
//                .ticketPrice(250.0).availableSeats(60)
//                .status(Show.ShowStatus.SCHEDULED).build());
//
//        showRepository.save(Show.builder()
//                .movie(m1).screen(screen1)
//                .showDate(LocalDate.now()).showTime(LocalTime.of(14, 30))
//                .ticketPrice(300.0).availableSeats(60)
//                .status(Show.ShowStatus.SCHEDULED).build());
//
//        showRepository.save(Show.builder()
//                .movie(m2).screen(screen2)
//                .showDate(LocalDate.now()).showTime(LocalTime.of(18, 0))
//                .ticketPrice(280.0).availableSeats(60)
//                .status(Show.ShowStatus.SCHEDULED).build());
//
//        log.info("Seeding complete! Admin: admin@movie.com / admin123 | User: john@example.com / user123");
//    }
//
//    private void generateSeats(Screen screen) {
//        char[] rows = "ABCDEF".toCharArray();
//        for (char row : rows) {
//            for (int i = 1; i <= 10; i++) {
//                Seat.SeatType type = (row == 'A' || row == 'B') ? Seat.SeatType.VIP
//                        : (row == 'C' || row == 'D') ? Seat.SeatType.PREMIUM
//                        : Seat.SeatType.REGULAR;
//                seatRepository.save(Seat.builder()
//                        .seatRow(String.valueOf(row))           // FIX: was .rowSeat() — correct field is .row()
//                        .seatNumber(row + String.valueOf(i))
//                        .seatType(type)
//                        .screen(screen)
//                        .build());
//            }
//        }
//    }
//}

package com.moviebooking.config;

import com.moviebooking.model.*;
import com.moviebooking.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        log.info("Seeding sample data...");

        userRepository.save(User.builder()
                .name("Admin User").email("admin@movie.com")
                .password(passwordEncoder.encode("admin123"))
                .role(User.Role.ADMIN).active(true).build());

        userRepository.save(User.builder()
                .name("John Doe").email("john@example.com")
                .password(passwordEncoder.encode("user123"))
                .role(User.Role.USER).active(true).build());

        Movie m1 = movieRepository.save(Movie.builder()
                .title("Avengers: Endgame").genre("Action").language("English")
                .durationMinutes(181).director("Russo Brothers")
                .description("The Avengers assemble for one final battle.").rating(8.4)
                .status(Movie.MovieStatus.NOW_SHOWING)
                .posterUrl("https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg")
                .build());

        Movie m2 = movieRepository.save(Movie.builder()
                .title("Interstellar").genre("Sci-Fi").language("English")
                .durationMinutes(169).director("Christopher Nolan")
                .description("A team travels through a wormhole in space.").rating(8.6)
                .status(Movie.MovieStatus.NOW_SHOWING)
                .posterUrl("https://upload.wikimedia.org/wikipedia/en/b/bc/Interstellar_film_poster.jpg")
                .build());
        
        
        

        movieRepository.save(Movie.builder()
                .title("KGF Chapter 3").genre("Action").language("Kannada")
                .durationMinutes(160).director("Prashanth Neel")
                .description("Rocky's saga continues.").rating(0.0)
                .status(Movie.MovieStatus.UPCOMING).build());

        Theatre theatre = theatreRepository.save(Theatre.builder()
                .name("PVR Cinemas").city("Chennai").address("Phoenix Mall, Velachery").build());

        Screen screen1 = screenRepository.save(Screen.builder()
                .screenName("Screen 1").totalSeats(60).theatre(theatre).build());

        Screen screen2 = screenRepository.save(Screen.builder()
                .screenName("Screen 2").totalSeats(60).theatre(theatre).build());

        generateSeats(screen1);
        generateSeats(screen2);

        showRepository.save(Show.builder()
                .movie(m1).screen(screen1)
                .showDate(LocalDate.now()).showTime(LocalTime.of(10, 0))
                .ticketPrice(250.0).availableSeats(60)
                .status(Show.ShowStatus.SCHEDULED).build());

        showRepository.save(Show.builder()
                .movie(m1).screen(screen1)
                .showDate(LocalDate.now()).showTime(LocalTime.of(14, 30))
                .ticketPrice(300.0).availableSeats(60)
                .status(Show.ShowStatus.SCHEDULED).build());

        showRepository.save(Show.builder()
                .movie(m2).screen(screen2)
                .showDate(LocalDate.now()).showTime(LocalTime.of(18, 0))
                .ticketPrice(280.0).availableSeats(60)
                .status(Show.ShowStatus.SCHEDULED).build());

        log.info("Seeding complete! Admin: admin@movie.com / admin123 | User: john@example.com / user123");
    }

    private void generateSeats(Screen screen) {
        char[] rows = "ABCDEF".toCharArray();
        for (char row : rows) {
            for (int i = 1; i <= 10; i++) {
                Seat.SeatType type = (row == 'A' || row == 'B') ? Seat.SeatType.VIP
                        : (row == 'C' || row == 'D') ? Seat.SeatType.PREMIUM
                        : Seat.SeatType.REGULAR;
                seatRepository.save(Seat.builder()
                        .seatRow(String.valueOf(row))
                        .seatNumber(row + String.valueOf(i))
                        .seatType(type)
                        .screen(screen)
                        .build());
            }
        }
    }
}