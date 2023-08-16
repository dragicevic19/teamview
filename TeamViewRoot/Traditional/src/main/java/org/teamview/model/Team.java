package org.teamview.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.Set;

@SQLDelete(sql = "UPDATE team SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private List<Employee> members;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "lead_id")
    private Employee teamLead;


    @OneToOne(fetch = FetchType.EAGER, mappedBy = "team", cascade = CascadeType.PERSIST)
    private Project project;

    @Column
    private boolean deleted = false;

}
