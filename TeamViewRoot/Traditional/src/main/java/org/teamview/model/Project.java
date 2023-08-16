package org.teamview.model;

import jakarta.persistence.*;
import lombok.*;
import org.teamview.enums.ProjectStatus;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    private String client;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

    @Column
    @Enumerated
    private ProjectStatus status;

    @Column
    private boolean deleted;

}
