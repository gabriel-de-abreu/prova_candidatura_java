/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.services;

import com.example.demo.Employee.Employee;
import com.example.demo.Employee.EmployeeRepository;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 *
 * @author gabriel
 */
@RestController
@RequestMapping("/assignedEmployees")
public class AssignedEmployeesRestController {

    private final EmployeeRepository employeeRepository;

    public AssignedEmployeesRestController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @RequestMapping(method = GET)
    public List<Employee> listAssigned() {
        List<Employee> employees = employeeRepository.findAll();
        employees.removeIf(el -> {
            if (el.getProjects().isEmpty()) {
                return true;
            } else {
                return false;
            }
        });
        return employees;
    }
}
