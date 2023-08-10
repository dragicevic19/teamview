package org.teamview.dto;

import lombok.*;
import org.teamview.model.Employee;
import org.teamview.model.Team;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamDTO {
    private Long id;
    private String name;
    private EmployeeForTeamDTO lead;
    private List<EmployeeForTeamDTO> members = new ArrayList<>();
    private ProjectDTO project;

    public TeamDTO(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        if (team.getTeamLead() != null) {
            this.lead = new EmployeeForTeamDTO(team.getTeamLead());
        }
        if (team.getMembers() != null) {
            for (Employee emp : team.getMembers()) {
                this.members.add(new EmployeeForTeamDTO(emp));
            }
        }
        if (team.getProject() != null)
            this.project = new ProjectDTO(team.getProject());
    }
}
