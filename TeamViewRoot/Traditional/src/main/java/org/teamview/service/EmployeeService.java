package org.teamview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.NewEmployeeDTO;
import org.teamview.dto.PaginationDTO;
import org.teamview.enums.SeniorityLevel;
import org.teamview.exception.BadRequestException;
import org.teamview.exception.NotFoundException;
import org.teamview.model.Employee;
import org.teamview.model.Team;
import org.teamview.repository.EmployeeRepository;
import org.teamview.repository.TeamRepository;

import java.util.ArrayList;
import java.util.HashSet;
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

    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Employee doesn't exists!"));
    }

    public EmployeeDTO newEmployee(NewEmployeeDTO newEmployee) {

        Employee employee = new Employee();
        employee.setName(newEmployee.getName());
        employee.setLastName(newEmployee.getLastName());
        employee.setEmail(newEmployee.getEmail());
        employee.setPosition(newEmployee.getPosition());
        employee.setSeniority(SeniorityLevel.valueOf(newEmployee.getSeniority()));
        employee.setPastProjects(new HashSet<>());
        employee.setDeleted(false);
        employee.setAddress(newEmployee.getAddress());

        if (newEmployee.getTeam() != null && newEmployee.getTeam().getId() != null) {
            Team team = findTeamById(newEmployee.getTeam().getId());
            employee.setTeam(team);
            if (team.getProject() != null) {
                employee.getPastProjects().add(team.getProject());
            }
        }

        return new EmployeeDTO(employeeRepository.save(employee));
    }

    private Team findTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Team doesn't exists!"));
    }

    public EmployeeDTO editEmployee(Long id, NewEmployeeDTO editEmployee) {
        Employee employee = findEmployeeById(id);

        employee.setName(editEmployee.getName());
        employee.setLastName(editEmployee.getLastName());
        employee.setAddress(editEmployee.getAddress());
        employee.setPosition(editEmployee.getPosition());
        employee.setSeniority(SeniorityLevel.valueOf(editEmployee.getSeniority()));

        editTeamForEmployee(employee, editEmployee);

        return new EmployeeDTO(employeeRepository.save(employee));
    }

    private void editTeamForEmployee(Employee employee, NewEmployeeDTO editEmployee) {

        if (employee.getTeam() != null && editEmployee.getTeam().getId() != null         // same team as before
                && employee.getTeam().getId().equals(editEmployee.getTeam().getId())) {
            return;
        }

        if (employee.getTeam() != null && editEmployee.getTeam().getId() == null) {     // removing from team
            if (employee.isTeamLead()) {
                employee.getTeam().setTeamLead(null);
                employee.setTeamLead(false);
            }
            employee.setTeam(null);
        }

        if (editEmployee.getTeam().getId() != null) {                                   // new team
            Team team = findTeamById(editEmployee.getTeam().getId());
            employee.setTeam(team);
        }
    }

    public void deleteEmployee(Long id) {
        Employee employee = findEmployeeById(id);
        employee.setDeleted(true);
        if (employee.isTeamLead()) {
            employee.setTeamLead(false);
            employee.getTeam().setTeamLead(null);
        }
        employee.setTeam(null);
        employeeRepository.save(employee);
    }


}
