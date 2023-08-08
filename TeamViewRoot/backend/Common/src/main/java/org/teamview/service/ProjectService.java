package org.teamview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.teamview.dto.NewProjectDTO;
import org.teamview.dto.PaginationDTO;
import org.teamview.dto.ProjectDTO;
import org.teamview.enums.ProjectStatus;
import org.teamview.exception.NotFoundException;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.repository.ProjectRepository;
import org.teamview.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;

    public ProjectService(ProjectRepository projectRepository, TeamRepository teamRepository) {
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
    }

    public PaginationDTO<ProjectDTO> findAll(PageRequest pageable) {

        Page<Project> pagedProjects = this.projectRepository.findAll(pageable);
        List<ProjectDTO> dtos = new ArrayList<>();
        for (Project proj : pagedProjects) {
            dtos.add(new ProjectDTO(proj));
        }

        return new PaginationDTO<>(dtos, pagedProjects.getTotalElements());
    }

    public ProjectDTO newProject(NewProjectDTO newProject) {

        Project project = new Project();
        project.setTitle(newProject.getTitle());
        project.setClient(newProject.getClient());
        project.setDescription(newProject.getDescription());
        project.setStatus(ProjectStatus.ON_HOLD);
        project.setStartDate(newProject.getStartDate());
        project.setEndDate(newProject.getEndDate());

        if (newProject.getTeam() != null && newProject.getTeam().getId() != null) {
            Team team = findTeamById(newProject.getTeam().getId());
            if (team.getProject() != null) team.getProject().setTeam(null);
            project.setTeam(team);
            team.setProject(project);
        }
        return new ProjectDTO(projectRepository.save(project));
    }

    private Team findTeamById(Long id) {
        return teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team doesn't exists!"));
    }
}
