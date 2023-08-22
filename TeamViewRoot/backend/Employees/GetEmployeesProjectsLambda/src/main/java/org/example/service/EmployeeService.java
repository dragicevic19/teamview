package org.example.service;

import org.springframework.stereotype.Service;
import org.teamview.dto.ProjectDTO;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.model.UserProject;
import org.teamview.repository.DynamoBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    public List<ProjectDTO> getEmployeesProject(String userId) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        List<UserProject> projects = repo.getUserProjects(userId);

        List<ProjectDTO> retList = new ArrayList<>();
        projects.forEach(project -> {
            Team team = repo.getTeam(project.getTeamId());
            List<User> members = repo.getMembersOfTheTeam(team.getId());
            retList.add(new ProjectDTO(project, team, members));
        });

        return retList;
    }
}
