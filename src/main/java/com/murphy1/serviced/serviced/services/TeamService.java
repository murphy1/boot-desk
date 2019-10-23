package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.Team;

import java.util.List;

public interface TeamService {
    List<Team> getAllTeams();
    Team updateTeam(Long id);
    Team saveTeam(Team team);
    void deleteTeamById(Long id);
}
