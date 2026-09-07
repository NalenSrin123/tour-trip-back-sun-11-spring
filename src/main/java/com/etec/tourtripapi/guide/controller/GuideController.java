package com.etec.tourtripapi.guide.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etec.tourtripapi.guide.dto.request.GuideRequest;
import com.etec.tourtripapi.guide.dto.response.GuideResponse;
import com.etec.tourtripapi.guide.entity.Guides;
import com.etec.tourtripapi.guide.mapper.GuideMapper;
import com.etec.tourtripapi.guide.service.GuideService;

@RestController
@RequestMapping("/api/guides")
public class GuideController {
    private final GuideService guideService;

    public GuideController() {
        guideService = null;
    }

    @GetMapping
    public ResponseEntity<List<GuideResponse>> getAll() {
        List<GuideResponse> guides = guideService.getAll().stream()
                .map(GuideMapper::toResponse)
                .toList();
        return ResponseEntity.ok(guides);
    }

    @GetMapping("/fullname/{fullName}")
    public ResponseEntity<GuideResponse> getByFullName(@PathVariable String fullName)
    {
        Guides guide = guideService.getByFullName(fullName);
        if (guide != null) {
            return ResponseEntity.ok(GuideMapper.toResponse(guide));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<GuideResponse> getByEmail(@PathVariable String email) {
        Guides guide = guideService.getByEmail(email);
        if (guide != null) {
            return ResponseEntity.ok(GuideMapper.toResponse(guide));
        } else {
            return ResponseEntity.notFound().build();
        }
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
