package org.example.service;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.NewTeamDTO;
import org.teamview.exception.BadRequestException;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.repository.DynamoBuilder;

import java.util.List;

@Service
public class TeamService {

    public void newTeam(NewTeamDTO newTeam) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        Team team = new Team();
        team.setId(UlidCreator.getUlid().toLowerCase());
        team.setTeamName(newTeam.getName());

        for (EmployeeDTO user : newTeam.getMembers()) {

            String teamId = (user.getTeam() != null && user.getTeam().getId() != null) ? user.getTeam().getId() : null;
            System.out.println("TeamService->newTeam -> teamId for selectedEmployee: " + teamId);
            User dynamoUser = repo.getUser(user.getId(), teamId);
            if (dynamoUser == null) throw new BadRequestException("Employee doesn't exist!");
            repo.deleteUser(dynamoUser);    // deleting user so I can update his PK

            dynamoUser.setTeamId(team.getId()); // new team
            dynamoUser.setTeamLead(false);
            if (user.getId().equals(newTeam.getLead().getId())) {
                dynamoUser.setTeamLead(true);
                team.setTeamLead(dynamoUser);
                // todo: check if he already was team lead for previous team and clear that
            }
            repo.saveUser(dynamoUser);
        }

        repo.saveTeam(team);
    }

    public void editTeam(NewTeamDTO newTeam) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        Team team = repo.getTeam(newTeam.getId());
        if (team == null) throw new BadRequestException("Team doesn't exist!");

        List<User> teamMembers = repo.getMembersOfTheTeam(team.getId());

        removeExMembers(newTeam, teamMembers, repo);
        addNewMembers(newTeam, team, repo);
        changeTeamLead(newTeam, team, repo);

        team.setTeamName(newTeam.getName());
        repo.saveTeam(team);
    }

    private void changeTeamLead(NewTeamDTO newTeam, Team team, DynamoBuilder repo) {
        if (newTeam.getLead().getId() != null) {
            if (!team.getTeamLead().getId().equals(newTeam.getLead().getId())) {
                team.getTeamLead().setTeamLead(false);
                repo.saveUser(team.getTeamLead());

                String teamId = (newTeam.getLead().getTeam() == null || newTeam.getLead().getTeam().getId() == null) ?
                        null : newTeam.getLead().getTeam().getId();
                User newLead = repo.getUser(newTeam.getLead().getId(), teamId);
                newLead.setTeamLead(true);
                if (!team.getId().equals(newLead.getTeamId()))
                    throw new BadRequestException("ERROR - This shouldn't happened! -> TeamId of new TeamLead != teamId");
                repo.saveUser(newLead);
                team.setTeamLead(newLead);
            }
        }
    }

    private void addNewMembers(NewTeamDTO newTeam, Team team, DynamoBuilder repo) {
        // adding new members
        for (EmployeeDTO empDTO : newTeam.getMembers()) {
            if (empDTO.getTeam() == null || !team.getId().equals(empDTO.getTeam().getId())) {
                String teamId = (empDTO.getTeam() == null || empDTO.getTeam().getId() == null) ? null : empDTO.getTeam().getId();
                User user = repo.getUser(empDTO.getId(), teamId);
                repo.deleteUser(user);
                // todo: check if he was team lead in previous team and clear that in Team object
                user.setTeamLead(false);
                user.setTeamId(team.getId());
                repo.saveUser(user);
            }
        }
    }

    private void removeExMembers(NewTeamDTO newTeam, List<User> teamMembers, DynamoBuilder repo) {
        // remove members that are no longer part of the team
        for (User member : teamMembers) {
            if (newTeam.getMembers().stream().noneMatch(m -> m.getId().equals(member.getId()))) {
                repo.deleteUser(member);
                member.setTeamLead(false);
                member.setTeamId(null);
                repo.saveUser(member);
            }
        }
    }
}
