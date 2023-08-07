package org.teamview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.teamview.dto.PaginationDTO;
import org.teamview.dto.ProjectDTO;
import org.teamview.model.Project;
import org.teamview.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public PaginationDTO<ProjectDTO> findAll(PageRequest pageable) {

        Page<Project> pagedProjects = this.projectRepository.findAll(pageable);
        List<ProjectDTO> dtos = new ArrayList<>();
        for (Project proj : pagedProjects) {
            dtos.add(new ProjectDTO(proj));
        }

        return new PaginationDTO<>(dtos, pagedProjects.getTotalElements());
    }
}
