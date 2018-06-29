/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.Employee;

/**
 *
 * @author gabriel
 */
public class EmployeeDTO {
    private String name;
    private Float salary;

    public EmployeeDTO(String name, Float salary) {
        this.name = name;
        this.salary = salary;
    }

    public EmployeeDTO() {
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }
    
    
}
