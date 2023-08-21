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
public class GetEmployeesController {

    private final EmployeeService employeeService;

    public GetEmployeesController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(
            value = "/employees",
            produces = "application/json"
    )
    public ResponseEntity<?> getAllEmployees(@RequestParam Map<String, String> params) {

        List<EmployeeDTO> employees = employeeService.getEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);

    }
}
