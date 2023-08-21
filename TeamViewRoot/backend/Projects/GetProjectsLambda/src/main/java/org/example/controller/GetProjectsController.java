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
public class GetProjectsController {

    private final ProjectService projectService;

    public GetProjectsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping(
            value = "/projects",
            produces = "application/json"
    )
    public ResponseEntity<?> getAllProjects(@RequestParam Map<String, String> params) {

        List<ProjectDTO> projects = projectService.getProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);

    }
}
