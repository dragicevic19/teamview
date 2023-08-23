package org.teamview.dto;

import lombok.*;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.model.UserProject;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectDTO {
    private String id;
    private String title;
    private String description;
    private TeamForProjectDTO team;
    private String client;
    private String status;
    private Date startDate;
    private Date endDate;

    public ProjectDTO(Project project, Team team, List<User> members) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.client = project.getClient();
        this.status = project.getProjectStatus().name();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();

        if (team != null) {
            this.team = new TeamForProjectDTO(team, members);
        }
    }
}
