package org.teamview.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewTeamDTO {
    private String name;
    private List<EmployeeDTO> members;
    private EmployeeDTO lead;
}
