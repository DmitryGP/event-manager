package org.dgp.eventmanager.controllers;

import lombok.RequiredArgsConstructor;
import org.dgp.eventmanager.dto.ParticipantDto;
import org.dgp.eventmanager.services.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping
    public ResponseEntity<ParticipantDto> create(@RequestBody ParticipantDto newParticipant) {
        return ResponseEntity.ok(participantService.create(newParticipant));
    }

    @GetMapping
    public ResponseEntity<ParticipantDto> getOwner(@RequestParam String email) {
        return ResponseEntity.ok(participantService.find(email));
    }

    @GetMapping("/{participantId}")
    public ResponseEntity<ParticipantDto> getParticipant(@PathVariable long participantId) {
        return ResponseEntity.ok(participantService.find(participantId));
    }
}
