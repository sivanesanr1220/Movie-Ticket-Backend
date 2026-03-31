//package com.moviebooking.repository;
//
//import com.moviebooking.model.Booking;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;
//import java.util.Optional;
//
//public interface BookingRepository extends JpaRepository<Booking, Long> {
//    List<Booking> findByUserId(Long userId);
//    List<Booking> findByShowId(Long showId);
//    Optional<Booking> findByBookingReference(String bookingReference);
//    List<Booking> findByStatus(Booking.BookingStatus status);
//}


//package com.moviebooking.repository;
//
//import com.moviebooking.model.Booking;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface BookingRepository extends JpaRepository<Booking, Long> {
//
//    // Eagerly fetch all nested associations needed by BookingDto.from()
//    @Query("SELECT DISTINCT b FROM Booking b " +
//           "JOIN FETCH b.user " +
//           "JOIN FETCH b.show s " +
//           "JOIN FETCH s.movie " +
//           "JOIN FETCH s.screen sc " +
//           "JOIN FETCH sc.theatre " +
//           "LEFT JOIN FETCH b.seats " +
//           "WHERE b.user.id = :userId")
//    List<Booking> findByUserId(@Param("userId") Long userId);
//
//    @Query("SELECT DISTINCT b FROM Booking b " +
//           "JOIN FETCH b.user " +
//           "JOIN FETCH b.show s " +
//           "JOIN FETCH s.movie " +
//           "JOIN FETCH s.screen sc " +
//           "JOIN FETCH sc.theatre " +
//           "LEFT JOIN FETCH b.seats " +
//           "WHERE b.show.id = :showId")
//    List<Booking> findByShowId(@Param("showId") Long showId);
//
//    @Query("SELECT DISTINCT b FROM Booking b " +
//           "JOIN FETCH b.user " +
//           "JOIN FETCH b.show s " +
//           "JOIN FETCH s.movie " +
//           "JOIN FETCH s.screen sc " +
//           "JOIN FETCH sc.theatre " +
//           "LEFT JOIN FETCH b.seats " +
//           "WHERE b.bookingReference = :ref")
//    Optional<Booking> findByBookingReference(@Param("ref") String ref);
//
//    @Query("SELECT DISTINCT b FROM Booking b " +
//           "JOIN FETCH b.user " +
//           "JOIN FETCH b.show s " +
//           "JOIN FETCH s.movie " +
//           "JOIN FETCH s.screen sc " +
//           "JOIN FETCH sc.theatre " +
//           "LEFT JOIN FETCH b.seats " +
//           "WHERE b.id = :id")
//    Optional<Booking> findByIdWithDetails(@Param("id") Long id);
//
//    @Query("SELECT DISTINCT b FROM Booking b " +
//           "JOIN FETCH b.user " +
//           "JOIN FETCH b.show s " +
//           "JOIN FETCH s.movie " +
//           "JOIN FETCH s.screen sc " +
//           "JOIN FETCH sc.theatre " +
//           "LEFT JOIN FETCH b.seats")
//    List<Booking> findAllWithDetails();
//
//    List<Booking> findByStatus(Booking.BookingStatus status);
//}

package com.moviebooking.repository;

import com.moviebooking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // All queries use JOIN FETCH to avoid LazyInitializationException
    // when BookingDto.from() walks: booking -> show -> screen -> theatre

    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN FETCH b.user " +
           "JOIN FETCH b.show s " +
           "JOIN FETCH s.movie " +
           "JOIN FETCH s.screen sc " +
           "JOIN FETCH sc.theatre " +
           "LEFT JOIN FETCH b.seats " +
           "WHERE b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN FETCH b.user " +
           "JOIN FETCH b.show s " +
           "JOIN FETCH s.movie " +
           "JOIN FETCH s.screen sc " +
           "JOIN FETCH sc.theatre " +
           "LEFT JOIN FETCH b.seats " +
           "WHERE b.bookingReference = :ref")
    Optional<Booking> findByBookingReference(@Param("ref") String ref);

    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN FETCH b.user " +
           "JOIN FETCH b.show s " +
           "JOIN FETCH s.movie " +
           "JOIN FETCH s.screen sc " +
           "JOIN FETCH sc.theatre " +
           "LEFT JOIN FETCH b.seats " +
           "WHERE b.id = :id")
    Optional<Booking> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN FETCH b.user " +
           "JOIN FETCH b.show s " +
           "JOIN FETCH s.movie " +
           "JOIN FETCH s.screen sc " +
           "JOIN FETCH sc.theatre " +
           "LEFT JOIN FETCH b.seats")
    List<Booking> findAllWithDetails();

    List<Booking> findByStatus(Booking.BookingStatus status);
}