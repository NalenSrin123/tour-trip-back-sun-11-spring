package com.etec.tourtripapi.guide.service.Impl;

import com.etec.tourtripapi.guide.entity.Guides;
import com.etec.tourtripapi.guide.repository.repoGuide;
import com.etec.tourtripapi.guide.service.GuideService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuideServiceImpl implements GuideService {

    private final repoGuide repoGuide;

    public GuideServiceImpl(repoGuide repoGuide) {
        this.repoGuide = repoGuide;
    }

    @Override
    public List<Guides> getAll() {
        return repoGuide.findAll();
    }

    @Override
    public Guides getByFullName(String fullName) {
        return repoGuide.findByFullName(fullName).orElse(null);
    }

    @Override
    public Guides getByEmail(String email) {
        return repoGuide.findByEmail(email).orElse(null);
    }

    @Override
    public Guides create(Guides guide) {
        return repoGuide.save(guide);
    }

    @Override
    public Guides update(Long id, Guides guide) {
        Guides existingGuide = repoGuide.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide not found with id: " + id));

        if (guide.getFullName() != null) {
            existingGuide.setFullName(guide.getFullName());
        }
        if (guide.getEmail() != null) {
            existingGuide.setEmail(guide.getEmail());
        }
        if (guide.getPhoneNumber() != null) {
            existingGuide.setPhoneNumber(guide.getPhoneNumber());
        }
        if (guide.getGuideUrl() != null) {
            existingGuide.setGuideUrl(guide.getGuideUrl());
        }

        return repoGuide.save(existingGuide);
    }

    @Override
    public void delete(Long id) {
        Guides existingGuide = repoGuide.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide not found with id: " + id));
        repoGuide.delete(existingGuide);
    }
}
