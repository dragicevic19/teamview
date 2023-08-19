//package org.teamview.dto;
//
//import lombok.*;
//import org.teamview.model.Project;
//
//import java.time.LocalDate;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//public class ProjectDTO {
//    private Long id;
//    private String title;
//    private String description;
//    private TeamForProjectDTO team;
//    private String client;
//    private String status;
//    private LocalDate startDate;
//    private LocalDate endDate;
//
//    public ProjectDTO(Project project) {
//        this.id = project.getId();
//        this.title = project.getTitle();
//        this.description = project.getDescription();
//        this.client = project.getClient();
//        this.status = project.getStatus().name();
//        this.startDate = project.getStartDate();
//        this.endDate = project.getEndDate();
//
//        if (project.getTeam() != null) {
//            this.team = new TeamForProjectDTO(project.getTeam());
//        }
//    }
//}
