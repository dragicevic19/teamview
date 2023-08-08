package org.teamview.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.NewTeamDTO;
import org.teamview.dto.PaginationDTO;
import org.teamview.dto.TeamDTO;
import org.teamview.model.Employee;
import org.teamview.model.Team;
import org.teamview.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final EmployeeService employeeService;

    public TeamService(TeamRepository teamRepository, EmployeeService employeeService) {
        this.teamRepository = teamRepository;
        this.employeeService = employeeService;
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

        Employee lead = this.employeeService.findById(newTeam.getLead().getId());
        removeEmployeeFromCurrentTeam(lead);
        team.setTeamLead(lead);
        lead.setTeamLead(true);
        lead.setTeam(team);
        members.add(lead);

        for (EmployeeDTO emp : newTeam.getMembers()) {
            if (emp.getId().equals(lead.getId())) continue;

            Employee employee = this.employeeService.findById(emp.getId());
            removeEmployeeFromCurrentTeam(employee);
            employee.setTeam(team);
            members.add(employee);
        }

        team.setMembers(members);
        this.teamRepository.save(team);
        return new TeamDTO(team);
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
}
