package org.example.controller;

import org.example.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamview.dto.ProjectDTO;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class DeleteProjectLambda {

    private final ProjectService projectService;

    public DeleteProjectLambda(ProjectService projectService) {
        this.projectService = projectService;
    }

    @DeleteMapping(
            value = "/projects",
            produces = "application/json"
    )
    public ResponseEntity<?> deleteProject(@RequestParam Map<String, String> params) {

        projectService.deleteProject(params.get("id"), params.get("teamId"));
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
