package org.dgp.eventmanager.mappers;

import org.dgp.eventmanager.dto.ParticipantDto;
import org.dgp.eventmanager.models.Participant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    Participant map(ParticipantDto dto);

    ParticipantDto map(Participant participant);
}
