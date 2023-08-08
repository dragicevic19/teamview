package org.teamview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewProjectDTO {
    private String title;
    private String client;
    private String description;
    private TeamDTO team;
    private LocalDate startDate;
    private LocalDate endDate;
}
