package org.teamview.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.teamview.enums.SeniorityLevel;

import java.util.Set;



@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee extends User {

    @Column
    private String position;

    @Column
    @Enumerated
    private SeniorityLevel seniority;

    @Column
    private boolean teamLead;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Project> pastProjects;

}
