package org.example.service;

import org.springframework.stereotype.Service;
import org.teamview.dto.EmployeeDTO;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.repository.DynamoBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    public List<EmployeeDTO> getEmployees() {
        List<EmployeeDTO> retList = new ArrayList<>();
        DynamoBuilder repository = DynamoBuilder.createBuilder();

        List<User> users = repository.getAllUsers();

        for (User user : users) {
            Team team = getUsersTeam(user);
            Project project = getProjectForTeam(team);
            List<User> membersOfTeam = getMembersOfTheTeam(team);

            retList.add(new EmployeeDTO(user, team, project, membersOfTeam));
        }
        return retList;
    }

    private List<User> getMembersOfTheTeam(Team team) {
        if (team == null) return null;

        return DynamoBuilder.createBuilder().getMembersOfTheTeam(team.getId());
    }

    private Project getProjectForTeam(Team team) {
        if (team == null) return null;

        return DynamoBuilder.createBuilder().getProjectForTeam(team.getId());
    }

    private Team getUsersTeam(User user) {
        if (user.getTeamId() == null) return null; // todo: check if it returns null if there is no column?

        return DynamoBuilder.createBuilder().getTeam(user.getTeamId());
    }
}
