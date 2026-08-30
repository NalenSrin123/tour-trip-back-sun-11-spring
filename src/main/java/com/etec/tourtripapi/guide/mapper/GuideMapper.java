package com.etec.tourtripapi.guide.mapper;

import com.etec.tourtripapi.guide.dto.request.GuideRequest;
import com.etec.tourtripapi.guide.dto.response.GuideResponse;
import com.etec.tourtripapi.guide.entity.Guides;

public class GuideMapper {

    public static Guides toEntity(GuideRequest request) {

        Guides guide = new Guides();

        guide.setFullName(request.getName());
        guide.setPhoneNumber(request.getPhoneNumber());
        guide.setEmail(request.getEmail());

        return guide;
    }

    public static GuideResponse toResponse(Guides guide) {

        GuideResponse response = new GuideResponse();

        response.setId(guide.getId());
        response.setName(guide.getFullName());
        response.setPhoneNumber(guide.getPhoneNumber());
        response.setEmail(guide.getEmail());

        return response;
    }
}
