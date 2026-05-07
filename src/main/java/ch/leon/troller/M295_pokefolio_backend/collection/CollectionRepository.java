package ch.leon.troller.M295_pokefolio_backend.collection;

import ch.leon.troller.M295_pokefolio_backend.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByOrderByNameAsc();
}