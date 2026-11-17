package com.newgen.employeemanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

// Spring Section 03: an entity too. No @Table: a subclass is stored in
// Employee's table, in the columns of its own fields (here team_size), which
// stay empty on the rows of the other types.
@Entity
public class Manager extends Employee implements Promotable {

    @Column(name = "team_size")
    private Integer teamSize;

    // For Hibernate, as in Employee.
    protected Manager() {
    }

    public Manager(String name, Department department, Double baseSalary, LocalDate hireDate, Integer teamSize) {
        super(name, department, baseSalary, hireDate);
        this.teamSize = teamSize;
    }

    public int getTeamSize() {
        return teamSize;
    }

    @Override
    public double calculateMonthlySalary() {
        double teamBonus = teamSize * 50.0;
        return baseSalary + teamBonus;
    }

    @Override
    public String designation() {
        return "Manager";
    }

    @Override
    protected String extraFields() {
        return "teamSize=" + teamSize;
    }

    @Override
    public String nextRole() {
        return "Senior Manager";
    }
}
