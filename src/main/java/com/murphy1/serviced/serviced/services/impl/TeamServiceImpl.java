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
        Team teamToUpdate = null;

        if (team.getId() == null){
            for (Team team1 : teams){
                if (team.getName().equalsIgnoreCase(team1.getName())){
                    throw new BadRequestException("A team already exists with that name");
                }
            }
            teamToUpdate = team;
            team.setProgressToTarget(0L);
        }
        else{
            Optional<Team> teamOptional = teamsRepository.findById(team.getId());
            teamToUpdate = teamOptional.get();
        }

        User user = userService.findUserByUsername(team.getTeamLeader().getUsername());
        if (!user.getRoles().equals("ADMIN")){
            throw new BadRequestException("Only Admins can be Team Leaders!");
        }

        Admin admin = userService.convertUserToAdmin(user);
        teamToUpdate.setTeamLeader(admin);

        teamToUpdate.setName(team.getName());
        teamToUpdate.setTarget(team.getTarget());

        if (!team.getAddMember().equals("")){
            User addUser = userService.findUserByUsername(team.getAddMember());
            if (!addUser.getRoles().equals("AGENT")){
                throw new BadRequestException("User is not an Agent!");
            }
            Agent agent = userService.convertUserToAgent(addUser);

            if (teamToUpdate.getTeamMembers() == null){
                List<Agent> agents = new ArrayList<>();
                agents.add(agent);
                teamToUpdate.setTeamMembers(agents);
            }else{
                List<Agent> agents = teamToUpdate.getTeamMembers();
                for (Agent agent1 : agents){
                    if (agent1.getUsername().equals(team.getAddMember())){
                        throw new BadRequestException("That Agent is already a member of the team!");
                    }
                }
                teamToUpdate.getTeamMembers().add(agent);
            }
            teamsRepository.save(teamToUpdate);
            admin.setTeam(teamToUpdate);
            adminRepository.save(admin);
            agent.setTeam(teamToUpdate);
            agentRepository.save(agent);

            return teamToUpdate;
        }
        if (!team.getRemoveMember().equals("")){
            List<String> usernames = new ArrayList<>();
            List<Agent> agents = teamToUpdate.getTeamMembers();
            for (Agent agent : agents){
                usernames.add(agent.getUsername());
            }
            if (!usernames.contains(team.getRemoveMember())){
                throw new BadRequestException("That user is not a member of the team!");
            }
            Optional<Agent> agentOptional = agentRepository.findByUsername(team.getRemoveMember());
            Agent agent = agentOptional.get();
            agent.setTeam(null);
            agentRepository.save(agent);
            teamToUpdate.getTeamMembers().remove(agent);
        }

        teamsRepository.save(teamToUpdate);

        admin.setTeam(teamToUpdate);
        adminRepository.save(admin);

        return teamToUpdate;
    }

    @Override
    public void deleteTeamById(Long id) {
        // Remove the team from the team leader
        Optional<Admin> admin = adminRepository.findByUsername(userService.getCurrentUserName());
        admin.get().setTeam(null);

        //Remove the team frm any associated team member
        List<Agent> agents = teamsRepository.findById(id).get().getTeamMembers();
        for (Agent agent : agents){
            agent.setTeam(null);
        }

        teamsRepository.deleteById(id);
    }
}
