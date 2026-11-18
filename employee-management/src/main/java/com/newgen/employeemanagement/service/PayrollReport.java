package com.newgen.employeemanagement.service;

import com.newgen.employeemanagement.model.Department;
import com.newgen.employeemanagement.model.Employee;
import com.newgen.employeemanagement.model.Payslip;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class PayrollReport {

    private static final Comparator<Payslip> HIGHEST_PAY_FIRST =
            Comparator.comparing(Payslip::monthlyPay, Comparator.reverseOrder());

    private final List<Payslip> payslips;

    public PayrollReport(Collection<? extends Employee> employees) {
       this.payslips =  employees.stream()
                .map(Payslip::of)
                .collect(readOnlyList());
    }

    private static <T> Collector<T, ?, List<T>>  readOnlyList() {
        return Collectors.collectingAndThen(
                Collectors.toList(), Collections::unmodifiableList
        );
    }


    public int headcount() {
        return payslips.size();
    }

    public double totalMonthlyPay() {
        return total(payslips);
    }

    private static double total(Collection<Payslip> payslips) {
        return payslips.stream()
                .map(Payslip::monthlyPay)
                .reduce(0.0, Double::sum);
    }

    public double averageMonthlyPay() {
        return payslips.isEmpty() ? 0 : totalMonthlyPay() / payslips.size();
    }


    public List<Payslip> topEarners(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count can't be negative: " + count);
        }
        return payslips.stream()
                .sorted(HIGHEST_PAY_FIRST)
                .limit(count)
                .collect(readOnlyList());
    }

    public Map<Department, List<Payslip>> byDepartment() {
        Map<Department, List<Payslip>> groups = payslips.stream()
                .collect(Collectors.groupingBy(Payslip::department, TreeMap::new, readOnlyList()));
        return Collections.unmodifiableMap(groups);
    }

    // What each department is paid in total. The downstream collector gathers
    // a group's payslips into a list and then collectingAndThen() turns that
    // list into its total.
    public Map<Department, Double> totalByDepartment() {
        Map<Department, Double> totals = payslips.stream()
                .collect(Collectors.groupingBy(Payslip::department, TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), PayrollReport::total)));
        return Collections.unmodifiableMap(totals);
    }

    // partitioningBy splits the payslips in two with a Predicate: the list for
    // true is everyone paid MORE than the threshold, the list for false is
    // everyone paid exactly that or less. Both keys are always there, even if
    // one list is empty.
    public Map<Boolean, List<Payslip>> splitAbove(double threshold) {
        Map<Boolean, List<Payslip>> parts = payslips.stream()
                .collect(Collectors.partitioningBy(payslip -> payslip.monthlyPay() > threshold, readOnlyList()));
        return Collections.unmodifiableMap(parts);
    }

    public List<Payslip> getPayslips() {
        return Collections.unmodifiableList(payslips);
    }

}
