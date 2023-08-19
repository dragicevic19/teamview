package org.teamview.dto;

import lombok.*;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamDTO {
    private String id;
    private String name;
    private EmployeeForTeamDTO lead;
    private List<EmployeeForTeamDTO> members = new ArrayList<>();
    private ProjectDTO project;

    public TeamDTO(Team team, List<User> employees, Project project) {
        this.id = team.getId();
        this.name = team.getTeamName();
        if (team.getTeamLead() != null) {
            this.lead = new EmployeeForTeamDTO(team.getTeamLead());
        }
        if (employees != null && !employees.isEmpty()) {
            for (User emp : employees) {
                this.members.add(new EmployeeForTeamDTO(emp));
            }
        }
        if (project != null)
            this.project = new ProjectDTO(project, team, employees);
    }
}
