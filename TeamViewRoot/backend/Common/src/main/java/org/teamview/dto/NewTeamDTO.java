package org.teamview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewTeamDTO {
    private String name;
    private List<EmployeeDTO> members;
    private EmployeeDTO lead;
}
