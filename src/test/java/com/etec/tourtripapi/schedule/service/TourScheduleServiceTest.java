package com.etec.tourtripapi.schedule.service;

import com.etec.tourtripapi.common.exception.ResourceNotFoundException;
import com.etec.tourtripapi.guide.repository.repoGuide;
import com.etec.tourtripapi.schedule.dto.request.TourScheduleRequest;
import com.etec.tourtripapi.schedule.dto.response.TourScheduleResponse;
import com.etec.tourtripapi.schedule.entity.TourSchedule;
import com.etec.tourtripapi.schedule.mapper.TourScheduleMapper;
import com.etec.tourtripapi.schedule.repository.TourScheduleRepository;
import com.etec.tourtripapi.schedule.service.impl.TourScheduleServiceImpl;
import com.etec.tourtripapi.tour.entity.Tour;
import com.etec.tourtripapi.tour.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourScheduleServiceTest {

    private TourScheduleRepository tourScheduleRepository;
    private TourScheduleMapper tourScheduleMapper;
    private repoGuide repoGuide;
    private TourRepository tourRepository;
    private TourScheduleService tourScheduleService;

    @BeforeEach
    void setUp() {
        tourScheduleRepository = mock(TourScheduleRepository.class);
        tourScheduleMapper = new TourScheduleMapper();
        repoGuide = mock(repoGuide.class);
        tourRepository = mock(TourRepository.class);
        tourScheduleService = new TourScheduleServiceImpl(tourScheduleRepository, tourScheduleMapper, repoGuide, tourRepository);
    }

    @Test
    void createSchedule_validRequest_createsSuccessfully() {
        LocalDateTime departure = LocalDateTime.now().plusDays(5);
        LocalDateTime returnDate = departure.plusDays(3);

        TourScheduleRequest request = TourScheduleRequest.builder()
                .tourId(10L)
                .departureDate(departure)
                .returnDate(returnDate)
                .maxCapacity(20)
                .availableSlots(20)
                .build();

        Tour tour = Tour.builder()
                .tourId(10L)
                .title("Angkor Tour")
                .build();

        when(tourRepository.findById(10L)).thenReturn(Optional.of(tour));

        TourSchedule saved = TourSchedule.builder()
                .scheduleId(1L)
                .tour(tour)
                .departureDate(departure)
                .returnDate(returnDate)
                .maxCapacity(20)
                .availableSlots(20)
                .build();

        when(tourScheduleRepository.save(any(TourSchedule.class))).thenReturn(saved);

        TourScheduleResponse response = tourScheduleService.createSchedule(request);

        assertNotNull(response);
        assertEquals(1L, response.getScheduleId());
        assertEquals(10L, response.getTourId());
        assertEquals("Angkor Tour", response.getTourTitle());
        assertEquals(20, response.getMaxCapacity());
    }

    @Test
    void createSchedule_tourNotFound_throwsResourceNotFound() {
        LocalDateTime departure = LocalDateTime.now().plusDays(5);
        LocalDateTime returnDate = departure.plusDays(3);

        TourScheduleRequest request = TourScheduleRequest.builder()
                .tourId(999L)
                .departureDate(departure)
                .returnDate(returnDate)
                .maxCapacity(20)
                .build();

        when(tourRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tourScheduleService.createSchedule(request));
    }

    @Test
    void getScheduleById_existingId_returnsSchedule() {
        Tour tour = Tour.builder().tourId(10L).title("Angkor Tour").build();
        TourSchedule schedule = TourSchedule.builder()
                .scheduleId(1L)
                .tour(tour)
                .departureDate(LocalDateTime.now().plusDays(1))
                .returnDate(LocalDateTime.now().plusDays(2))
                .maxCapacity(15)
                .availableSlots(15)
                .build();

        when(tourScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        TourScheduleResponse response = tourScheduleService.getScheduleById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getScheduleId());
        assertEquals(10L, response.getTourId());
        assertEquals("Angkor Tour", response.getTourTitle());
    }
}
