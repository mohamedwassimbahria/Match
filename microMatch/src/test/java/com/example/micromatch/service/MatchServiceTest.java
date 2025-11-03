package com.example.micromatch.service;

import com.example.micromatch.entity.Match;
import com.example.micromatch.enums.MatchStatus;
import com.example.micromatch.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService matchService;

    @Test
    void createMatch() {
        Match match = new Match();
        match.setId("1");
        match.setStatus(MatchStatus.SCHEDULED.name());
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        Match created = matchService.createMatch("1", "2", LocalDateTime.now());
        assertEquals(MatchStatus.SCHEDULED.name(), created.getStatus());
    }

    @Test
    void startMatch() {
        Match match = new Match();
        match.setId("1");
        match.setStatus(MatchStatus.SCHEDULED.name());
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        Match started = matchService.startMatch("1");
        assertEquals(MatchStatus.LIVE.name(), started.getStatus());
    }
}
