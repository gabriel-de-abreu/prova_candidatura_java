/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.services;

import com.example.demo.Employee.Employee;
import com.example.demo.Employee.EmployeeRepository;
import com.example.demo.project.Project;
import com.example.demo.project.ProjectRepository;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *
 * @author gabriel
 */
@RestController
@RequestMapping(path="/assign", produces = APPLICATION_JSON_VALUE)
public class AssignProject {
    private EmployeeRepository employeeRepository;
    private ProjectRepository projectRepository;

    public AssignProject(EmployeeRepository employeeRepository, ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }
    
    
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> assign(@RequestBody AssignDTO input){
        Employee employee= employeeRepository.findOne(input.getEmpId());
        Project project = projectRepository.findOne(input.getProjId());
        
        if(employee==null || project==null){
         return ResponseEntity.notFound().build();
        }
        if(employee.getProjects().size()<2){
            employee.addProject(project);
            return ResponseEntity.ok(this.employeeRepository.save(
        employee));
        }else{
            return ResponseEntity.badRequest().build();
        }
        
    }
}
