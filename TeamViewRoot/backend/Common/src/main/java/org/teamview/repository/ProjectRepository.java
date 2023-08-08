package org.teamview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamview.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
