package org.example.service;

import org.springframework.stereotype.Service;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.ProjectDTO;
import org.teamview.exception.BadRequestException;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.repository.DynamoBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    public void deleteProject(String id, String teamId) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        if (teamId.equals("null")) teamId = null;

        Project project = repo.getProject(id, teamId);
        if (project == null)
            throw new BadRequestException("Project with id: <" + id + ">, teamId: <" + teamId + "> doesn't exists!");

        repo.deleteProject(project);
    }
}
