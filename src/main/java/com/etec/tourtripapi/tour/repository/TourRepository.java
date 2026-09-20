package com.etec.tourtripapi.tour.repository;

import com.etec.tourtripapi.tour.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long>, JpaSpecificationExecutor<Tour> {

    List<Tour> findByCategory_CategoryId(Long categoryId);

    boolean existsByTitleIgnoreCase(String title);

    boolean existsByTitleIgnoreCaseAndTourIdNot(String title, Long tourId);
}
