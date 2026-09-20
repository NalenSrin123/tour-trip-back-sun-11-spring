package com.etec.tourtripapi.tour.repository;

import com.etec.tourtripapi.tour.entity.TourImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourImageRepository extends JpaRepository<TourImage, Long> {

    List<TourImage> findByTour_TourId(Long tourId);

    Optional<TourImage> findByTour_TourIdAndIsPrimaryTrue(Long tourId);

    void deleteByTour_TourId(Long tourId);
}
