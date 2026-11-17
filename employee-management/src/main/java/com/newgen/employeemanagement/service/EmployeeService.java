package com.newgen.employeemanagement.service;

import com.newgen.employeemanagement.dao.EmployeeDAO;
import com.newgen.employeemanagement.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeDAO employeeDAO;


    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }

    public List<Employee> findAll() {
        return  employeeDAO.findAll();
    }

   public void update(Employee employee) {
        var updatedEmployee = employeeDAO.save(employee);
        log.info("Updated employee with id:{}",updatedEmployee.getId());
    }

    public void delete(Integer id) {
        employeeDAO.deleteById(id);
        log.info("Deleted employee with id:{}",id);
    }

}
