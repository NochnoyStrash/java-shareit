package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestorId(Long requestorId);

    @Query("select r " +
            "from ItemRequest as r " +
            "join r.requestor as user " +
            "where user.id != ?1 " +
            "order by r.created desc ")
    Page<ItemRequest> findAll(Long userId, Pageable pageable);
}
