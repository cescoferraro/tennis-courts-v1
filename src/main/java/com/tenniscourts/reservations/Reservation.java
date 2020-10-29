package com.tenniscourts.reservations;

import com.tenniscourts.config.persistence.BaseEntity;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.schedules.Schedule;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class Reservation extends BaseEntity<Long> {

    @OneToOne
    private Guest guest;

    @ManyToOne
    @NotNull
    private Schedule schedule;

    @NotNull
    @Builder.Default
    private BigDecimal value = BigDecimal.valueOf(100);

    @NotNull
    @Builder.Default
    private ReservationStatus reservationStatus = ReservationStatus.READY_TO_PLAY;

    private BigDecimal refundValue;
}
