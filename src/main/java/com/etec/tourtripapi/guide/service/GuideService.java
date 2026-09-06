package com.etec.tourtripapi.guide.service;

import com.etec.tourtripapi.guide.entity.Guides;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GuideService {

    List<Guides> getAll();

    Guides getByFullName(String fullName);

    Guides getByEmail(String email);

    Guides create(Guides guide);

    Guides update(Long id, Guides guide);

    void delete(Long id);
}
