package com.newgen.employeemanagement.controller;

import com.newgen.employeemanagement.dao.EmployeeRepository;
import com.newgen.employeemanagement.exception.EmployeeBadRequestException;
import com.newgen.employeemanagement.exception.EmployeeNotFoundException;
import com.newgen.employeemanagement.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeResourceController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeResourceController.class);

    private final EmployeeRepository employeeService;
    private final JsonMapper jsonMapper;

    public EmployeeResourceController(EmployeeRepository employeeService, JsonMapper jsonMapper) {
        this.employeeService = employeeService;
        this.jsonMapper = jsonMapper;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees() {
        return
                new ResponseEntity<>(
                        employeeService.findAll(),
                        HttpStatus.OK
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Integer id) {

       var optEmployee =  employeeService.findById(id).orElse(null);

       if(optEmployee == null) {
           throw new EmployeeNotFoundException("Employee id not found: " + id);
       }
       return new ResponseEntity<>(optEmployee, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
         var savedEmployee = employeeService.save(employee);
         return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        var existingEmployee = employeeService.findById(employee.getId());
        if (existingEmployee == null) {
            throw new EmployeeNotFoundException("Employee id not found - " + employee.getId());
        }
        return new ResponseEntity<>(employeeService.save(employee), HttpStatus.OK);
    }

    @PatchMapping("/{employeeId}")
    public ResponseEntity<Employee> patchEmployee(@PathVariable Integer employeeId,
                                                  @RequestBody Map<String, Object> patchPayload) {

       Employee employee =  employeeService.findById(employeeId).orElse(null);
       if(employee == null) {
           throw new EmployeeNotFoundException("Employee id not found - " + employeeId);
       }

       if(patchPayload.containsKey("id")) {
            throw new EmployeeBadRequestException("id is not allowed in patch body - " + patchPayload.get("id"));
       }

       Employee patched = jsonMapper.updateValue(employee,  patchPayload);
       return new ResponseEntity<>(employeeService.save(patched), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Integer id) {
        log.info("Delete employee with id={}", id);
        employeeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }








}