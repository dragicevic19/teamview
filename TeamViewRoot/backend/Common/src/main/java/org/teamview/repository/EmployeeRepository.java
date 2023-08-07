package org.teamview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.teamview.model.Employee;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {

//    Page<Employee> findAll(Pageable pageable);
}
