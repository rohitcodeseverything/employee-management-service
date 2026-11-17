package com.newgen.employeemanagement.model;

/**
 * Marks an employee type that can move up to the next role.
 * Interns deliberately do NOT implement this -- they must convert to a
 * Developer role first.
 */
public interface Promotable {

    String nextRole();

    default void promote() {
        System.out.println("Congratulations! You are being promoted to " + nextRole());
    }
}
