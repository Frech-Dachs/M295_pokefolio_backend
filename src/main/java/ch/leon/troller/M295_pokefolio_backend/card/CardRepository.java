package ch.leon.troller.M295_pokefolio_backend.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByOrderByNameAsc();
}
