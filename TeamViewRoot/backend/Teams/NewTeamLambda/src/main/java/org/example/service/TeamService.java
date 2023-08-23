package org.example.service;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.NewTeamDTO;
import org.teamview.exception.BadRequestException;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.model.UserProject;
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
            addTeamMember(newTeam, user, repo, team);
        }
        repo.saveTeam(team);
    }

    private void addTeamMember(NewTeamDTO newTeam, EmployeeDTO user, DynamoBuilder repo, Team team) {
        String teamId = user.getTeamId();
        System.out.println("TeamService->newTeam -> teamId for selectedEmployee: " + teamId);
        User dynamoUser = repo.getUser(user.getId(), teamId);
        removeFromPreviousTeam(repo, dynamoUser);
        addToNewTeam(newTeam, team, dynamoUser);
        repo.saveUser(dynamoUser);
    }

    private void addToNewTeam(NewTeamDTO newTeam, Team team, User dynamoUser) {
        dynamoUser.setTeamId(team.getId());
        dynamoUser.setTeamLead(false);
        if (dynamoUser.getId().equals(newTeam.getLead().getId())) {
            dynamoUser.setTeamLead(true);
            team.setTeamLead(dynamoUser);
        }
    }

    private void removeFromPreviousTeam(DynamoBuilder repo, User dynamoUser) {
        if (dynamoUser == null) throw new BadRequestException("Employee doesn't exist!");
        repo.deleteUser(dynamoUser);    // deleting user so I can update his PK

        if (dynamoUser.getTeamLead() && dynamoUser.getTeamId() != null) {
            removeLeadFromTeam(repo, dynamoUser);
        }
    }

    private void removeLeadFromTeam(DynamoBuilder repo, User user) {
        Team previousTeam = repo.getTeam(user.getTeamId());
        previousTeam.setTeamLead(null);
        repo.saveTeam(previousTeam);
    }

    public void editTeam(NewTeamDTO newTeam) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        Team team = repo.getTeam(newTeam.getId());
        if (team == null) throw new BadRequestException("Team doesn't exist!");

        addNewMembers(newTeam, team, repo);
        changeTeamLead(newTeam, team, repo);
        removeExMembers(newTeam, team, repo);

        team.setTeamName(newTeam.getName());
        repo.saveTeam(team);
    }

    //
    private void changeTeamLead(NewTeamDTO newTeam, Team team, DynamoBuilder repo) {
        if (newTeam.getLead() != null && newTeam.getLead().getId() != null) {
            if (team.getTeamLead() == null) {
                setNewTeamLead(newTeam, team, repo);
            } else if (!team.getTeamLead().getId().equals(newTeam.getLead().getId())) {
                team.getTeamLead().setTeamLead(false);
                repo.saveUser(team.getTeamLead());  // ovde mislim da je greska jer ne brise lead- msm da ga vrati opet // todo: POPRAVLJENO?
                setNewTeamLead(newTeam, team, repo);
            }
        } else if (team.getTeamLead() != null) {
            // remove team lead
            team.getTeamLead().setTeamLead(false);
            repo.saveUser(team.getTeamLead());
            team.setTeamLead(null);
            repo.saveTeam(team);
        }
    }

    private void setNewTeamLead(NewTeamDTO newTeam, Team team, DynamoBuilder repo) {
        String teamId = team.getId(); // lead is already added in new team so his PK is TEAM#<newTeamId>
        User newLead = repo.getUser(newTeam.getLead().getId(), teamId);

        if (newLead == null)
            throw new BadRequestException("Employee with id: <" + newTeam.getLead().getId() + "> and teamId: <" + teamId + "> doesn't exist!");
        if (!team.getId().equals(newLead.getTeamId()))
            throw new BadRequestException("ERROR - This shouldn't happened! -> TeamId of new TeamLead != teamId");

        newLead.setTeamLead(true);
        repo.saveUser(newLead);
        team.setTeamLead(newLead);
    }

    private void addNewMembers(NewTeamDTO newTeam, Team team, DynamoBuilder repo) {
        for (EmployeeDTO empDTO : newTeam.getMembers()) {

            if (!team.getId().equals(empDTO.getTeamId())) {
                String teamId = empDTO.getTeamId();
                User newEmployee = repo.getUser(empDTO.getId(), teamId);
                repo.deleteUser(newEmployee);

                if (newEmployee.getTeamLead() && newEmployee.getTeamId() != null)
                    removeLeadFromTeam(repo, newEmployee);

                newEmployee.setTeamLead(false);
                newEmployee.setTeamId(team.getId());

                addTeamsProjectToUser(newEmployee);
                repo.saveUser(newEmployee);
            }
        }
    }

    private void addTeamsProjectToUser(User user) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        Project teamProject = repo.getProjectForTeam(user.getTeamId());
        if (teamProject != null) {
            UserProject usersProject = new UserProject(user.getId(), teamProject);
            repo.saveUsersProject(usersProject);
        }
    }


    private void removeExMembers(NewTeamDTO newTeam, Team team, DynamoBuilder repo) {
        // remove members that are no longer part of the team
        List<User> teamMembers = repo.getMembersOfTheTeam(team.getId());
        for (User member : teamMembers) {
            if (newTeam.getMembers().stream().noneMatch(m -> m.getId().equals(member.getId()))) {
                repo.deleteUser(member);
                member.setTeamLead(false);
                member.setTeamId(null);
                if (team.getTeamLead() != null && member.getId().equals(team.getTeamLead().getId())) {
                    team.setTeamLead(null);
                }
                repo.saveUser(member);
                System.out.println("Removed from team members: " + member.getId());
            }
        }
    }
}
