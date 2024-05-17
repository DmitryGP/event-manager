package org.dgp.eventmanager.services;

import org.dgp.eventmanager.dto.OwnerDto;

public interface OwnerService {

    OwnerDto create(OwnerDto owner);

    OwnerDto find(String email);

    OwnerDto find(long ownerId);
}
