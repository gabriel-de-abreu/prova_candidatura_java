/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.Employee;

import com.example.demo.Utils.Validate;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *
 * @author gabriel
 */
@RestController
@RequestMapping(path = "/employees", produces = APPLICATION_JSON_VALUE)
public class EmployeeRestController {

    private final EmployeeRepository employeeRepository;

    public EmployeeRestController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @RequestMapping(method = GET)
    public List<Employee> list() {
        return employeeRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = GET)
    public ResponseEntity<Employee> get(@PathVariable Long id) {
        Employee employee = this.employeeRepository.findOne(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> post(@RequestBody EmployeeDTO input) {
        if (!Validate.validateName(input.getName())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity
                .ok(
                        this.employeeRepository.save(
                                new Employee(
                                        input.getName(),
                                        input.getSalary())
                        )
                );
    }
}
