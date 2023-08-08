package org.teamview.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamview.dto.*;
import org.teamview.service.EmployeeService;

@RestController
@RequestMapping(value = "/employees")
@CrossOrigin
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/{page}/{rows}")
    public ResponseEntity<?> getEmployees(@PathVariable int page, @PathVariable int rows) {

        PaginationDTO<EmployeeDTO> retVal = this.employeeService.findAll(PageRequest.of(page, rows));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> newEmployee(@RequestBody NewEmployeeDTO newEmployee) {

        EmployeeDTO retVal = this.employeeService.newEmployee(newEmployee);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }
}
