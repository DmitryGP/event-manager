package org.dgp.eventmanager.mappers;

import org.dgp.eventmanager.dto.PlaceDto;
import org.dgp.eventmanager.models.Place;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaceMapper {
    PlaceDto map(Place place);

    Place map(PlaceDto dto);
}
