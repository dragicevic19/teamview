package org.teamview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.teamview.model.Team;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamDTO {
    private Long id;
    private String name;
    private String lead;
    private String leadsMail;
    private int members;
    private ProjectDTO project;

    public TeamDTO(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        if (team.getTeamLead() != null) {
            this.lead = team.getTeamLead().getName() + " " + team.getTeamLead().getLastName();
            this.leadsMail = team.getTeamLead().getEmail();
        }
        if (team.getMembers() != null) {
            this.members = team.getMembers().size();
        }
        if (team.getProject() != null)
            this.project = new ProjectDTO(team.getProject());
    }
}
