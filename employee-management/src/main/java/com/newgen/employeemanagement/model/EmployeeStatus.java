package com.newgen.employeemanagement.model;

/**
 * Where an employee stands with the company.
 * <p>
 * Section 22: this replaces the {@code boolean active} that Employee has had
 * since Section 4, which could only say yes or no. Every status here is still
 * on the payroll -- a status never takes anyone off a payslip or out of the
 * headcount (removing someone is {@code EmployeeDAO.delete}). What it
 * decides is eligibility: the annual stock grant in {@code Main} is for ACTIVE
 * employees only.
 */
public enum EmployeeStatus {

    ACTIVE("Active"),
    ON_LEAVE("On leave"),
    ON_NOTICE("Serving notice");

    private final String displayName;

    EmployeeStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
