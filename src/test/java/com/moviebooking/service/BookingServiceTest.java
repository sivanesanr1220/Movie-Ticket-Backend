package com.moviebooking.service;

import com.moviebooking.dto.BookingDto;
import com.moviebooking.dto.BookingRequest;
import com.moviebooking.exception.BadRequestException;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.*;
import com.moviebooking.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private UserRepository userRepository;
    @Mock private ShowRepository showRepository;
    @Mock private SeatRepository seatRepository;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Show show;
    private Screen screen;
    private Seat seat1, seat2;

    @BeforeEach
    void setUp() {
        Theatre theatre = Theatre.builder().id(1L).name("PVR").city("Chennai").build();
        screen = Screen.builder().id(1L).screenName("Screen 1").totalSeats(60).theatre(theatre).build();
        Movie movie = Movie.builder().id(1L).title("Test Movie").build();
        show = Show.builder().id(1L).movie(movie).screen(screen)
                .showDate(LocalDate.now()).showTime(LocalTime.of(10, 0))
                .ticketPrice(250.0).availableSeats(60)
                .status(Show.ShowStatus.SCHEDULED).build();
        user = User.builder().id(1L).name("John").email("john@test.com")
                .role(User.Role.USER).build();
        seat1 = Seat.builder().id(1L).seatNumber("A1").seatRow("A").seatType(Seat.SeatType.VIP).screen(screen).build();
        seat2 = Seat.builder().id(2L).seatNumber("A2").seatRow("A").seatType(Seat.SeatType.VIP).screen(screen).build();
    }

    @Test
    void createBooking_success() {
        BookingRequest req = new BookingRequest();
        req.setShowId(1L);
        req.setSeatIds(List.of(1L, 2L));

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(seatRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(seat1, seat2));
//        when(seatRepository.findAvailableSeatsByShowId(1L, 1L)).thenReturn(List.of(seat1, seat2));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> {
            Booking b = inv.getArgument(0);
            b.setId(100L);
            return b;
        });

        BookingDto result = bookingService.createBooking("john@test.com", req);
        assertThat(result.getNumberOfTickets()).isEqualTo(2);
        assertThat(result.getTotalAmount()).isEqualTo(500.0);
    }

    @Test
    void createBooking_seatsAlreadyBooked_throwsException() {
        BookingRequest req = new BookingRequest();
        req.setShowId(1L);
        req.setSeatIds(List.of(1L));

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(seatRepository.findAllById(List.of(1L))).thenReturn(List.of(seat1));
//        when(seatRepository.findAvailableSeatsByShowId(1L, 1L)).thenReturn(List.of()); // no available

        assertThatThrownBy(() -> bookingService.createBooking("john@test.com", req))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already booked");
    }

    @Test
    void cancelBooking_success() {
        Booking booking = Booking.builder().id(1L).user(user).show(show)
                .seats(List.of(seat1)).numberOfTickets(1)
                .status(Booking.BookingStatus.CONFIRMED)
                .paymentStatus(Booking.PaymentStatus.PAID)
                .bookingReference("BOOK-TEST01").build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BookingDto result = bookingService.cancelBooking(1L, "john@test.com");
        assertThat(result.getStatus()).isEqualTo(Booking.BookingStatus.CANCELLED);
    }

    @Test
    void cancelBooking_wrongUser_throwsException() {
        Booking booking = Booking.builder().id(1L).user(user).show(show)
                .status(Booking.BookingStatus.CONFIRMED).build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(1L, "other@test.com"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("not authorized");
    }

    @Test
    void getByReference_notFound_throwsException() {
        when(bookingRepository.findByBookingReference("INVALID")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookingService.getByReference("INVALID"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
