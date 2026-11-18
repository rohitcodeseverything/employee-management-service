package com.newgen.employeemanagement.dao;

import com.newgen.employeemanagement.model.Department;
import com.newgen.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDAO extends JpaRepository<Employee, Integer> {

    List<Employee> findByDepartment(Department department);

}
