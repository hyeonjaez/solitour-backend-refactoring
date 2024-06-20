package solitour_backend.solitour.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.place.entity.Place;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    boolean existsBySearchId(String searchId);

    Optional<Place> findBySearchIdAndId(String searchId, Long id);

}
