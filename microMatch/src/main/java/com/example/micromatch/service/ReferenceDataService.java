package com.example.micromatch.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ReferenceDataService {

    public List<String> getTunisianReferees() {
        return Arrays.asList(
                "Sadok Selmi",
                "Youssef Srairi",
                "Haythem Guirat",
                "Naim Hosni",
                "Oussama Rezgallah"
        );
    }

    public List<String> getTunisianCoaches() {
        return Arrays.asList(
                "Mondher Kebaier",
                "Faouzi Benzarti",
                "Nabil Maaloul",
                "Lassaad Dridi",
                "Sami Trabelsi"
        );
    }
}
