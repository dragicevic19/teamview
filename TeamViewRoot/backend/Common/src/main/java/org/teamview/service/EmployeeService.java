package org.teamview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.PaginationDTO;
import org.teamview.model.Employee;
import org.teamview.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public PaginationDTO<EmployeeDTO> findAll(Pageable pageable) {

        Page<Employee> pagedEmps = this.employeeRepository.findAll(pageable);
        List<EmployeeDTO> dtos = new ArrayList<>();
        for (Employee emp : pagedEmps) {
            dtos.add(new EmployeeDTO(emp));
        }

        return new PaginationDTO<>(dtos, pagedEmps.getTotalElements());
    }
}
