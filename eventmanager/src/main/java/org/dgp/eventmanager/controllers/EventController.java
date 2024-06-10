package org.dgp.eventmanager.controllers;

import lombok.RequiredArgsConstructor;
import org.dgp.eventmanager.dto.CreateEventDto;
import org.dgp.eventmanager.dto.EditEventDto;
import org.dgp.eventmanager.dto.EventDto;
import org.dgp.eventmanager.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventDto> create(@RequestBody CreateEventDto newEvent) {
        return ResponseEntity.ok(eventService.create(newEvent));
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getEvents(
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Long participantId) {
        if (ownerId != null) {
            return ResponseEntity.ok(eventService.findOwnedEvents(ownerId));
        }

        if (participantId != null) {
            return ResponseEntity.ok(eventService.findParticipatedEvents(participantId));
        }

        return ResponseEntity.ofNullable(eventService.findAllActual());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.find(eventId));
    }

    @PutMapping
    public ResponseEntity<EventDto> editEvent(@RequestBody EditEventDto editEvent) {
        return ResponseEntity.ok(eventService.editEvent(editEvent));
    }

    @PutMapping("/{eventId}/{participantId}")
    public ResponseEntity<EventDto> addParticipant(@PathVariable Long eventId, @PathVariable Long participantId) {
        return ResponseEntity.ok(eventService.addParticipant(eventId, participantId));
    }
}
