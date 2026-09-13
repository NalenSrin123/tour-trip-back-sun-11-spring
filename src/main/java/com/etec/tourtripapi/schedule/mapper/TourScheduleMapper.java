package com.etec.tourtripapi.schedule.mapper;

import com.etec.tourtripapi.common.mapper.BaseMapper;
import com.etec.tourtripapi.schedule.dto.request.TourScheduleRequest;
import com.etec.tourtripapi.schedule.dto.response.TourScheduleResponse;
import com.etec.tourtripapi.schedule.entity.TourSchedule;
import org.springframework.stereotype.Component;

@Component
public class TourScheduleMapper implements BaseMapper<TourSchedule, TourScheduleRequest, TourScheduleResponse> {

    @Override
    public TourSchedule toEntity(TourScheduleRequest request) {
        if (request == null) {
            return null;
        }

        TourSchedule schedule = new TourSchedule();
        updateEntity(schedule, request);
        return schedule;
    }

    @Override
    public TourScheduleResponse toResponse(TourSchedule entity) {
        if (entity == null) {
            return null;
        }

        return TourScheduleResponse.builder()
                .scheduleId(entity.getScheduleId())
                .tourId(entity.getTourId())
                .guideId(entity.getGuide() != null ? entity.getGuide().getId() : null)
                .guideName(entity.getGuide() != null ? entity.getGuide().getFullName() : null)
                .departureDate(entity.getDepartureDate())
                .returnDate(entity.getReturnDate())
                .maxCapacity(entity.getMaxCapacity())
                .availableSlots(entity.getAvailableSlots())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public void updateEntity(TourSchedule entity, TourScheduleRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setTourId(request.getTourId());
        entity.setDepartureDate(request.getDepartureDate());
        entity.setReturnDate(request.getReturnDate());
        entity.setMaxCapacity(request.getMaxCapacity());
        entity.setAvailableSlots(request.getAvailableSlots());
    }
}
