package org.teamview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.teamview.model.Project;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectDTO {
    private Long id;
    private String title;
    private String lead;
    private String leadsMail;
    private String team;
    private String client;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.client = project.getClient();
        this.status = project.getStatus().name();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();

        if (project.getTeam() != null) {
            if (project.getTeam().getTeamLead() != null) {
                this.lead = project.getTeam().getTeamLead().getName() + " " + project.getTeam().getTeamLead().getLastName();
                this.leadsMail = project.getTeam().getTeamLead().getEmail();
            }
            this.team = project.getTeam().getName();
        }
    }
}
