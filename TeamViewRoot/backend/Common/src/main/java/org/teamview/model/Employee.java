package org.teamview.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.teamview.enums.SeniorityLevel;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee extends User {

    @Column
    private String position;

    @Column
    @Enumerated
    private SeniorityLevel seniority;

    @Column
    private Boolean teamLead;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Project> projects;



}
