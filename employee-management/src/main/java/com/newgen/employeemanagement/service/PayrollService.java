package com.newgen.employeemanagement.service;

import com.newgen.employeemanagement.dao.EmployeeDAO;
import com.newgen.employeemanagement.model.Payslip;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class PayrollService {


    private final EmployeeDAO employeeDAO;
    private final PayrollReport payrollReport;

    @Value("${ems.payroll.top-earners}")
    private int topEarners;

    public PayrollService(EmployeeDAO employeeDAO, PayrollReport payrollReport) {
        this.employeeDAO = employeeDAO;
        this.payrollReport = payrollReport;
    }

    @PostConstruct
    public void checkSettings() {
        if (topEarners < 0) {
            throw new IllegalStateException("ems.payroll.top-earners can't be negative: " + topEarners);
        }
    }


    public String summary() {
        PayrollReport report = new PayrollReport(employeeDAO.findAll());
        String best = report.topEarners(topEarners).stream()
                .map(payslip -> String.format(Locale.ENGLISH, "%s (%,.2f)",
                        payslip.employeeName(), payslip.monthlyPay()))
                .collect(Collectors.joining(", "));
        return String.format(Locale.ENGLISH,
                "Payroll for %d employee(s): total %,.2f a month, average %,.2f; top %d: %s",
                report.headcount(), report.totalMonthlyPay(), report.averageMonthlyPay(), topEarners,
                best.isEmpty() ? "(nobody)" : best);
    }

    public List<Payslip> getPayslips() {
        return payrollReport.getPayslips();
    }

}
