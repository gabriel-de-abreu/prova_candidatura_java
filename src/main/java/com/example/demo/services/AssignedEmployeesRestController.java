/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.services;

import com.example.demo.Employee.Employee;
import com.example.demo.Employee.EmployeeRepository;
import com.example.demo.Log.Log;
import com.example.demo.Log.LogRepository;
import java.util.Date;
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
    private final LogRepository logRepository;

    public AssignedEmployeesRestController(EmployeeRepository employeeRepository, LogRepository logRepository) {
        this.employeeRepository = employeeRepository;
        this.logRepository = logRepository;
    }

    @RequestMapping(method = GET)
    public List<Employee> listAssigned() {
        logRepository.save(new Log("Listed all employees assigned to a project",
                 new Date()));
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
