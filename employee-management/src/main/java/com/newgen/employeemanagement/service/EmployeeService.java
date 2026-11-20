package com.newgen.employeemanagement.service;

import com.newgen.employeemanagement.model.Employee;

import java.util.List;

public interface EmployeeService {

    // Every employee, in id order.
    List<Employee> findAll();

    // The employee with that id, or null when there is none.
    Employee findById(Integer id);

    // Adds the employee when its id is 0 and updates the row with that id
    // otherwise. The answer is the saved employee, with its id.
    Employee save(Employee employee);

    void deleteById(Integer id);

    // Deletes every employee. Nothing in the REST API asks for it; the
    // start-up loader does, to begin from an empty table.
    void deleteAll();

}
