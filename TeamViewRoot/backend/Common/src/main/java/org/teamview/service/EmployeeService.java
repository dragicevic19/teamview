package org.teamview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.NewEmployeeDTO;
import org.teamview.dto.PaginationDTO;
import org.teamview.enums.SeniorityLevel;
import org.teamview.exception.NotFoundException;
import org.teamview.model.Employee;
import org.teamview.model.Team;
import org.teamview.repository.EmployeeRepository;
import org.teamview.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;

    public EmployeeService(EmployeeRepository employeeRepository, TeamRepository teamRepository) {
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
    }

    public PaginationDTO<EmployeeDTO> findAll(Pageable pageable) {

        Page<Employee> pagedEmps = this.employeeRepository.findAll(pageable);
        List<EmployeeDTO> dtos = new ArrayList<>();
        for (Employee emp : pagedEmps) {
            dtos.add(new EmployeeDTO(emp));
        }

        return new PaginationDTO<>(dtos, pagedEmps.getTotalElements());
    }

    public EmployeeDTO newEmployee(NewEmployeeDTO newEmployee) {

        Employee employee = new Employee();
        employee.setName(newEmployee.getName());
        employee.setLastName(newEmployee.getLastName());
        employee.setEmail(newEmployee.getEmail());
        employee.setPosition(newEmployee.getPosition());
        employee.setSeniority(SeniorityLevel.valueOf(newEmployee.getSeniority()));

        if (newEmployee.getTeam() != null && newEmployee.getTeam().getId() != null) {
            Team team = findTeamById(newEmployee.getTeam().getId());
            employee.setTeam(team);
        }

        return new EmployeeDTO(employeeRepository.save(employee));
    }

    private Team findTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Team doesn't exists!"));
    }
}
