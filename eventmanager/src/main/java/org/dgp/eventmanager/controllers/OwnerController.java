package org.dgp.eventmanager.controllers;

import lombok.RequiredArgsConstructor;
import org.dgp.eventmanager.dto.OwnerDto;
import org.dgp.eventmanager.services.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<OwnerDto> create(@RequestBody OwnerDto newOwner) {
        return ResponseEntity.ok(ownerService.create(newOwner));
    }

    @GetMapping
    public ResponseEntity<OwnerDto> getOwner(@RequestParam String email) {
        return ResponseEntity.ok(ownerService.find(email));
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<OwnerDto> getOwner(@PathVariable long ownerId) {
        return ResponseEntity.ok(ownerService.find(ownerId));
    }
}
