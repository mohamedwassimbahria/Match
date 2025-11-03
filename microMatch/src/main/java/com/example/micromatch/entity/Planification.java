package com.example.micromatch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "planifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planification {

    @Id
    private String id;
    private String matchId;
    private LocalDateTime datePropose;
    private String statut;
    private List<String> contraintes;
    private List<String> historiqueModifications;
}
