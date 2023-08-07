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
        this.lead = team.getTeamLead().getName() + " " + team.getTeamLead().getLastName();
        this.leadsMail = team.getTeamLead().getEmail();
        this.members = team.getMembers().size();
        this.project = new ProjectDTO(team.getProject());
    }
}
