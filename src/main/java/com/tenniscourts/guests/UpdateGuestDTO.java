package com.tenniscourts.guests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class UpdateGuestDTO {

    private Long id;

    private String name;

}
