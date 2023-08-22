package org.example.service;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Service;
import org.teamview.dto.NewEmployeeDTO;
import org.teamview.enums.SeniorityLevel;
import org.teamview.exception.BadRequestException;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.model.UserProject;
import org.teamview.repository.DynamoBuilder;

import java.util.List;

@Service
public class EmployeeService {

    public void newEmployee(NewEmployeeDTO newEmployee) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        User employee = new User();
        employee.setId(UlidCreator.getUlid().toLowerCase());
        employee.setFirstName(newEmployee.getName());
        employee.setLastName(newEmployee.getLastName());
        employee.setAddress(newEmployee.getAddress());
        employee.setEmail(newEmployee.getEmail());
        employee.setTeamLead(false);
        employee.setPosition(newEmployee.getPosition());
        employee.setSeniority(SeniorityLevel.valueOf(newEmployee.getSeniority()));

        if (newEmployee.getTeam().getId() != null) {
            employee.setTeamId(newEmployee.getTeam().getId());
            addTeamsProjectToUser(employee);
        }
        repo.saveUser(employee);
    }

    private void addTeamsProjectToUser(User employee) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        Project teamProject = repo.getProjectForTeam(employee.getTeamId());
        if (teamProject != null) {
            UserProject usersProject = new UserProject(employee.getId(), teamProject);
            repo.saveUsersProject(usersProject);
        }
    }

    public void editEmployee(NewEmployeeDTO newEmployee) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        User user = repo.getUser(newEmployee.getId(), newEmployee.getPrevTeamId());
        if (user == null) throw new BadRequestException("Employee doesn't exist!");

        updateTeamForEmployee(newEmployee, repo, user);

        user.setFirstName(newEmployee.getName());
        user.setLastName(newEmployee.getLastName());
        user.setPosition(newEmployee.getPosition());
        user.setSeniority(SeniorityLevel.valueOf(newEmployee.getSeniority()));
        user.setAddress(newEmployee.getAddress());

        repo.saveUser(user);
    }


    private void updateTeamForEmployee(NewEmployeeDTO newEmployee, DynamoBuilder repo, User user) {
        // if user has team AND new team is not the same -> replace with new team   OR
        // if user has not been in any team AND there is new team assigned -> replace NOTEAM with new team

        if (user.getTeamId() != null && !user.getTeamId().equals(newEmployee.getTeam().getId())) {
            if (user.getTeamLead()) {
                removeLeadFromTeam(repo, user);
            }
            changeTeam(newEmployee, repo, user);

        } else if (user.getTeamId() == null && newEmployee.getTeam().getId() != null) {
            changeTeam(newEmployee, repo, user);
        }
    }

    private void changeTeam(NewEmployeeDTO newEmployee, DynamoBuilder repo, User user) {
        repo.deleteUser(user);
        user.setTeamId(newEmployee.getTeam().getId());
        user.setTeamLead(false);
        addTeamsProjectToUser(user);
    }

    private static void removeLeadFromTeam(DynamoBuilder repo, User user) {
        Team previousTeam = repo.getTeam(user.getTeamId());
        previousTeam.setTeamLead(null);
        repo.saveTeam(previousTeam);
    }
}
