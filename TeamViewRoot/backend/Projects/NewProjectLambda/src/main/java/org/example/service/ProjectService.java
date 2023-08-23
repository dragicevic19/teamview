package org.example.service;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Service;
import org.teamview.dto.NewEmployeeDTO;
import org.teamview.dto.NewProjectDTO;
import org.teamview.dto.ProjectDTO;
import org.teamview.enums.ProjectStatus;
import org.teamview.enums.SeniorityLevel;
import org.teamview.exception.BadRequestException;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.model.UserProject;
import org.teamview.repository.DynamoBuilder;

import java.util.List;

@Service
public class ProjectService {

    public void newProject(NewProjectDTO newProject) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        Project project = new Project();
        project.setId(UlidCreator.getUlid().toLowerCase());
        project.setTitle(newProject.getTitle());
        project.setDescription(newProject.getDescription());
        project.setProjectStatus(ProjectStatus.valueOf(newProject.getStatus()));
        project.setClient(newProject.getClient());
        project.setStartDate(newProject.getStartDate());
        project.setEndDate(newProject.getEndDate());

        if (newProject.getTeam().getId() != null) {
            project.setTeamId(newProject.getTeam().getId());
            addProjectToTeamMembers(project);
        }
        repo.saveProject(project);
    }

    private void addProjectToTeamMembers(Project project) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        List<User> teamMembers = repo.getMembersOfTheTeam(project.getTeamId());
        teamMembers.forEach(user -> {
            UserProject userProject = new UserProject(user.getId(), project);
            repo.saveUsersProject(userProject);
        });
    }

    public void editProject(NewProjectDTO newProject) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        Project project = repo.getProject(newProject.getId(), newProject.getPrevTeamId());
        if (project == null) throw new BadRequestException("Project doesn't exist!");

        updateTeamForProject(newProject, repo, project);

        project.setTitle(newProject.getTitle());
        project.setClient(newProject.getClient());
        project.setProjectStatus(ProjectStatus.valueOf(newProject.getStatus()));
        project.setDescription(newProject.getDescription());
        project.setEndDate(newProject.getEndDate());
        project.setStartDate(newProject.getStartDate());

        repo.saveProject(project);
    }

    private void updateTeamForProject(NewProjectDTO newProject, DynamoBuilder repo, Project project) {
        if (project.getTeamId() != null && !project.getTeamId().equals(newProject.getTeam().getId())) {
            changeTeam(newProject, repo, project);
        } else if (project.getTeamId() == null && newProject.getTeam().getId() != null) {
            changeTeam(newProject, repo, project);
        }
    }

    private void changeTeam(NewProjectDTO newProject, DynamoBuilder repo, Project project) {
        repo.deleteProject(project);
        project.setTeamId(newProject.getTeam().getId());
        addProjectToTeamMembers(project);

        Project newTeamsPreviousProject = repo.getProjectForTeam(newProject.getTeam().getId());
        if (newTeamsPreviousProject != null) {
            repo.deleteProject(newTeamsPreviousProject);
            newTeamsPreviousProject.setTeamId(null);
            newTeamsPreviousProject.setProjectStatus(ProjectStatus.ON_HOLD);
            repo.saveProject(newTeamsPreviousProject);
        }
    }
}
