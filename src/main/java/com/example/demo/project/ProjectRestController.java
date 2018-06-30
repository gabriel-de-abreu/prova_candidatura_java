/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.project;

import com.example.demo.Log.Log;
import com.example.demo.Log.LogRepository;
import com.example.demo.Utils.Validate;
import java.util.Date;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 *
 * @author gabriel
 */
@RestController
@RequestMapping(path = "/projects", produces = APPLICATION_JSON_VALUE)
public class ProjectRestController {

    private final ProjectRepository projectRepository;
    private final LogRepository logRepository;

    public ProjectRestController(ProjectRepository projectRepository, LogRepository logRepository) {
        this.projectRepository = projectRepository;
        this.logRepository = logRepository;
    }

    @GetMapping()
    public List<Project> list() {
        logRepository.save(new Log("Listed all projects", new Date()));
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> get(@PathVariable Long id) {
        logRepository.save(new Log("Queried project with id: " + id, new Date()));
        Project project = projectRepository.findOne(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody Object input) {
        return null;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> post(@RequestBody ProjectDTO input) {
        logRepository.save(new Log("Created project with name: " + input.getName(),
                new Date()));
        if (!Validate.validateName(input.getName())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(projectRepository.save(
                new Project(input.getName())
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return null;
    }

}
