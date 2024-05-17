package org.dgp.eventmanager.mappers;

import org.dgp.eventmanager.dto.OwnerDto;
import org.dgp.eventmanager.models.Owner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    OwnerDto map(Owner owner);
    Owner map(OwnerDto dto);
}
