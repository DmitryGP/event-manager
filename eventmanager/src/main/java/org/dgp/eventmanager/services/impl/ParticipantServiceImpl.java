package org.dgp.eventmanager.services.impl;

import lombok.RequiredArgsConstructor;
import org.dgp.eventmanager.dto.ParticipantDto;
import org.dgp.eventmanager.exceptions.AlreadyExistsException;
import org.dgp.eventmanager.exceptions.NotFoundException;
import org.dgp.eventmanager.mappers.ParticipantMapper;
import org.dgp.eventmanager.repositories.ParticipantRepository;
import org.dgp.eventmanager.services.ParticipantService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository repository;

    private final ParticipantMapper mapper;
    @Override
    public ParticipantDto create(ParticipantDto participant) {

        var existed = repository.findByEmail(participant.getEmail());

        if(existed.isPresent()) {
            throw new AlreadyExistsException(
                    "Event owner with email [%s] is already existed.".formatted(participant.getEmail()));
        }

        var newParticipant = mapper.map(participant);

        newParticipant.setId(0);

        var savedParticipant = repository.save(newParticipant);

        return mapper.map(savedParticipant);
    }

    @Override
    public ParticipantDto find(String email) {

        var owner = repository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("Person with email = %s is not found".formatted(email)));

        return mapper.map(owner);
    }

    @Override
    public ParticipantDto find(long participantId) {
        var owner = repository.findById(participantId).orElseThrow(() ->
                new NotFoundException("Person with id = %d is not found".formatted(participantId)));

        return mapper.map(owner);
    }
}
