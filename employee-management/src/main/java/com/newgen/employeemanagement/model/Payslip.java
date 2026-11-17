package com.newgen.employeemanagement.model;

/**
 * What one employee is paid for a month, frozen at the moment it was issued.
 * <p>
 * Employee is mutable (setName, setBaseSalary, ...), so a payslip copies the
 * values out instead of pointing back at it: later edits to the employee
 * can't rewrite a payslip that already exists. As a record it's immutable by
 * construction, and equals()/hashCode()/toString() are generated from all
 * the components -- unlike Employee, where equality is just the id.
 * <p>
 * The compact constructor only rejects nulls (states that should never
 * happen). It deliberately doesn't judge the pay amount: nothing upstream
 * validates salaries yet, and a rule enforced here would surface as a crash
 * when a payslip is printed, long after the bad value was entered.
 * <p>
 * Section 22: the department is a {@link Department} now. An enum constant is
 * a single, unchangeable instance, so keeping it in a record costs nothing and
 * the payslip stays frozen. The record's own toString() prints the constant's
 * name (ENGINEERING); the printed payslip uses {@code getDisplayName()}.
 */
public record Payslip(int employeeId, String employeeName, String designation,
                      Department department, double monthlyPay) {

    public Payslip {
        if (employeeName == null || designation == null || department == null) {
            throw new IllegalArgumentException("A payslip needs a name, designation and department");
        }
    }

    public static Payslip of(Employee employee) {
        return new Payslip(employee.getId(), employee.getName(), employee.designation(),
                employee.getDepartment(), employee.calculateMonthlySalary());
    }
}
