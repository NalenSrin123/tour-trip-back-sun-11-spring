package com.etec.tourtripapi.schedule.entity;

import com.etec.tourtripapi.common.entity.BaseEntity;
import com.etec.tourtripapi.guide.entity.Guides;
import com.etec.tourtripapi.tour.entity.Tour;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tour_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    public Long getTourId() {
        return tour != null ? tour.getTourId() : null;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id")
    private Guides guide;

    @Column(name = "departure_date", nullable = false)
    private LocalDateTime departureDate;

    @Column(name = "return_date", nullable = false)
    private LocalDateTime returnDate;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "available_slots", nullable = false)
    private Integer availableSlots;
}
