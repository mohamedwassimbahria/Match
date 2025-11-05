package com.example.micromatch.service;

import com.example.micromatch.entity.Match;
import com.example.micromatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutomaticSchedulingService {

    private final MatchRepository matchRepository;

    public List<Match> scheduleMatches(List<String> teamIds) {
        List<String> teams = new ArrayList<>(teamIds);
        if (teams.size() % 2 != 0) {
            teams.add("BYE");
        }

        Collections.shuffle(teams);

        List<Match> scheduledMatches = new ArrayList<>();
        int numTeams = teams.size();
        int numRounds = numTeams - 1;

        for (int round = 0; round < numRounds; round++) {
            for (int i = 0; i < numTeams / 2; i++) {
                String team1 = teams.get(i);
                String team2 = teams.get(numTeams - 1 - i);

                if (!team1.equals("BYE") && !team2.equals("BYE")) {
                    Match match = new Match();
                    match.setTeam1Id(team1);
                    match.setTeam2Id(team2);
                    match.setDate(LocalDateTime.now().plusWeeks(round + 1));
                    scheduledMatches.add(match);
                }
            }

            // Rotate teams for the next round
            String lastTeam = teams.remove(numTeams - 1);
            teams.add(1, lastTeam);
        }

        return matchRepository.saveAll(scheduledMatches);
    }
}
