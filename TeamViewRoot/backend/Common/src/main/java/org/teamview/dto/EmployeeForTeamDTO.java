package org.teamview.dto;

import lombok.*;
import org.teamview.model.Employee;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeForTeamDTO {

    private Long id;
    private String name;
    private String email;

    public EmployeeForTeamDTO(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName() + " " + employee.getLastName();
        this.email = employee.getEmail();
    }
}
