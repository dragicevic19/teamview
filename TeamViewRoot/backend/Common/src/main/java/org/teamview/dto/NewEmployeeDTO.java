package org.teamview.dto;

import lombok.*;

@Getter
@Setter
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
