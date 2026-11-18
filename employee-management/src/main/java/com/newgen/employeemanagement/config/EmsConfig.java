package com.newgen.employeemanagement.config;


import com.newgen.employeemanagement.dao.EmployeeDAO;
import com.newgen.employeemanagement.model.Employee;
import com.newgen.employeemanagement.model.Payable;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

@Configuration
public class EmsConfig {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Bean
    @Primary
    public Comparator<Employee> byId() {
        return Comparator.naturalOrder();
    }

    @Bean
    public Comparator<Employee> byName() {
        return Comparator.comparing(Employee::getName, String.CASE_INSENSITIVE_ORDER);
    }

    @Bean
    public Comparator<Employee> byPayHighestFirst() {
        return Comparator.comparing(Payable::calculateMonthlySalary,  Comparator.reverseOrder());
    }

    @Bean
    public Collection<? extends Employee> employees() {
        return employeeDAO.findAll();
    }

}
