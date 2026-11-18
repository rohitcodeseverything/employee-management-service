package com.newgen.employeemanagement.controller;

import com.newgen.employeemanagement.dao.EmployeeDAO;
import com.newgen.employeemanagement.model.Department;
import com.newgen.employeemanagement.model.Employee;
import com.newgen.employeemanagement.model.Payslip;
import com.newgen.employeemanagement.service.EmployeeService;
import com.newgen.employeemanagement.service.PayrollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final Comparator<Employee> byPayHighestFirst;

    public EmployeeResourceController(EmployeeDAO employeeDAO, PayrollService payrollService,
                                      Comparator<Employee> defaultOrder,
                                      Comparator<Employee> nameOrder,
                                      @Qualifier("byPayHighestFirst") Comparator<Employee> byPayHighestFirst) {
        this.employeeDAO = employeeDAO;
        this.payrollService = payrollService;
        this.defaultOrder = defaultOrder;
        this.nameOrder = nameOrder;
        this.byPayHighestFirst = byPayHighestFirst;
    }

    @GetMapping
    public ResponseEntity<String> getEmployees() {
        return
                new ResponseEntity<>(
                        names(sorted(defaultOrder)),
                        HttpStatus.OK
                );
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {

        return new ResponseEntity<>(
                employeeDAO.findAll()
                .stream()
                .sorted(byPayHighestFirst).toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Integer id) {

       var optEmployee =  employeeDAO.findById(id);

       return optEmployee
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byDepartment/{department}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable String department) {

        var dep = Department.valueOf(department);

        return  new ResponseEntity<>(
        employeeDAO.findByDepartment(dep),
                HttpStatus.OK
        );

    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
         var savedEmployee = employeeDAO.save(employee);
         return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Integer id) {
        log.info("Delete employee with id={}", id);
        employeeDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/payroll")
    public ResponseEntity<String> getPayrolls() {

        return new ResponseEntity<>(
                payrollService.summary(),
                HttpStatus.OK
        );

    }

    @GetMapping("/payslips")
    public ResponseEntity<List<Payslip>> getPayslips() {

        return new ResponseEntity<>(
                payrollService.getPayslips(),
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