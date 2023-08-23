package org.example.service;

import com.amazonaws.services.dynamodbv2.datamodeling.TransactionLoadRequest;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import org.springframework.stereotype.Service;
import org.teamview.dto.TeamDTO;
import org.teamview.exception.BadRequestException;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.repository.DynamoBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    public void deleteTeam(String id) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();
        Team team = repo.getTeam(id);
        if (team == null) throw new BadRequestException("Team with id: <" + id + "> doesn't exist!");

        removeMembersFromTeam(repo, team);
        removeProjectsFromTeam(repo, team);

        repo.deleteTeam(team);
    }

    private void removeProjectsFromTeam(DynamoBuilder repo, Team team) {
        Project teamsProject = repo.getProjectForTeam(team.getId());
        if (teamsProject != null) {
            repo.deleteProject(teamsProject);
            teamsProject.setTeamId(null);
            repo.saveProject(teamsProject);
        }
    }

    private void removeMembersFromTeam(DynamoBuilder repo, Team team) {
        List<User> members = repo.getMembersOfTheTeam(team.getId());
        members.forEach(member -> {
            repo.deleteUser(member);
            member.setTeamLead(false);
            member.setTeamId(null);
        });
        repo.batchSaveUsers(members);
    }
}
