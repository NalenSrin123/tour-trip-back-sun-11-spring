package com.etec.tourtripapi.tour.service;

import com.etec.tourtripapi.category.entity.Category;
import com.etec.tourtripapi.category.repository.CategoryRepository;
import com.etec.tourtripapi.common.exception.ResourceNotFoundException;
import com.etec.tourtripapi.tour.dto.request.TourRequest;
import com.etec.tourtripapi.tour.dto.response.TourResponse;
import com.etec.tourtripapi.tour.entity.Tour;
import com.etec.tourtripapi.tour.entity.TourImage;
import com.etec.tourtripapi.tour.mapper.TourMapper;
import com.etec.tourtripapi.tour.repository.TourImageRepository;
import com.etec.tourtripapi.tour.repository.TourRepository;
import com.etec.tourtripapi.tour.service.impl.TourServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourServiceTest {

    private TourRepository tourRepository;
    private TourImageRepository tourImageRepository;
    private CategoryRepository categoryRepository;
    private TourMapper tourMapper;
    private TourService tourService;

    @BeforeEach
    void setUp() {
        tourRepository = mock(TourRepository.class);
        tourImageRepository = mock(TourImageRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        tourMapper = new TourMapper();
        tourService = new TourServiceImpl(tourRepository, tourImageRepository, categoryRepository, tourMapper);
    }

    @Test
    void createTourWithoutCategorySavesTour() {
        TourRequest request = TourRequest.builder()
                .title("Angkor Discovery")
                .description("Explore temples")
                .durationDays(3)
                .basePrice(new BigDecimal("120.00"))
                .build();

        Tour savedTour = Tour.builder()
                .tourId(1L)
                .title("Angkor Discovery")
                .description("Explore temples")
                .durationDays(3)
                .basePrice(new BigDecimal("120.00"))
                .images(new ArrayList<>())
                .schedules(new ArrayList<>())
                .build();

        when(tourRepository.save(any(Tour.class))).thenReturn(savedTour);

        TourResponse response = tourService.createTour(request);

        assertNotNull(response);
        assertEquals(1L, response.getTourId());
        assertEquals("Angkor Discovery", response.getTitle());
        verify(tourRepository, atLeastOnce()).save(any(Tour.class));
    }

    @Test
    void createTourWithCategoryAssociatesCategory() {
        TourRequest request = TourRequest.builder()
                .title("Island Escape")
                .durationDays(2)
                .basePrice(new BigDecimal("90.00"))
                .categoryId(5L)
                .build();

        Category category = new Category(5L, "Islands", "Island trips");
        when(categoryRepository.findById(5L)).thenReturn(Optional.of(category));

        Tour savedTour = Tour.builder()
                .tourId(2L)
                .title("Island Escape")
                .durationDays(2)
                .basePrice(new BigDecimal("90.00"))
                .category(category)
                .images(new ArrayList<>())
                .schedules(new ArrayList<>())
                .build();

        when(tourRepository.save(any(Tour.class))).thenReturn(savedTour);

        TourResponse response = tourService.createTour(request);

        assertNotNull(response);
        assertEquals(5L, response.getCategoryId());
        assertEquals("Islands", response.getCategoryName());
    }

    @Test
    void getTourByIdReturnsTourWhenFound() {
        Tour tour = Tour.builder()
                .tourId(1L)
                .title("Historical Tour")
                .durationDays(1)
                .basePrice(new BigDecimal("50.00"))
                .images(new ArrayList<>())
                .schedules(new ArrayList<>())
                .build();

        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));

        TourResponse response = tourService.getTourById(1L);

        assertEquals(1L, response.getTourId());
        assertEquals("Historical Tour", response.getTitle());
    }

    @Test
    void getTourByIdThrowsWhenNotFound() {
        when(tourRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tourService.getTourById(99L));
    }

    @Test
    void updateTourModifiesFields() {
        Tour existing = Tour.builder()
                .tourId(1L)
                .title("Old Title")
                .durationDays(1)
                .basePrice(BigDecimal.TEN)
                .images(new ArrayList<>())
                .schedules(new ArrayList<>())
                .build();

        TourRequest updateRequest = TourRequest.builder()
                .title("Updated Title")
                .durationDays(2)
                .basePrice(new BigDecimal("20.00"))
                .build();

        when(tourRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(tourRepository.save(any(Tour.class))).thenAnswer(i -> i.getArgument(0));

        TourResponse response = tourService.updateTour(1L, updateRequest);

        assertEquals("Updated Title", response.getTitle());
        assertEquals(2, response.getDurationDays());
        assertEquals(new BigDecimal("20.00"), response.getBasePrice());
    }

    @Test
    void deleteTourDeletesSuccessfully() {
        Tour tour = Tour.builder()
                .tourId(1L)
                .title("Tour to delete")
                .images(new ArrayList<>())
                .schedules(new ArrayList<>())
                .build();

        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));

        tourService.deleteTour(1L);

        verify(tourRepository).delete(tour);
    }

    @Test
    void addImageToTourAddsNewImage() {
        Tour tour = Tour.builder()
                .tourId(1L)
                .title("Tour With Image")
                .images(new ArrayList<>())
                .schedules(new ArrayList<>())
                .build();

        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));
        when(tourRepository.save(any(Tour.class))).thenAnswer(i -> i.getArgument(0));

        TourResponse response = tourService.addImageToTour(1L, "https://example.com/img.jpg", true);

        assertNotNull(response);
        assertEquals(1, response.getImages().size());
        assertEquals("https://example.com/img.jpg", response.getPrimaryImageUrl());
    }
}
