package org.teamview.dto;

import lombok.*;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDTO {
    private String id;
    private String name;
    private String email;
    private ProjectDTO project;
    private TeamDTO team;
    private String position;
    private String seniority;
    private boolean isLead;
    private String address;
    private List<ProjectDTO> allProjects;

    public EmployeeDTO(User employee, Team team, Project project, List<User> members) {
        this.id = employee.getId();
        this.name = employee.getFirstName() + " " + employee.getLastName();
        this.email = employee.getEmail();
        this.address = employee.getAddress();

        if (team != null) {
            this.team = new TeamDTO(team, members, project);
            if (project != null)
                this.project = new ProjectDTO(project, team, members);
        }

        this.isLead = employee.getTeamLead();
        this.position = employee.getPosition();
        this.seniority = employee.getSeniority().name().charAt(0) + employee.getSeniority().name().substring(1).toLowerCase();
        this.allProjects = new ArrayList<>();

//        for (Project project : employee.getPastProjects()) {
//            this.allProjects.add(new ProjectDTO(project));
//        }
    }
}
