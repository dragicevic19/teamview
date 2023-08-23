package org.example.controller;

import org.example.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamview.dto.NewEmployeeDTO;

@RestController
@CrossOrigin
public class NewEmployeeController {
    private final EmployeeService employeeService;

    public NewEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(
            value = "/employees",
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<?> createNewEmployee(@RequestBody NewEmployeeDTO newEmployee) {
        if (newEmployee.getId() == null)
            employeeService.newEmployee(newEmployee);
        else
            employeeService.editEmployee(newEmployee);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
