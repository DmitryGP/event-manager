package org.dgp.eventmanager.repositories;

import org.dgp.eventmanager.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
