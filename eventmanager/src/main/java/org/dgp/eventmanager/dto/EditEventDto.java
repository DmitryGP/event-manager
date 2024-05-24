package org.dgp.eventmanager.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditEventDto {

    @Min(1)
    private long id;

    private String name;

    private String description;

    private ZonedDateTime startDateTime;

    private ZonedDateTime endDateTime;

}
