package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.booker as booker " +
            "join fetch b.item as i " +
            "where b.id = ?1 " +
            "and (booker.id = ?2 OR i.owner.id = ?2)")
    Optional<Booking> findBookingForAuthor(Long bookingId, Long userId);

    @Query("select b " +
            "from Booking as b " +
            "join  b.booker as booker " +
            "join  b.item as i " +
            "where b.booker.id = ?1 " +
            "order by b.start desc ")
    Page<Booking> findAllByUser(Long userId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join  b.booker as booker " +
            "join  b.item as i " +
            "where b.booker.id = ?1 " +
            "and b.start < current_timestamp " +
            "and b.end > current_timestamp " +
            "order by b.start desc ")
    Page<Booking> findAllByUserCurrent(Long userId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join  b.booker as booker " +
            "join  b.item as i " +
            "where b.booker.id = ?1 " +
            "and b.status = 'APPROVED' " +
            "and b.end < current_timestamp " +
            "order by b.start desc ")
    Page<Booking> findAllByUserPost(Long userId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join  b.booker as booker " +
            "join  b.item as i " +
            "where b.booker.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start desc ")
    Page<Booking> findAllByUserFuture(Long userId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as booker " +
            "join b.item as i " +
            "where b.booker.id = ?1 " +
            "and b.status = 'WAITING' " +
            "order by b.start desc ")
    Page<Booking> findAllByUserWaiting(Long userId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join  b.booker as booker " +
            "join  b.item as i " +
            "where b.booker.id = ?1 " +
            "and b.status = 'REJECTED' " +
            "order by b.start desc ")
    Page<Booking> findAllByUserRejected(Long userId, Pageable pageable);

    @Query("select b " +
        "from Booking as b " +
        "join  b.booker as booker " +
        "join b.item as i where i.owner.id = ?1 " +
        "and b.start < current_timestamp " +
        "and b.end > current_timestamp " +
        "order by b.end desc")
    Page<Booking> findAllByOwnerCurrent(Long userId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join  b.booker as booker " +
            "join  b.item as i where i.owner.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.end desc")
    Page<Booking> findAllByOwnerPost(Long userId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join  b.booker as booker " +
            "join  b.item as i where i.owner.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.end desc")
    List<Booking> findAllByOwnerPost(Long userId);

    @Query("select b "
        + "from Booking as b "
        + "join  b.booker as booker "
        + "join  b.item as i "
        + "where i.owner.id = ?1 and b.start > current_timestamp "
        + "order by b.end desc")
    Page<Booking> findAllByOwnerFuture(Long userId, Pageable pageable);

    @Query("select b "
            + "from Booking as b "
            + "join fetch  b.booker as booker "
            + "join fetch  b.item as i "
            + "where i.owner.id = ?1 and b.start > current_timestamp "
            + "order by b.end desc")
    List<Booking> findAllByOwnerFuture(Long userId);

    @Query("select b " +
            "from Booking as b " +
            "join  b.booker as booker " +
            "join  b.item as i where i.owner.id = ?1 " +
            "and b.status = 'WAITING' " +
            "order by b.end desc")
    Page<Booking> findAllByOwnerWaiting(Long userId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join  b.booker as booker " +
            "join  b.item as i where i.owner.id = ?1 " +
            "and b.status = 'REJECTED' " +
            "order by b.end desc")
    Page<Booking> findAllByOwnerRejected(Long userId, Pageable pageable);

    @Query("select b "
            + "from Booking as b "
            + "join  b.booker as booker "
            + "join  b.item as i "
            + "where i.owner.id = ?1 "
            + "order by b.end desc")
    Page<Booking> findAllByOwner(Long userId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.booker as booker " +
            "join fetch b.item as i " +
            "where i.id = ?1 " +
            "and b.start < current_timestamp " +
            "order by b.end desc ")
    List<Booking> findLastBookingItem(Long userId);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.booker as booker " +
            "join fetch b.item as i " +
            "where i.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start asc ")
    List<Booking> findNextBookingItem(Long user);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.booker as booker " +
            "join fetch b.item as i " +
            "where i.id = ?1 " +
            "and booker.id = ?2 " +
            "and b.end < current_timestamp ")
    List<Booking> findPastBookingByUser(Long itemId, Long user);
}