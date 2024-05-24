package org.dgp.eventmanager.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class CreateEventDto {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private PlaceDto place;

    @NotNull
    @Future
    private ZonedDateTime startDateTime;

    @NotNull
    @Future
    private ZonedDateTime endDateTime;

    @NotNull
    @Valid
    private OwnerDto owner;

    @Min(1)
    private int maxParticipantsCount;
}
