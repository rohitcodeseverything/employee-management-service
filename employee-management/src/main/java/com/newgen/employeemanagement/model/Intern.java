package com.newgen.employeemanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

// Spring Section 03: an entity, stored in Employee's table (see Manager).
@Entity
public class Intern extends Employee {

    @Column(name = "mentor_name")
    private String mentorName;

    // For Hibernate, as in Employee.
    protected Intern() {
    }

    public Intern(String name, Department department, Double baseSalary, LocalDate hireDate, String mentorName) {
        super(name, department, baseSalary, hireDate);
        this.mentorName = mentorName;
    }

    public String getMentorName() {
        return mentorName;
    }

    @Override
    public String designation() {
        return "Intern";
    }

    @Override
    protected String extraFields() {
        return "mentorName='" + mentorName + "'";
    }

    // No calculateMonthlySalary() override: interns are paid a flat stipend
    // (Employee's default) until they convert to a Developer role.
}
