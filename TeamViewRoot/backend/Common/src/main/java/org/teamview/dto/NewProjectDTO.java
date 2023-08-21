package org.teamview.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewProjectDTO {
    private String title;
    private String client;
    private String description;
    private TeamDTO team;
    private Date startDate;
    private Date endDate;
    private String status;

}
