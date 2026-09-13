package com.etec.tourtripapi.schedule.repository;

import com.etec.tourtripapi.schedule.entity.TourSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TourScheduleRepository extends JpaRepository<TourSchedule, Long> {

    List<TourSchedule> findByTourId(Long tourId);

    @Query("SELECT s FROM TourSchedule s WHERE s.guide.id = :guideId")
    List<TourSchedule> findByGuideId(@Param("guideId") Long guideId);

    @Query("SELECT s FROM TourSchedule s WHERE s.guide.id = :guideId AND s.departureDate < :returnDate AND s.returnDate > :departureDate")
    List<TourSchedule> findOverlappingSchedulesForGuide(
            @Param("guideId") Long guideId,
            @Param("departureDate") LocalDateTime departureDate,
            @Param("returnDate") LocalDateTime returnDate
    );

    @Query("SELECT s FROM TourSchedule s WHERE s.guide.id = :guideId AND s.scheduleId != :excludeScheduleId AND s.departureDate < :returnDate AND s.returnDate > :departureDate")
    List<TourSchedule> findOverlappingSchedulesForGuideExcludingSchedule(
            @Param("guideId") Long guideId,
            @Param("departureDate") LocalDateTime departureDate,
            @Param("returnDate") LocalDateTime returnDate,
            @Param("excludeScheduleId") Long excludeScheduleId
    );
}
