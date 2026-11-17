package com.newgen.employeemanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.util.Map;

// Spring Section 03: an entity, stored in Employee's table (see Manager).
@Entity
public class Developer extends Employee implements Promotable {

    // The bonus rate for each in-demand language, keyed by lower-case name.
    // Map.of builds an immutable map, so this table can't be changed after
    // it's built; a language that isn't listed gets the default rate.
    private static final Map<String, Double> SKILL_BONUS_RATES = Map.of("java", 0.10, "python", 0.10);
    private static final double DEFAULT_BONUS_RATE = 0.05;

    @Column(name = "primary_language")
    private String primaryLanguage;

    // For Hibernate, as in Employee.
    protected Developer() {
    }

    public Developer(String name, Department department, Double baseSalary, LocalDate hireDate,
                     String primaryLanguage) {
        super(name, department, baseSalary, hireDate);
        this.primaryLanguage = primaryLanguage;
    }


    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    @Override
    public double calculateMonthlySalary() {
        // get() answers null for a language that isn't in the table (a
        // Double can be null; the double it is unboxed to can't).
        Double listedRate = SKILL_BONUS_RATES.get(primaryLanguage.toLowerCase());
        double bonusRate = listedRate != null ? listedRate : DEFAULT_BONUS_RATE;
        double skillBonus = baseSalary * bonusRate;
        return baseSalary + skillBonus;
    }

    @Override
    public String designation() {
        return "Software Developer";
    }

    @Override
    protected String extraFields() {
        return "primaryLanguage='" + primaryLanguage + "'";
    }

    @Override
    public String nextRole() {
        return "Senior Developer";
    }
}
