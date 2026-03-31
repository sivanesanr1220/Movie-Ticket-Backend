//package com.moviebooking.repository;
//
//import com.moviebooking.model.Seat;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import java.util.List;
//
//public interface SeatRepository extends JpaRepository<Seat, Long> {
//    List<Seat> findByScreenId(Long screenId);
//
//    @Query("SELECT s FROM Seat s WHERE s.screen.id = :screenId AND s.id NOT IN " +
//           "(SELECT seat.id FROM Booking b JOIN b.seats seat WHERE b.show.id = :showId AND b.status = 'CONFIRMED')")
//    List<Seat> findAvailableSeatsByShowId(Long screenId, Long showId);
//}
//package com.moviebooking.repository;
//
//import com.moviebooking.model.Seat;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface SeatRepository extends JpaRepository<Seat, Long> {
//
//    List<Seat> findByScreenId(Long screenId);
//
//    @Query(value =
//        "SELECT s.* FROM seats s " +
//        "WHERE s.screen_id = :screenId " +
//        "AND s.id NOT IN (" +
//        "  SELECT bs.seat_id FROM booking_seats bs " +
//        "  INNER JOIN bookings b ON bs.booking_id = b.id " +
//        "  WHERE b.show_id = :showId AND b.status = 'CONFIRMED'" +
//        ")",
//        nativeQuery = true)
//    List<Seat> findAvailableSeatsByShowId(@Param("screenId") Long screenId,
//                                          @Param("showId") Long showId);
//}

//package com.moviebooking.repository;
//
//import com.moviebooking.model.Seat;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import java.util.List;
//
//public interface SeatRepository extends JpaRepository<Seat, Long> {
//    List<Seat> findByScreenId(Long screenId);
//
//    @Query("SELECT s FROM Seat s WHERE s.screen.id = :screenId AND s.id NOT IN " +
//           "(SELECT seat.id FROM Booking b JOIN b.seats seat WHERE b.show.id = :showId AND b.status = 'CONFIRMED')")
//    List<Seat> findAvailableSeatsByShowId(Long screenId, Long showId);
//}
package com.moviebooking.repository;

import com.moviebooking.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByScreenId(Long screenId);

    @Query("SELECT s FROM Seat s WHERE s.screen.id = :screenId AND s.id NOT IN " +
           "(SELECT seat.id FROM Booking b JOIN b.seats seat WHERE b.show.id = :showId AND b.status = 'CONFIRMED')")
    List<Seat> findAvailableSeatsByShowId(@Param("screenId") Long screenId,
                                          @Param("showId") Long showId);
}