package com.newgen.employeemanagement.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

// Lets Jackson pick the concrete subclass from a "type" field in the JSON body.
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Developer.class, name = "DEVELOPER"),
        @JsonSubTypes.Type(value = Manager.class, name = "MANAGER"),
        @JsonSubTypes.Type(value = Intern.class, name = "INTERN")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public abstract class Employee implements Payable, Cloneable, Comparable<Employee>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "department")
    private Department department;
    @Column(name = "base_salary")
    public Double baseSalary;
    @Column(name = "hire_date")
    private LocalDate hireDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EmployeeStatus status;

    @Transient
    private Set<String> skills = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    public abstract String designation();

    public Employee(String name, Department department, Double baseSalary, LocalDate now) {
        this.name = name;
        this.department = department;
        this.baseSalary = baseSalary;
        this.hireDate = now;
        this.status = EmployeeStatus.ACTIVE;
    }

    public Employee(String name, Double baseSalary) {
        this(name, Department.UNASSIGNED, baseSalary, LocalDate.now());
    }



    public Period tenure(LocalDate asOf) {
        return Period.between(hireDate, asOf);
    }

    public boolean isActive() {
        return status == EmployeeStatus.ACTIVE;
    }

    public boolean addSkill(String skill) {
        String trimmed = skill.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        return skills.add(trimmed);
    }


    @Override
    public double calculateMonthlySalary() {
        return baseSalary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Ids are unique within a repository, so no two stored employees ever
    // compare as equal here. Integer.compare avoids the overflow a plain
    // "id - other.id" subtraction can hit.
    @Override
    public int compareTo(Employee other) {
        return Integer.compare(id, other.id);
    }

    @Override
    public String toString() {
        String extras = extraFields();
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department=" + department +
                ", baseSalary=" + baseSalary +
                ", hireDate=" + hireDate +
                ", status=" + status +
                ", skills=" + skills +
                (extras.isEmpty() ? "" : ", " + extras) +
                '}';
    }

    protected String extraFields() {
        return "";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Employee copy = (Employee) super.clone();
        copy.skills = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        copy.skills.addAll(skills);
        return copy;
    }
}
