package com.example.micromatch.controller;

import com.example.micromatch.service.ReferenceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reference-data")
@RequiredArgsConstructor
public class ReferenceDataController {

    private final ReferenceDataService referenceDataService;

    @GetMapping("/referees")
    public List<String> getTunisianReferees() {
        return referenceDataService.getTunisianReferees();
    }

    @GetMapping("/coaches")
    public List<String> getTunisianCoaches() {
        return referenceDataService.getTunisianCoaches();
    }
}
