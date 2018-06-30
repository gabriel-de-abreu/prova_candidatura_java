/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.Log;

import com.example.demo.Employee.Employee;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 *
 * @author gabriel
 */
@RestController
@RequestMapping("/logs")
public class LogRestController {

    private LogRepository logRepository;

    public LogRestController(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @RequestMapping(method = GET)
    public List<Log> list() {
        return logRepository.findAll();
    }
}
