package com.newgen.employeemanagement.controller;

import com.newgen.employeemanagement.model.Employee;
import com.newgen.employeemanagement.service.EmployeeService;
import com.newgen.employeemanagement.service.PayrollService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employee/report")
public class EmployeeReportRestController {

    private final EmployeeService employeeService;
    private final PayrollService payrollService;
    private final Comparator<Employee> defaultOrder;
    private final Comparator<Employee> nameOrder;
    private final Comparator<Employee> payOrder;


    public EmployeeReportRestController(EmployeeService employeeService, PayrollService payrollService,
                                        Comparator<Employee> defaultOrder,
                                        @Qualifier("byName") Comparator<Employee> nameOrder,
                                        @Qualifier("byPayHighestFirst") Comparator<Employee> payOrder) {
        this.employeeService = employeeService;
        this.payrollService = payrollService;
        this.defaultOrder = defaultOrder;
        this.nameOrder = nameOrder;
        this.payOrder = payOrder;
    }

    @GetMapping
    public String employees() {
        return names(sorted(defaultOrder));
    }

    @GetMapping("/by-name")
    public String employeesByName() {
        return names(sorted(nameOrder));
    }

    @GetMapping("/by-pay")
    public String employeesByPay() {
        return names(sorted(payOrder));
    }

    @GetMapping("/payroll")
    public String payroll() {
        return payrollService.summary();
    }

    // The service answers in id order and the sort orders are beans of their
    // own, so the list is sorted here. sorted() is stable: employees with equal
    // pay stay in id order.
    private List<Employee> sorted(Comparator<Employee> order) {
        return employeeService.findAll().stream()
                .sorted(order)
                .collect(Collectors.toList());
    }

    private static String names(List<Employee> employees) {
        if (employees.isEmpty()) {
            return "(none)";
        }
        return employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(", "));
    }

}
