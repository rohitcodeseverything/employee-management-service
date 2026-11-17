package com.newgen.employeemanagement.controller;

import com.newgen.employeemanagement.dao.EmployeeDAO;
import com.newgen.employeemanagement.model.Employee;
import com.newgen.employeemanagement.service.EmployeeService;
import com.newgen.employeemanagement.service.PayrollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeResourceController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeResourceController.class);


    private final EmployeeDAO employeeDAO;
    private final PayrollService payrollService;
    private final Comparator<Employee> defaultOrder;
    private final Comparator<Employee> nameOrder;
    private final Comparator<Employee> payOrder;

    public EmployeeResourceController(EmployeeDAO employeeDAO, PayrollService payrollService, Comparator<Employee> defaultOrder, Comparator<Employee> nameOrder, Comparator<Employee> payOrder) {
        this.employeeDAO = employeeDAO;
        this.payrollService = payrollService;
        this.defaultOrder = defaultOrder;
        this.nameOrder = nameOrder;
        this.payOrder = payOrder;
    }

    @GetMapping
    public ResponseEntity<String> getEmployees() {
        return
                new ResponseEntity<>(
                        names(sorted(defaultOrder)),
                        HttpStatus.OK
                );
    }

    private static String names(List<Employee> employees) {
        if (employees.isEmpty()) {
            return "(none)";
        }
        return employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(", "));
    }

    private List<Employee> sorted(Comparator<Employee> order) {
        return employeeDAO.findAll().stream()
                .sorted(order)
                .collect(Collectors.toList());
    }



}