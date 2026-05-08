package ch.leon.troller.M295_pokefolio_backend.cardinstance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardInstanceRepository extends JpaRepository<CardInstance, Long> {
    List<CardInstance> findByCollectionId(Long collectionId);
    boolean existsByCardIdAndCollectionId(Long cardId, Long collectionId);
}