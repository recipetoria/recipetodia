package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;


}
