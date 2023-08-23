package org.example.service;

import org.springframework.stereotype.Service;
import org.teamview.dto.EmployeeDTO;
import org.teamview.exception.BadRequestException;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.repository.DynamoBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    public void deleteEmployee(String id, String teamId) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();
        if (teamId.equals("null")) teamId = null;
        User employee = repo.getUser(id, teamId);

        if (employee == null)
            throw new BadRequestException("Employee with id: <" + id + ">, teamId: <" + teamId + "> doesn't exist!");

        if (employee.getTeamLead()) {
            removeLeadFromTeam(repo, employee);
        }

        repo.deleteUser(employee);
        repo.deleteUsersProjectsItems(employee);    // TODO: ne radi!
    }

    private void removeLeadFromTeam(DynamoBuilder repo, User employee) {
        Team team = repo.getTeam(employee.getTeamId());
        team.setTeamLead(null);
        repo.saveTeam(team);
    }
}
