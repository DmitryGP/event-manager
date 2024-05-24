package org.dgp.eventmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
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
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class EventDto {

    @Min(0)
    private long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private PlaceDto place;

    @NotNull
    @JsonFormat(/*with = JsonFormat.Feature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,*/
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime startDateTime;

    @NotNull
    @JsonFormat(/*with = JsonFormat.Feature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,*/
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime endDateTime;

    @NotNull
    @Valid
    private OwnerDto owner;

    @Min(1)
    private int maxParticipantsCount;

    @Valid
    private List<ParticipantDto> participants;
}
