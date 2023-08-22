package org.example.controller;

import org.example.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.ProjectDTO;
import org.teamview.model.Team;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class GetEmployeesProjectsController {

    private final EmployeeService employeeService;

    public GetEmployeesProjectsController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(
            value = "/user-projects",
            produces = "application/json"
    )
    public ResponseEntity<?> getAllUserProjects(@RequestParam Map<String, String> allParams) {
        List<ProjectDTO> projects = employeeService.getEmployeesProject(allParams.get("id"));
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
}
