package org.example.service;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Service;
import org.teamview.dto.NewEmployeeDTO;
import org.teamview.enums.SeniorityLevel;
import org.teamview.exception.BadRequestException;
import org.teamview.model.User;
import org.teamview.repository.DynamoBuilder;

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
            // todo: add team's project to employee's project list
            // todo: USER#1 | PROJECT#projID | i ostali atributi projekta koji treba da se prikazu
        }
        repo.saveUser(employee);
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

        if (user.getTeamId() != null && !user.getTeamId().equals(newEmployee.getTeam().getId())) {
            // replacing team
            repo.deleteUser(user);
            user.setTeamId(newEmployee.getTeam().getId());
            user.setTeamLead(false);
            // todo: add project to all user's project
        } else if (user.getTeamId() == null && newEmployee.getTeam().getId() != null) {
            repo.deleteUser(user);
            user.setTeamId(newEmployee.getTeam().getId());
            user.setTeamLead(false);
            // todo: add project to all user's project
        }
    }
}
