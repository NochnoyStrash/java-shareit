package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

@Repository
public interface ItemsRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long ownerId);

    @Query(value = "select * from Items i where i.name ilike %?1% or i.description ilike %?1% and i.is_available = true ", nativeQuery = true)
    Set<Item> findItemsByText(String text);

}
