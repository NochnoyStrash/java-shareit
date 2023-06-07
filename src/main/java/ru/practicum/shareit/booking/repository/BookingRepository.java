package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select * from bookings b left join items it on b.item_id = it.id" +
            " where b.id = ?1 and (b.booker_id = ?2 or it.owner_id = ?2)", nativeQuery = true)
    Optional<Booking> findBookingForAuthor(Long bookingId, Long userId);

    @Query(value = "select * from bookings b where b.booker_id = ?1  order by b.start_date desc", nativeQuery = true)
    List<Booking> findAllByUser(Long user);

    @Query(value = "select * from bookings b  where b.booker_id  = ?1 " +
            "and b.start_date  < now() " +
            "and b.end_date  > now()  order by b.start_date  desc;", nativeQuery = true)
    List<Booking> findAllByUserCurrent(Long user);

    @Query(value = "select * from bookings b  where b.booker_id  = ?1  " +
            "and b.status like 'APPROVED' and b.end_date  < now() order by b.start_date  desc;", nativeQuery = true)
    List<Booking> findAllByUserPost(Long user);

    @Query(value = "select * from bookings b  where b.booker_id  = ?1  " +
            "and b.start_date  > now() order by b.start_date  desc;", nativeQuery = true)
    List<Booking> findAllByUserFuture(Long user);

    @Query(value = "select * from bookings b  where b.booker_id  = ?1  " +
            "and b.status like 'WAITING' order by b.start_date desc;", nativeQuery = true)
    List<Booking> findAllByUserWaiting(Long user);

    @Query(value = "select * from bookings b  where b.booker_id  = ?1  " +
            "and b.status like 'REJECTED' order by b.start_date desc;", nativeQuery = true)
    List<Booking> findAllByUserRejected(Long user);

    @Query("select b " +
        "from Booking as b " +
        "join fetch b.booker as booker " +
        "join fetch b.item as i where i.owner.id = ?1 " +
        "and b.start < current_timestamp " +
        "and b.end > current_timestamp " +
        "order by b.end desc")
    List<Booking> findAllByOwnerCurrent(Long owner);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.booker as booker " +
            "join fetch b.item as i where i.owner.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.end desc")
    List<Booking> findAllByOwnerPost(Long owner);

    @Query("select b "
        + "from Booking as b "
        + "join fetch b.booker as booker "
        + "join fetch b.item as i "
        + "where i.owner.id = ?1 and b.start > current_timestamp "
        + "order by b.end desc")
    List<Booking> findAllByOwnerFuture(Long owner);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.booker as booker " +
            "join fetch b.item as i where i.owner.id = ?1 " +
            "and b.status = 'WAITING' " +
            "order by b.end desc")
    List<Booking> findAllByOwnerWaiting(Long owner);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.booker as booker " +
            "join fetch b.item as i where i.owner.id = ?1 " +
            "and b.status = 'REJECTED' " +
            "order by b.end desc")
    List<Booking> findAllByOwnerRejected(Long owner);

    @Query("select b "
            + "from Booking as b "
            + "join fetch b.booker as booker "
            + "join fetch b.item as i "
            + "where i.owner.id = ?1 "
            + "order by b.end desc")
    List<Booking> findAllByOwner(Long owner);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.booker as booker " +
            "join fetch b.item as i " +
            "where i.id = ?1 " +
            "and b.start < current_timestamp " +
            "order by b.end desc ")
    List<Booking> findLastBookingItem(Long itemId);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.booker as booker " +
            "join fetch b.item as i " +
            "where i.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start asc ")
    List<Booking> findNextBookingItem(Long itemId);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.booker as booker " +
            "join fetch b.item as i " +
            "where i.id = ?1 " +
            "and booker.id = ?2 " +
            "and b.end < current_timestamp ")
    List<Booking> findPastBookingByUser(Long itemId, Long userId);
}
