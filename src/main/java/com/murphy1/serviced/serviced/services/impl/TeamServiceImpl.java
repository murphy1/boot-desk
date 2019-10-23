package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.model.Admin;
import com.murphy1.serviced.serviced.model.Agent;
import com.murphy1.serviced.serviced.model.Team;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.repositories.AdminRepository;
import com.murphy1.serviced.serviced.repositories.AgentRepository;
import com.murphy1.serviced.serviced.repositories.TeamsRepository;
import com.murphy1.serviced.serviced.services.TeamService;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    private TeamsRepository teamsRepository;
    private UserService userService;
    private AdminRepository adminRepository;
    private AgentRepository agentRepository;

    public TeamServiceImpl(TeamsRepository teamsRepository, UserService userService, AdminRepository adminRepository, AgentRepository agentRepository) {
        this.teamsRepository = teamsRepository;
        this.userService = userService;
        this.adminRepository = adminRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        teamsRepository.findAll().forEach(teams::add);
        return teams;
    }

    @Override
    public Team updateTeam(Long id) {

        Optional<Team> teamOptional = teamsRepository.findById(id);

        if (!teamOptional.isPresent()) {
            throw new BadRequestException("That team does not exist!");
        }

        return teamOptional.get();
    }

    @Override
    public Team saveTeam(Team team) {
        List<Team> teams = getAllTeams();

        for (Team team1 : teams){
            if (team.getName().equalsIgnoreCase(team1.getName())){
                throw new BadRequestException("A team already exists with that name");
            }
        }

        User user = userService.findUserByUsername(team.getTeamLeader().getUsername());
        if (!user.getRoles().equals("ADMIN")){
            throw new BadRequestException("Only Admins can be Team Leaders!");
        }

        User addUser = userService.findUserByUsername(team.getAddMember());
        if (!addUser.getRoles().equals("AGENT")){
            throw new BadRequestException("User is not an Agent!");
        }
        Agent agent = userService.convertUserToAgent(addUser);

        Admin admin = userService.convertUserToAdmin(user);
        team.setTeamLeader(admin);

        if (team.getTeamMembers() == null){
            List<Agent> agents = new ArrayList<>();
            agents.add(agent);
            team.setTeamMembers(agents);
        }else{
            team.getTeamMembers().add(agent);
        }

        teamsRepository.save(team);

        admin.setTeam(team);
        adminRepository.save(admin);
        agent.setTeam(team);
        agentRepository.save(agent);

        return team;
    }

    @Override
    public void deleteTeamById(Long id) {
        Optional<Admin> admin = adminRepository.findByUsername(userService.getCurrentUserName());
        admin.get().setTeam(null);

        teamsRepository.deleteById(id);

        String i = "i";
    }
}
