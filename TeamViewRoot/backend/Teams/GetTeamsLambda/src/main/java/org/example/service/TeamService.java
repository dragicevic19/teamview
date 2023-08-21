package org.example.service;

import org.springframework.stereotype.Service;
import org.teamview.dto.TeamDTO;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.repository.DynamoBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    public List<TeamDTO> getTeams() {
        DynamoBuilder repository = DynamoBuilder.createBuilder();
        List<TeamDTO> retList = new ArrayList<>();

        List<Team> teams = repository.getAllTeams();

        for (Team team : teams) {
            List<User> members = repository.getMembersOfTheTeam(team.getId());
            Project project = repository.getProjectForTeam(team.getId());
            retList.add(new TeamDTO(team, members, project));
        }
        return retList;
    }
}
