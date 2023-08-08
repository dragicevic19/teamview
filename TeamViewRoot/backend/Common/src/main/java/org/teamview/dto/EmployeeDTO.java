package org.teamview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.teamview.model.Employee;

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

        if (employee.getTeam() != null) {
            this.team = new TeamDTO(employee.getTeam());
            if (employee.getTeam().getProject() != null)
                this.project = new ProjectDTO(employee.getTeam().getProject());
        }

        this.isLead = employee.isTeamLead();
        this.position = employee.getPosition();
        this.seniority = employee.getSeniority().name().charAt(0) + employee.getSeniority().name().substring(1).toLowerCase();
    }
}
