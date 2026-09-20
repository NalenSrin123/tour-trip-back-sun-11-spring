package com.etec.tourtripapi.tour.mapper;

import com.etec.tourtripapi.category.entity.Category;
import com.etec.tourtripapi.tour.dto.request.TourRequest;
import com.etec.tourtripapi.tour.dto.response.TourResponse;
import com.etec.tourtripapi.tour.entity.Tour;
import com.etec.tourtripapi.tour.entity.TourImage;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TourMapperTest {

    private final TourMapper tourMapper = new TourMapper();

    @Test
    void toEntityMapsFieldsCorrectly() {
        TourRequest request = TourRequest.builder()
                .title("Koh Rong Adventure")
                .description("Tropical beach vacation")
                .durationDays(4)
                .basePrice(new BigDecimal("220.00"))
                .build();

        Tour entity = tourMapper.toEntity(request);

        assertNotNull(entity);
        assertEquals("Koh Rong Adventure", entity.getTitle());
        assertEquals("Tropical beach vacation", entity.getDescription());
        assertEquals(4, entity.getDurationDays());
        assertEquals(new BigDecimal("220.00"), entity.getBasePrice());
    }

    @Test
    void toResponseMapsAllFieldsIncludingCategoryAndImages() {
        Category category = new Category(10L, "Beaches", "Coastal escapes");

        Tour tour = Tour.builder()
                .tourId(1L)
                .title("Phnom Penh Heritage")
                .description("Historical city tour")
                .durationDays(2)
                .basePrice(new BigDecimal("80.00"))
                .category(category)
                .images(new ArrayList<>())
                .schedules(new ArrayList<>())
                .build();

        TourImage primaryImage = TourImage.builder()
                .tourImageId(100L)
                .imageUrl("https://example.com/primary.jpg")
                .isPrimary(true)
                .tour(tour)
                .build();
        tour.setPrimaryImage(primaryImage);
        tour.addImage(primaryImage);

        TourResponse response = tourMapper.toResponse(tour);

        assertNotNull(response);
        assertEquals(1L, response.getTourId());
        assertEquals("Phnom Penh Heritage", response.getTitle());
        assertEquals(10L, response.getCategoryId());
        assertEquals("Beaches", response.getCategoryName());
        assertEquals("https://example.com/primary.jpg", response.getPrimaryImageUrl());
        assertEquals(1, response.getImages().size());
        assertEquals(0, response.getScheduleCount());
    }

    @Test
    void updateEntityUpdatesFields() {
        Tour tour = Tour.builder()
                .title("Old Title")
                .description("Old Desc")
                .durationDays(1)
                .basePrice(BigDecimal.TEN)
                .build();

        TourRequest updateRequest = TourRequest.builder()
                .title("New Title")
                .description("New Desc")
                .durationDays(5)
                .basePrice(new BigDecimal("99.99"))
                .build();

        tourMapper.updateEntity(tour, updateRequest);

        assertEquals("New Title", tour.getTitle());
        assertEquals("New Desc", tour.getDescription());
        assertEquals(5, tour.getDurationDays());
        assertEquals(new BigDecimal("99.99"), tour.getBasePrice());
    }
}
