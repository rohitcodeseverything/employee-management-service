package com.newgen.employeemanagement.model;

/**
 * Contract for anything on the payroll that earns money.
 */
public interface Payable {

    double calculateMonthlySalary();

    default double calculateAnnualSalary() {
        return calculateMonthlySalary() * 12;
    }
}
