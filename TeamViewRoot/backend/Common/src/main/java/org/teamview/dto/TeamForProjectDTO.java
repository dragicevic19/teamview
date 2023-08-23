package org.teamview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.teamview.model.Team;
import org.teamview.model.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamForProjectDTO {
    private String id;
    private String name;
    private EmployeeForTeamDTO lead;
    private List<EmployeeForTeamDTO> members = new ArrayList<>();

    public TeamForProjectDTO(Team team, List<User> members) {
        this.id = team.getId();
        this.name = team.getTeamName();
        if (members != null && !members.isEmpty()) {
            for (User emp : members) {
                this.members.add(new EmployeeForTeamDTO(emp));
            }
        }
        if (team.getTeamLead() != null) {
            this.lead = new EmployeeForTeamDTO(team.getTeamLead());
        }
    }
}
