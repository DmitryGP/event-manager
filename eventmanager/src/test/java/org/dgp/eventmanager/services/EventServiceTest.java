package org.dgp.eventmanager.services;

import org.dgp.eventmanager.dto.CreateEventDto;
import org.dgp.eventmanager.dto.EditEventDto;
import org.dgp.eventmanager.dto.EventDto;
import org.dgp.eventmanager.dto.OwnerDto;
import org.dgp.eventmanager.dto.ParticipantDto;
import org.dgp.eventmanager.exceptions.LogicException;
import org.dgp.eventmanager.exceptions.NotFoundException;
import org.dgp.eventmanager.models.Event;
import org.dgp.eventmanager.models.Owner;
import org.dgp.eventmanager.models.Participant;
import org.dgp.eventmanager.repositories.EventRepository;
import org.dgp.eventmanager.repositories.OwnerRepository;
import org.dgp.eventmanager.repositories.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private OwnerRepository ownerRepository;

    @MockBean
    private ParticipantRepository participantRepository;

    @MockBean
    private EditEventNotificationSender editEventNotificationSender;

    @MockBean
    private NearestEventsNotificationSender nearestEventsNotificationSender;

    private InOrder inOrderEventRepository;

    private final static LocalDate now = LocalDate.now();

    private static CreateEventDto createDto;

    private static Event newEvent;

    private static EventDto expectedNewEvent;

    private static Event existedEvent;

    private static Event existedEventFull;

    private static Participant firstParticipant;

    private static Participant secondParticipant;

    private static EventDto expectedEventWithParticipant;

    private static EditEventDto editEventDto;

    private static EventDto expectedEditedEvent;

    @BeforeEach
    void setup() {
        inOrderEventRepository = inOrder(eventRepository);
        initData();
    }

    @Test
    void createEvent() {

        when(eventRepository.save(any(Event.class))).thenReturn(newEvent);

        var actual = eventService.create(createDto);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expectedNewEvent);
    }

    @Test
    void addParticipant() {

        when(eventRepository.findById(2L)).thenReturn(Optional.of(existedEvent));
        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstParticipant));
        doAnswer(invocationOnMock -> {
            var event = invocationOnMock.getArgument(0, Event.class);
            return event;
        }).when(eventRepository).save(any(Event.class));

        var actual = eventService.addParticipant(2, 1);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expectedEventWithParticipant);
    }

    @Test
    void addParticipantShouldNotAddParticipantIfMaxCountIsReached() {
        when(eventRepository.findById(3L)).thenReturn(Optional.of(existedEventFull));
        when(participantRepository.findById(2L)).thenReturn(Optional.of(secondParticipant));

        assertThatExceptionOfType(LogicException.class).isThrownBy(() -> eventService.addParticipant(3, 2));

        inOrderEventRepository.verify(eventRepository, times(0)).save(any(Event.class));
    }

    @Test
    void addParticipantShouldThrowNotFoundExceptionIfEventNotExists() {
        when(eventRepository.findById(33L)).thenReturn(Optional.empty());
        when(participantRepository.findById(2L)).thenReturn(Optional.of(secondParticipant));

        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> eventService.addParticipant(33, 2));

        inOrderEventRepository.verify(eventRepository, times(0)).save(any(Event.class));
    }

    @Test
    void addParticipantShouldThrowNotFoundExceptionIfParticipantNotExists() {
        when(eventRepository.findById(2L)).thenReturn(Optional.of(existedEvent));
        when(participantRepository.findById(22L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> eventService.addParticipant(3, 22));

        inOrderEventRepository.verify(eventRepository, times(0)).save(any(Event.class));
    }

    @Test
    void editEvent() {
        when(eventRepository.findById(2L)).thenReturn(Optional.of(existedEvent));

        doAnswer(invocationOnMock -> {
            var event = invocationOnMock.getArgument(0, Event.class);
            return event;
        }).when(eventRepository).save(any(Event.class));

        var actual = eventService.editEvent(editEventDto);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedEditedEvent);
    }

    private static void initData() {
        createDto = CreateEventDto.builder()
                .name("new event")
                .description("super event")
                .startDate(now.plusDays(10))
                .startTime(LocalTime.of(10, 0))
                .endDate(now.plusDays(12))
                .endTime(LocalTime.of(20, 0))
                .maxParticipantsCount(3)
                .address("address1")
                .owner(OwnerDto.builder()
                        .id(1)
                        .name("Alex")
                        .surname("First")
                        .email("AlexFirst@host.org")
                        .phoneNumber("+7 (111) 222-3344")
                        .build())
                .build();

        newEvent = Event.builder()
                .id(1)
                .name("new event")
                .description("super event")
                .startDate(now.plusDays(10))
                .startTime(LocalTime.of(10, 0))
                .endDate(now.plusDays(12))
                .endTime(LocalTime.of(20, 0))
                .maxParticipantsCount(3)
                .address("address1")
                .owner(Owner.builder()
                        .id(1)
                        .name("Alex")
                        .surname("First")
                        .email("AlexFirst@host.org")
                        .phoneNumber("+7 (111) 222-3344")
                        .build())
                .participants(new ArrayList<>())
                .build();

        expectedNewEvent = EventDto.builder()
                .id(1)
                .name("new event")
                .description("super event")
                .startDate(now.plusDays(10))
                .startTime(LocalTime.of(10, 0))
                .endDate(now.plusDays(12))
                .endTime(LocalTime.of(20, 0))
                .maxParticipantsCount(3)
                .address("address1")
                .owner(OwnerDto.builder()
                        .id(1)
                        .name("Alex")
                        .surname("First")
                        .email("AlexFirst@host.org")
                        .phoneNumber("+7 (111) 222-3344")
                        .build())
                .participants(new ArrayList<>())
                .build();

        existedEvent = Event.builder()
                .id(2)
                .name("second event")
                .description("just event")
                .startDate(now.plusDays(20))
                .startTime(LocalTime.of(10, 0))
                .endDate(now.plusDays(22))
                .endTime(LocalTime.of(20, 0))
                .maxParticipantsCount(2)
                .address("address1")
                .owner(Owner.builder()
                        .id(1)
                        .name("Alex")
                        .surname("First")
                        .email("AlexFirst@host.org")
                        .phoneNumber("+7 (111) 222-3344")
                        .build())
                .participants(new ArrayList<>())
                .build();

        firstParticipant = Participant.builder()
                .id(1)
                .name("Ivan")
                .surname("Ivanov")
                .email("IvanIvanov@host.org")
                .phoneNumber("+7 (444) 777-2211")
                .build();


        expectedEventWithParticipant = EventDto.builder()
                .id(2)
                .name("second event")
                .description("just event")
                .startDate(now.plusDays(20))
                .startTime(LocalTime.of(10, 0))
                .endDate(now.plusDays(22))
                .endTime(LocalTime.of(20, 0))
                .maxParticipantsCount(2)
                .address("address1")
                .owner(OwnerDto.builder()
                        .id(1)
                        .name("Alex")
                        .surname("First")
                        .email("AlexFirst@host.org")
                        .phoneNumber("+7 (111) 222-3344")
                        .build())
                .participants(List.of(ParticipantDto.builder()
                        .id(1)
                        .name("Ivan")
                        .surname("Ivanov")
                        .email("IvanIvanov@host.org")
                        .phoneNumber("+7 (444) 777-2211")
                        .build()))
                .build();

        existedEventFull = Event.builder()
                .id(3)
                .name("event")
                .description("just event")
                .startDate(now.plusDays(20))
                .startTime(LocalTime.of(10, 0))
                .endDate(now.plusDays(22))
                .endTime(LocalTime.of(20, 0))
                .maxParticipantsCount(1)
                .address("address1")
                .owner(Owner.builder()
                        .id(1)
                        .name("Alex")
                        .surname("First")
                        .email("AlexFirst@host.org")
                        .phoneNumber("+7 (111) 222-3344")
                        .build())
                .participants(new ArrayList<>(List.of(firstParticipant)))
                .build();

        secondParticipant = Participant.builder()
                .id(2)
                .name("Petr")
                .surname("Petrov")
                .email("PetrPetrovv@host.org")
                .phoneNumber("+7 (567) 987-3456")
                .build();

        editEventDto = EditEventDto.builder()
                .id(2L)
                .name("new-new")
                .description("more information")
                .startDate(now.plusDays(30))
                .startTime(LocalTime.of(11, 0))
                .endDate(now.plusDays(35))
                .endTime(LocalTime.of(21, 0))
                .build();

        expectedEditedEvent = EventDto.builder()
                .id(2)
                .name("new-new")
                .description("more information")
                .startDate(now.plusDays(30))
                .startTime(LocalTime.of(11, 0))
                .endDate(now.plusDays(35))
                .endTime(LocalTime.of(21, 0))
                .maxParticipantsCount(2)
                .address("address1")
                .owner(OwnerDto.builder()
                        .id(1)
                        .name("Alex")
                        .surname("First")
                        .email("AlexFirst@host.org")
                        .phoneNumber("+7 (111) 222-3344")
                        .build())
                .participants(new ArrayList<>())
                .build();


    }

    @Configuration
    @ComponentScan({
            "org.dgp.eventmanager.services",
            "org.dgp.eventmanager.repositories",
            "org.dgp.eventmanager.mappers"})
    public static class TestConfiguration {

    }
}
