/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.Employee;

import com.example.demo.Log.Log;
import com.example.demo.Log.LogRepository;
import com.example.demo.Utils.Validate;
import java.util.Date;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
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
    private final LogRepository logRepository;

    public EmployeeRestController(EmployeeRepository employeeRepository, LogRepository logRepository) {
        this.employeeRepository = employeeRepository;
        this.logRepository = logRepository;
    }

    @RequestMapping(method = GET)
    public List<Employee> list() {
        logRepository.save(new Log("Listed all Employees", new Date()));
        return employeeRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = GET)
    public ResponseEntity<Employee> get(@PathVariable Long id) {
        logRepository.save(new Log("Queried employee with id:" + id, new Date()));
        Employee employee = this.employeeRepository.findOne(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> post(@RequestBody EmployeeDTO input) {
        logRepository.save(new Log("Signed employee with name "
                + input.getName() + " and salary: "
                + input.getSalary(),
                new Date()
        ));
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
    @RequestMapping(method=DELETE,value="/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        logRepository.save(new Log("Deleting employee with: "+id,
                new Date()));
        Employee emp = employeeRepository.findOne(id);
        if(emp==null){
            return ResponseEntity.notFound().build();
        }
        employeeRepository.delete(id);
        return ResponseEntity.ok("Removed");
    }
    
}
