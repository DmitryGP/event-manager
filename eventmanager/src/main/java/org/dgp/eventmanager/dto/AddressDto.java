package org.dgp.eventmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddressDto {

    private long id;

    private String country;

    private String city;

    private String address;
}
