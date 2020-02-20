package com.tenniscourts.guests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GuestDTO {
    private Long id;

    @NotNull
    private String name;
}
