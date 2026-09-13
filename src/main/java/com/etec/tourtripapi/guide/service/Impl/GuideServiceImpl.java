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
        try {
            return repoGuide.findAll();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to retrieve guides", exception);
        }
    }

    @Override
    public Guides getByFullName(String fullName) {
        try {
            return repoGuide.findByFullName(fullName).orElse(null);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to retrieve guide by full name", exception);
        }
    }

    @Override
    public Guides getByEmail(String email) {
        try {
            return repoGuide.findByEmail(email).orElse(null);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to retrieve guide by email", exception);
        }
    }

    @Override
    public Guides create(Guides guide) {
        try {
            return repoGuide.save(guide);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create guide", exception);
        }
    }

    @Override
    public Guides update(Long id, Guides guide) {
        try {
            Guides existingGuide = repoGuide.findById(id)
                    .orElseThrow(() -> new RuntimeException("Guide not found with id: " + id));

            existingGuide.setFullName(guide.getFullName());
            existingGuide.setEmail(guide.getEmail());
            existingGuide.setPhoneNumber(guide.getPhoneNumber());
            existingGuide.setGuideUrl(guide.getGuideUrl());

            return repoGuide.save(existingGuide);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to update guide with id: " + id, exception);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Guides existingGuide = repoGuide.findById(id)
                    .orElseThrow(() -> new RuntimeException("Guide not found with id: " + id));
            repoGuide.delete(existingGuide);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to delete guide with id: " + id, exception);
        }
    }
}
