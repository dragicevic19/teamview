package org.teamview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamview.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
