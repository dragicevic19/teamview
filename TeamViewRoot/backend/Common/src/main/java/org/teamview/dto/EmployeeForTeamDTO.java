package org.teamview.dto;

import lombok.*;
import org.teamview.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeForTeamDTO {

    private String id;
    private String name;
    private String email;
    private String seniority;
    private String position;
    private boolean lead;

    public EmployeeForTeamDTO(User employee) {
        this.id = employee.getId();
        this.name = employee.getFirstName() + " " + employee.getLastName();
        this.email = employee.getEmail();
        this.seniority = employee.getSeniority().name().charAt(0) + employee.getSeniority().name().substring(1).toLowerCase();
        this.position = employee.getPosition();
        this.lead = employee.isTeamLead();
    }
}
