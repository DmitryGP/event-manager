package org.dgp.eventmanager.services;

import org.dgp.eventmanager.dto.ParticipantDto;

public interface ParticipantService {
    ParticipantDto create(ParticipantDto participant);

    ParticipantDto find(String email);

    ParticipantDto find(long participantId);
}
