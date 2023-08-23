package org.example.controller;

import org.example.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamview.dto.EmployeeDTO;
import org.teamview.model.Team;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class DeleteEmployeeController {

    private final EmployeeService employeeService;

    public DeleteEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @DeleteMapping(
            value = "/employees",
            produces = "application/json"
    )
    public ResponseEntity<?> deleteEmployee(@RequestParam Map<String, String> params) {

        employeeService.deleteEmployee(params.get("id"), params.get("teamId"));
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
