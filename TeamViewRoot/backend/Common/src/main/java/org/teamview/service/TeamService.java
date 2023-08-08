package org.teamview.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.NewTeamDTO;
import org.teamview.dto.PaginationDTO;
import org.teamview.dto.TeamDTO;
import org.teamview.exception.NotFoundException;
import org.teamview.model.Employee;
import org.teamview.model.Team;
import org.teamview.repository.EmployeeRepository;
import org.teamview.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;

    public TeamService(TeamRepository teamRepository, EmployeeRepository employeeRepository) {
        this.teamRepository = teamRepository;
        this.employeeRepository = employeeRepository;
    }

    public PaginationDTO<TeamDTO> findAll(PageRequest pageable) {

        Page<Team> pagedTeams = this.teamRepository.findAll(pageable);
        List<TeamDTO> dtos = new ArrayList<>();
        for (Team team : pagedTeams) {
            dtos.add(new TeamDTO(team));
        }

        return new PaginationDTO<>(dtos, pagedTeams.getTotalElements());
    }

    @Transactional
    public TeamDTO newTeam(NewTeamDTO newTeam) {

        Team team = new Team();
        team.setName(newTeam.getName());
        List<Employee> members = new ArrayList<>();

        Employee lead = findEmployeeById(newTeam.getLead().getId());
        addLead(lead, team, members);
        addEmployees(newTeam, lead, team, members);

        team.setMembers(members);
        this.teamRepository.save(team);
        return new TeamDTO(team);
    }

    private void addEmployees(NewTeamDTO newTeam, Employee lead, Team team, List<Employee> members) {
        for (EmployeeDTO emp : newTeam.getMembers()) {
            if (emp.getId().equals(lead.getId())) continue;

            Employee employee = findEmployeeById(emp.getId());
            removeEmployeeFromCurrentTeam(employee);
            employee.setTeam(team);
            members.add(employee);
        }
    }

    private void addLead(Employee lead, Team team, List<Employee> members) {
        removeEmployeeFromCurrentTeam(lead);
        team.setTeamLead(lead);
        lead.setTeamLead(true);
        lead.setTeam(team);
        members.add(lead);
    }

    private void removeEmployeeFromCurrentTeam(Employee employee) {
        if (employee.getTeam() != null) {
            employee.getTeam().getMembers().remove(employee);
            if (employee.isTeamLead()) {
                employee.getTeam().setTeamLead(null);
                employee.setTeamLead(false);
            }
            teamRepository.save(employee.getTeam());
        }
    }

    private Employee findEmployeeById(Long id) {
        return this.employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee doesn't exists"));
    }
}
