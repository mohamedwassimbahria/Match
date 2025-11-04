package com.example.micromatch.dto;

import com.example.micromatch.entity.Match;
import lombok.Data;

@Data
public class AddMediaRequest {
    private Match.Media media;
}
