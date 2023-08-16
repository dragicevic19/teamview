package org.teamview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.teamview.model.Employee;
import org.teamview.model.Team;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamForProjectDTO {
    private Long id;
    private String name;
    private EmployeeForTeamDTO lead;
    private List<EmployeeForTeamDTO> members = new ArrayList<>();

    public TeamForProjectDTO(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        if (team.getMembers() != null) {
            for (Employee emp : team.getMembers()) {
                this.members.add(new EmployeeForTeamDTO(emp));
            }
        }
        if (team.getTeamLead() != null) {
            this.lead = new EmployeeForTeamDTO(team.getTeamLead());
        }
    }
}
