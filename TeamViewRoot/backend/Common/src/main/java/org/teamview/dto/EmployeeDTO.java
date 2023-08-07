package org.teamview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.teamview.model.Employee;
import org.teamview.model.Team;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDTO {
    private Long id;
    private String name;
    private String email;
    private ProjectDTO project;
    private TeamDTO team;
    private String position;
    private String seniority;
    private boolean isLead;

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName() + " " + employee.getLastName();
        this.email = employee.getEmail();
        if (employee.getProject() != null)
            this.project = new ProjectDTO(employee.getProject());
        if (employee.getTeam() != null)
            this.team = new TeamDTO(employee.getTeam());

        this.isLead = employee.isTeamLead();
        this.position = employee.getPosition();
        this.seniority = employee.getSeniority().name().charAt(0) + employee.getSeniority().name().substring(1).toLowerCase();
    }
}
