package com.etec.tourtripapi.schedule.service.impl;

import com.etec.tourtripapi.common.exception.ResourceNotFoundException;
import com.etec.tourtripapi.guide.entity.Guides;
import com.etec.tourtripapi.guide.repository.repoGuide;
import com.etec.tourtripapi.schedule.dto.request.TourScheduleRequest;
import com.etec.tourtripapi.schedule.dto.response.TourScheduleResponse;
import com.etec.tourtripapi.schedule.entity.TourSchedule;
import com.etec.tourtripapi.schedule.exception.ScheduleConflictException;
import com.etec.tourtripapi.schedule.exception.ScheduleValidationException;
import com.etec.tourtripapi.schedule.mapper.TourScheduleMapper;
import com.etec.tourtripapi.schedule.repository.TourScheduleRepository;
import com.etec.tourtripapi.schedule.service.TourScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourScheduleServiceImpl implements TourScheduleService {

    private final TourScheduleRepository tourScheduleRepository;
    private final TourScheduleMapper tourScheduleMapper;
    private final repoGuide repoGuide;

    @Override
    @Transactional
    public TourScheduleResponse createSchedule(TourScheduleRequest request) {
        validateScheduleDates(request.getDepartureDate(), request.getReturnDate(), true);
        validateCapacityAndSlots(request, true);

        TourSchedule schedule = tourScheduleMapper.toEntity(request);

        if (request.getGuideId() != null) {
            Guides guide = repoGuide.findById(request.getGuideId())
                    .orElseThrow(() -> new ResourceNotFoundException("Guide", "id", request.getGuideId()));
            validateGuideAvailability(guide.getId(), request.getDepartureDate(), request.getReturnDate(), null);
            schedule.setGuide(guide);
        }

        TourSchedule savedSchedule = tourScheduleRepository.save(schedule);
        return tourScheduleMapper.toResponse(savedSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public TourScheduleResponse getScheduleById(Long id) {
        TourSchedule schedule = tourScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourSchedule", "id", id));
        return tourScheduleMapper.toResponse(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourScheduleResponse> getAllSchedules() {
        List<TourSchedule> schedules = tourScheduleRepository.findAll();
        return tourScheduleMapper.toResponseList(schedules);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourScheduleResponse> getSchedulesByTourId(Long tourId) {
        List<TourSchedule> schedules = tourScheduleRepository.findByTourId(tourId);
        return tourScheduleMapper.toResponseList(schedules);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourScheduleResponse> getSchedulesByGuideId(Long guideId) {
        List<TourSchedule> schedules = tourScheduleRepository.findByGuideId(guideId);
        return tourScheduleMapper.toResponseList(schedules);
    }

    @Override
    @Transactional
    public TourScheduleResponse updateSchedule(Long id, TourScheduleRequest request) {
        TourSchedule existingSchedule = tourScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourSchedule", "id", id));

        validateScheduleDates(request.getDepartureDate(), request.getReturnDate(), false);
        validateCapacityAndSlots(request, false);

        tourScheduleMapper.updateEntity(existingSchedule, request);

        if (request.getGuideId() != null) {
            Guides guide = repoGuide.findById(request.getGuideId())
                    .orElseThrow(() -> new ResourceNotFoundException("Guide", "id", request.getGuideId()));
            validateGuideAvailability(guide.getId(), request.getDepartureDate(), request.getReturnDate(), id);
            existingSchedule.setGuide(guide);
        } else {
            existingSchedule.setGuide(null);
        }

        TourSchedule updatedSchedule = tourScheduleRepository.save(existingSchedule);
        return tourScheduleMapper.toResponse(updatedSchedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        TourSchedule schedule = tourScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourSchedule", "id", id));

        try {
            tourScheduleRepository.delete(schedule);
            tourScheduleRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ScheduleConflictException("Cannot delete tour schedule with id: " + id +
                    " because it is referenced by existing bookings");
        }
    }

    private void validateScheduleDates(LocalDateTime departureDate, LocalDateTime returnDate, boolean isCreate) {
        if (departureDate == null || returnDate == null) {
            throw new ScheduleValidationException("Departure date and return date are required");
        }

        if (!returnDate.isAfter(departureDate)) {
            throw new ScheduleValidationException("Return date must be after departure date");
        }

        if (isCreate && departureDate.isBefore(LocalDateTime.now())) {
            throw new ScheduleValidationException("Departure date must be in the present or future");
        }
    }

    private void validateCapacityAndSlots(TourScheduleRequest request, boolean isCreate) {
        if (request.getMaxCapacity() == null || request.getMaxCapacity() < 1) {
            throw new ScheduleValidationException("Max capacity must be at least 1");
        }

        if (isCreate && request.getAvailableSlots() == null) {
            request.setAvailableSlots(request.getMaxCapacity());
        }

        if (request.getAvailableSlots() != null) {
            if (request.getAvailableSlots() < 0) {
                throw new ScheduleValidationException("Available slots cannot be negative");
            }
            if (request.getAvailableSlots() > request.getMaxCapacity()) {
                throw new ScheduleValidationException("Available slots (" + request.getAvailableSlots() +
                        ") cannot exceed max capacity (" + request.getMaxCapacity() + ")");
            }
        }
    }

    private void validateGuideAvailability(Long guideId, LocalDateTime departureDate, LocalDateTime returnDate, Long excludeScheduleId) {
        List<TourSchedule> overlapping;
        if (excludeScheduleId == null) {
            overlapping = tourScheduleRepository.findOverlappingSchedulesForGuide(guideId, departureDate, returnDate);
        } else {
            overlapping = tourScheduleRepository.findOverlappingSchedulesForGuideExcludingSchedule(guideId, departureDate, returnDate, excludeScheduleId);
        }

        if (!overlapping.isEmpty()) {
            throw new ScheduleConflictException("Guide with id " + guideId +
                    " has an overlapping schedule between " + departureDate + " and " + returnDate);
        }
    }
}
