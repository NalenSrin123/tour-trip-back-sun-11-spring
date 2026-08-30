package com.etec.tourtripapi.guide.service.Impl;

import com.etec.tourtripapi.guide.entity.Guides;
import com.etec.tourtripapi.guide.service.GuideService;

import java.util.List;

public class GuideServiceImpl implements GuideService {

    @Override
    public List<Guides> getAll() {
        return List.of();
    }

    @Override
    public Guides getByFullName(String fullName) {
        return null;
    }

    @Override
    public Guides getByEmail(String email) {
        return null;
    }

    @Override
    public Guides create(Guides guide) {
        return null;
    }

    @Override
    public Guides update(Long id, Guides guide) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
