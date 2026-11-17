package com.newgen.employeemanagement.model;

/**
 * The departments an employee can belong to.
 * <p>
 * Section 22: this is a fixed list, so it is an enum instead of the free-text
 * String it was before. A typo such as {@code Department.ENGINEERNG} is a
 * compile error, where "Enginering" as a String was just another department
 * that nobody noticed, and "QA" and "qa" are no longer two spellings of one.
 * <p>
 * Each constant carries the text people read ("Human Resources"), because the
 * constant's own name (HUMAN_RESOURCES) is for the code. Constants keep the
 * order they are declared in, and that order is what {@code values()}, an
 * {@code EnumSet} and a {@code TreeMap} keyed by Department all use --
 * UNASSIGNED is last on purpose.
 */
public enum Department {

    ENGINEERING("Engineering"),
    HUMAN_RESOURCES("Human Resources"),
    SALES("Sales"),
    FINANCE("Finance"),
    UNASSIGNED("Unassigned");

    private final String displayName;

    // An enum constructor is always private: the constants above are the only
    // instances there will ever be.
    Department(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
