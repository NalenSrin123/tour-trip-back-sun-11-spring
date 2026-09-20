package com.etec.tourtripapi.review.repository;

import com.etec.tourtripapi.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

    List<Review> findByTourId(Long tourId);

    List<Review> findByTourIdAndStatusIgnoreCase(Long tourId, String status);

    List<Review> findByUserId(Long userId);

    List<Review> findByStatusIgnoreCase(String status);

    boolean existsByUserIdAndTourId(Long userId, Long tourId);

    long countByTourIdAndStatusIgnoreCase(Long tourId, String status);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.tourId = :tourId AND UPPER(r.status) = 'APPROVED'")
    Double calculateAverageRatingForApproved(@Param("tourId") Long tourId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.tourId = :tourId")
    Double calculateAverageRating(@Param("tourId") Long tourId);
}
