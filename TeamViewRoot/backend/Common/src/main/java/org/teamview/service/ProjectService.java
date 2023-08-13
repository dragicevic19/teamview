package org.teamview.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.teamview.dto.NewProjectDTO;
import org.teamview.dto.PaginationDTO;
import org.teamview.dto.ProjectDTO;
import org.teamview.enums.ProjectStatus;
import org.teamview.exception.BadRequestException;
import org.teamview.exception.NotFoundException;
import org.teamview.model.Employee;
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
        project.setStatus(ProjectStatus.valueOf(newProject.getStatus()));
        project.setStartDate(newProject.getStartDate());
        project.setEndDate(newProject.getEndDate());

        addTeamToProject(newProject, project, false);
        return new ProjectDTO(projectRepository.save(project));
    }

    private Team findTeamById(Long id) {
        return teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team doesn't exists!"));
    }

    public ProjectDTO editProject(Long id, NewProjectDTO editProject) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Project doesn't exists!"));

        project.setTitle(editProject.getTitle());
        project.setClient(editProject.getClient());
        project.setDescription(editProject.getDescription());
        project.setStatus(ProjectStatus.valueOf(editProject.getStatus()));
        project.setStartDate(editProject.getStartDate());
        project.setEndDate(editProject.getEndDate());

        addTeamToProject(editProject, project, true);

        return new ProjectDTO(projectRepository.save(project));
    }

    private void addTeamToProject(NewProjectDTO newProject, Project project, boolean edit) {

        if (newProject.getTeam() == null || newProject.getTeam().getId() == null) {
            if (!edit) return; // there is no team for new project yet

            // removing team from project
            project.getTeam().setProject(null);
            project.setTeam(null);
            return;
        }

        // there is team in edited project...
        if (edit && project.getTeam() != null) {
            if (project.getTeam().getId() == newProject.getTeam().getId())
                return;  // same team as before
            else
                project.getTeam().setProject(null); // removing current team from project
        }

        Team team = findTeamById(newProject.getTeam().getId());
        if (team.getProject() != null) team.getProject().setTeam(null);
        project.setTeam(team);
        team.setProject(project);
        addProjectForEmployeesOfTeam(team, project);
    }

    private void addProjectForEmployeesOfTeam(Team team, Project project) {
        for (Employee employee : team.getMembers()) {
            employee.getPastProjects().add(project);
        }
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new BadRequestException("Project doesn't exists!"));
        project.setDeleted(true);
        project.setStatus(ProjectStatus.COMPLETED);

        if (project.getTeam() != null) {
            project.setTeam(null);
        }
        projectRepository.save(project);
    }
}
