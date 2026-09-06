package com.etec.tourtripapi.guide.controller;

import com.etec.tourtripapi.guide.dto.request.GuideRequest;
import com.etec.tourtripapi.guide.dto.response.GuideResponse;
import com.etec.tourtripapi.guide.entity.Guides;
import com.etec.tourtripapi.guide.mapper.GuideMapper;
import com.etec.tourtripapi.guide.service.GuideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guides")
public class GuideController {
    private final GuideService guideService;

    public GuideController(GuideService guideService) {
        this.guideService = guideService;
    }

    @GetMapping
    public ResponseEntity<List<GuideResponse>> getAll() {
        List<GuideResponse> guides = guideService.getAll().stream()
                .map(GuideMapper::toResponse)
                .toList();
        return ResponseEntity.ok(guides);
    }

    @PostMapping
    public ResponseEntity<GuideResponse> create(@RequestBody GuideRequest request) {
        Guides guide = GuideMapper.toEntity(request);
        Guides createdGuide = guideService.create(guide);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GuideMapper.toResponse(createdGuide));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuideResponse> update(
            @PathVariable Long id,
            @RequestBody GuideRequest request) {

        Guides guide = GuideMapper.toEntity(request);
        Guides updatedGuide = guideService.update(id, guide);
        return ResponseEntity.ok(GuideMapper.toResponse(updatedGuide));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        guideService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
