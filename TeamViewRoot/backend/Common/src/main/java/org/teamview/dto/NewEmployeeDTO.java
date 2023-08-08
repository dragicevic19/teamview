package org.teamview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEmployeeDTO {
    private String name;
    private String lastName;
    private String position;
    private String seniority;
    private String address;
    private String email;
    private TeamDTO team;
}
