package org.example.controller;

import org.example.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamview.dto.NewProjectDTO;

@RestController
@CrossOrigin
public class NewProjectController {

    private final ProjectService projectService;

    public NewProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping(
            value = "/projects",
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<?> newProject(@RequestBody NewProjectDTO newProject) {
        if (newProject.getId() == null)
            projectService.newProject(newProject);
        else
            projectService.editProject(newProject);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
