package org.dgp.eventmanager.mappers;

import org.dgp.eventmanager.dto.CreateEventDto;
import org.dgp.eventmanager.dto.EventDto;
import org.dgp.eventmanager.models.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class EventMapper {

    @Autowired
    protected OwnerMapper ownerMapper;

    @Autowired
    protected ParticipantMapper participantMapper;

    @Mapping(target = "owner", expression = "java(ownerMapper.map(event.getOwner()))")
    @Mapping(target = "participants", expression = "java(event.getParticipants().stream()" +
            ".map(participantMapper::map).toList())")
    public abstract EventDto map(Event event);

    @Mapping(target = "owner", expression = "java(ownerMapper.map(event.getOwner()))")
    @Mapping(target = "participants", expression = "java(event.getParticipants().stream()" +
            ".map(participantMapper::map).toList())")
    public abstract Event map(EventDto event);

    @Mapping(target = "owner", expression = "java(ownerMapper.map(event.getOwner()))")
    @Mapping(target = "participants", expression = "java(new java.util.ArrayList())")
    public abstract Event map(CreateEventDto event);
}
