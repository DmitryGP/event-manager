package org.dgp.eventmanager.services.impl;

import lombok.RequiredArgsConstructor;
import org.dgp.eventmanager.dto.OwnerDto;
import org.dgp.eventmanager.exceptions.AlreadyExistsException;
import org.dgp.eventmanager.exceptions.NotFoundException;
import org.dgp.eventmanager.mappers.OwnerMapper;
import org.dgp.eventmanager.repositories.OwnerRepository;
import org.dgp.eventmanager.services.OwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository repository;

    private final OwnerMapper mapper;

    @Override
    @Transactional
    public OwnerDto create(OwnerDto owner) {

        var existed = repository.findByEmail(owner.getEmail());

        if(existed.isPresent()) {
            throw new AlreadyExistsException(
                    "Event owner with email [%s] is already existed.".formatted(owner.getEmail()));
        }

        var newOwner = mapper.map(owner);
        newOwner.setId(0);

        var savedOwner = repository.save(newOwner);

        return mapper.map(savedOwner);
    }

    @Override
    public OwnerDto find(String email) {

        var owner = repository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("Person with email = %s is not found".formatted(email)));

        return mapper.map(owner);
    }

    @Override
    public OwnerDto find(long ownerId) {
        var owner = repository.findById(ownerId).orElseThrow(() ->
                new NotFoundException("Person with id = %d is not found".formatted(ownerId)));

        return mapper.map(owner);
    }
}