package org.teamview.dto;

import lombok.*;
import org.teamview.model.Employee;
import org.teamview.model.Project;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDTO {
    private Long id;
    private String name;
    private String email;
    private ProjectDTO project;
    private TeamDTO team;
    private String position;
    private String seniority;
    private boolean isLead;
    private String address;
    private List<ProjectDTO> allProjects;

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName() + " " + employee.getLastName();
        this.email = employee.getEmail();
        this.address = employee.getAddress();

        if (employee.getTeam() != null) {
            this.team = new TeamDTO(employee.getTeam());
            if (employee.getTeam().getProject() != null)
                this.project = new ProjectDTO(employee.getTeam().getProject());
        }

        this.isLead = employee.isTeamLead();
        this.position = employee.getPosition();
        this.seniority = employee.getSeniority().name().charAt(0) + employee.getSeniority().name().substring(1).toLowerCase();
        this.allProjects = new ArrayList<>();

        for (Project project : employee.getPastProjects()) {
            this.allProjects.add(new ProjectDTO(project));
        }
    }
}
