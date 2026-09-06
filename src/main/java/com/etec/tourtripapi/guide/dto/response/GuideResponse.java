package com.etec.tourtripapi.guide.dto.response;

import lombok.Data;

@Data
public class GuideResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
}
