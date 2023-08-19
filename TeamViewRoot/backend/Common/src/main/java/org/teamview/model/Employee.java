//package org.teamview.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.SQLDelete;
//import org.hibernate.annotations.Where;
//import org.teamview.enums.SeniorityLevel;
//
//import java.util.Map;
//import java.util.Set;
//
//
//
//@EqualsAndHashCode(callSuper = true)
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//public class Employee extends User {
//
//    @Column
//    private String position;
//
//    @Column
//    @Enumerated
//    private SeniorityLevel seniority;
//
//    @Column
//    private boolean teamLead;
//
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "team_id")
//    private Team team;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    private Set<Project> pastProjects;
//
//    public Employee(Map<String, String> userAttributes) {
////        this.id = userAttributes.get("sub");
//        this.name = userAttributes.get("given_name");
//        this.lastName = userAttributes.get("family_name");
//        this.email = userAttributes.get("email");
//        this.deleted = false;
////        this.tokens = 0L;
//    }
//}
