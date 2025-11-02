package com.siemens.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.siemens.dto.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

}
