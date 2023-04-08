package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.repository.TagRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;


}
