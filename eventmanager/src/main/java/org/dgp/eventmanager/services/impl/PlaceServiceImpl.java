package org.dgp.eventmanager.services.impl;

import lombok.RequiredArgsConstructor;
import org.dgp.eventmanager.dto.PlaceDto;
import org.dgp.eventmanager.mappers.PlaceMapper;
import org.dgp.eventmanager.repositories.PlaceRepository;
import org.dgp.eventmanager.services.PlaceService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository repository;

    private final PlaceMapper mapper;
    @Override
    public PlaceDto create(PlaceDto place) {

        var newPlace = mapper.map(place);

        newPlace.setId(0);

        var savedPlace = repository.save(newPlace);

        return mapper.map(savedPlace);
    }
}
