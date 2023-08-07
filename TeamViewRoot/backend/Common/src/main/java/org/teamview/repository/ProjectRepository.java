package org.teamview.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.teamview.model.Project;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Long> {
}
