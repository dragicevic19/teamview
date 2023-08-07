package org.teamview.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamview.dto.PaginationDTO;
import org.teamview.dto.ProjectDTO;
import org.teamview.service.ProjectService;

@RestController
@RequestMapping(value = "/projects")
@CrossOrigin
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @GetMapping(value = "/{page}/{rows}")
    public ResponseEntity<?> getProjects(@PathVariable int page, @PathVariable int rows) {
        PaginationDTO<ProjectDTO> retVal = this.projectService.findAll(PageRequest.of(page, rows));

        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }
}
