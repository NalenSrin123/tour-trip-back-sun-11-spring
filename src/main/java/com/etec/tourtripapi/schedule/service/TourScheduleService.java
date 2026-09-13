package com.etec.tourtripapi.schedule.service;

import com.etec.tourtripapi.schedule.dto.request.TourScheduleRequest;
import com.etec.tourtripapi.schedule.dto.response.TourScheduleResponse;

import java.util.List;

public interface TourScheduleService {

    TourScheduleResponse createSchedule(TourScheduleRequest request);

    TourScheduleResponse getScheduleById(Long id);

    List<TourScheduleResponse> getAllSchedules();

    List<TourScheduleResponse> getSchedulesByTourId(Long tourId);

    List<TourScheduleResponse> getSchedulesByGuideId(Long guideId);

    TourScheduleResponse updateSchedule(Long id, TourScheduleRequest request);

    void deleteSchedule(Long id);
}
