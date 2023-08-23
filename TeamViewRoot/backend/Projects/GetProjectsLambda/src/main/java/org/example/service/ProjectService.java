package org.example.service;

import org.springframework.stereotype.Service;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.ProjectDTO;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.repository.DynamoBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    public List<ProjectDTO> getProjects() {
        List<ProjectDTO> retList = new ArrayList<>();
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        List<Project> projects = repo.getAllProjects();

        for (Project project : projects) {
            Team team = getTeamForProject(project);
            List<User> members = getMembersOfTheTeam(team);
            retList.add(new ProjectDTO(project, team, members));
        }

        return retList;
    }

    private Team getTeamForProject(Project project) {
        if (project.getTeamId() == null) return null;

        return DynamoBuilder.createBuilder().getTeam(project.getTeamId());
    }

    private List<User> getMembersOfTheTeam(Team team) {
        if (team == null) return null;

        return DynamoBuilder.createBuilder().getMembersOfTheTeam(team.getId());
    }
}
