package org.example.service;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Service;
import org.teamview.dto.NewEmployeeDTO;
import org.teamview.enums.SeniorityLevel;
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

        if (newEmployee.getTeam().getId() != null)
            employee.setTeamId(newEmployee.getTeam().getId());

        repo.saveUser(employee);
    }

}
