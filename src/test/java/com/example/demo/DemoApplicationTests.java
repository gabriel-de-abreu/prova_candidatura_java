package com.example.demo;

import com.example.demo.Employee.Employee;
import com.example.demo.Employee.EmployeeRepository;
import com.example.demo.project.Project;
import com.example.demo.services.AssignDTO;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Priority;
import org.aspectj.util.LangUtil;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DemoApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final String urlEmployee = "/employees";
    private final String urlProject = "/projects";
    private final String urlAssign = "/assign";
    private final String urlAssigned = "/assignedEmployees";

    private MockMvc mockMvc;

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    //Retorna todos os funcionários com projeto
    @Test
    public void A1_EmployeeGetAllAssigned() throws Exception {
        Employee pedro = new Employee("Pedro", 2000f);
        Long idPedro = Integer.toUnsignedLong(
                (int) getParser().parseMap(mockMvc.perform(post(urlEmployee).
                        content(json(pedro)).
                        contentType(contentType)).andReturn().getResponse().
                        getContentAsString()).get("id"));
        Employee fernando = new Employee("Fernando", 1500f);

        Long idFernando = Integer.toUnsignedLong(
                (int) getParser().parseMap(mockMvc.perform(post(urlEmployee).
                        content(json(fernando)).
                        contentType(contentType)).andReturn().getResponse().
                        getContentAsString()).get("id"));
        Project projA = new Project("Projeto A");
        Long idProj = Integer.toUnsignedLong(
                (int) getParser().parseMap(mockMvc.perform(post(urlProject).
                        content(json(projA)).
                        contentType(contentType)).andReturn().getResponse().
                        getContentAsString()).get("id"));
        AssignDTO dto = new AssignDTO();
        dto.setProjId(idProj);

        mockMvc.perform(post(urlAssign + "/" + idPedro).
                content(json(dto)).
                contentType(contentType));
        MvcResult queryResult = mockMvc.perform(get(urlAssigned)).andReturn();
        int total
                = getParser().parseList(queryResult.getResponse().getContentAsString()).size();
        Map row = (Map) getParser().parseList(queryResult.getResponse().getContentAsString()).get(0);
        Long idEmp = Integer.toUnsignedLong((int) row.get("id"));
        boolean result = ((Objects.equals(idEmp, idPedro)) && (total==1));
        assertTrue(result);

    }

    //Adiciona um Employee ao banco de dados
    @Test
    public void addEmployee() throws Exception {
        Employee emp = new Employee("Fulano", 1400.20f);
        mockMvc.perform(post(urlEmployee).
                content(json(emp)).
                contentType(contentType)).
                andExpect(status().isOk());
    }

    //Adiciona um projeto ao banco de dados
    @Test
    public void addProject() throws Exception {
        Project proj = new Project("Projeto1");
        mockMvc.perform(post(urlProject).
                content(json(proj)).
                contentType(contentType)).
                andExpect(status().isOk());
    }

    //Adiciona um projeto para um funcionário
    @Test
    public void assignProjectToEmp() throws Exception {
        Employee emp = new Employee("José", 3000f);
        MvcResult result = mockMvc.perform(post(urlEmployee).
                content(json(emp)).
                contentType(contentType)).andReturn();
        Long idEmp = Integer.
                toUnsignedLong((int) getParser()
                        .parseMap(result.getResponse()
                                .getContentAsString()).get("id"));
        MvcResult projectResult = mockMvc.perform(post(urlProject).
                content(json(emp)).
                contentType(contentType)).andReturn();
        Long idProject = Integer.
                toUnsignedLong((int) getParser()
                        .parseMap(projectResult.getResponse()
                                .getContentAsString()).get("id"));
        AssignDTO dto = new AssignDTO();
        dto.setProjId(idProject);
        mockMvc.perform(post(urlAssign + "/" + idEmp).
                content(json(dto)).
                contentType(contentType)).andExpect(status().isOk());

    }

    //Adiciona dois projetos para o mesmo funcionário
    @Test
    public void assign2ProjectToEmp() throws Exception {
        Project proj1 = new Project("Projeto2");
        Project proj2 = new Project("Projeto3");
        ArrayList<Project> projects = new ArrayList<>();
        projects.add(proj1);
        projects.add(proj2);
        ArrayList<Long> ids = new ArrayList<>();

        for (Project project : projects) {
            ids.add(Integer.toUnsignedLong((Integer) getParser().parseMap(
                    mockMvc.perform(post(urlProject).
                            content(json(project)).
                            contentType(contentType)).andReturn().getResponse()
                            .getContentAsString()).get("id")));
        }
        Employee emp = new Employee("Silva", 3000f);
        MvcResult result = mockMvc.perform(post(urlEmployee).
                content(json(emp)).
                contentType(contentType)).andReturn();
        int idEmp = (Integer) getParser().parseMap(result.getResponse()
                .getContentAsString()).get("id");
        AssignDTO dto = new AssignDTO();
        dto.setProjId(ids.get(0));
        mockMvc.perform(post(urlAssign + "/" + idEmp).
                content(json(dto)).
                contentType(contentType));
        dto.setProjId(ids.get(1));
        mockMvc.perform(post(urlAssign + "/" + idEmp).
                content(json(dto)).
                contentType(contentType)).andExpect(status().isOk());

    }

    //Tenta adicionar 3 projetos para o mesmo employee
    @Test
    public void assign3ProjectsToEmp() throws Exception {
        Project proj1 = new Project("Projeto4");
        Project proj2 = new Project("Projeto5");
        Project proj3 = new Project("Projeto6");
        ArrayList<Long> ids = new ArrayList<>();

        ArrayList<Project> projects = new ArrayList<>();
        projects.add(proj1);
        projects.add(proj2);
        projects.add(proj3);
        for (Project project : projects) {
            ids.add(Integer.toUnsignedLong((Integer) getParser().parseMap(
                    mockMvc.perform(post(urlProject).
                            content(json(project)).
                            contentType(contentType)).andReturn().getResponse()
                            .getContentAsString()).get("id")));
        }
        Employee emp = new Employee("Felipe", 3000f);
        MvcResult result = mockMvc.perform(post(urlEmployee).
                content(json(emp)).
                contentType(contentType)).andReturn();
        int idEmp = (Integer) getParser().parseMap(result.getResponse().getContentAsString()).get("id");
        AssignDTO dto = new AssignDTO();
        for (int i = 0; i < 2; i++) {
            dto.setProjId(ids.get(i));
            mockMvc.perform(post(urlAssign + "/" + idEmp).
                    content(json(dto)).
                    contentType(contentType));
        }
        dto.setProjId(ids.get(2));
        mockMvc.perform(post(urlAssign + "/" + idEmp).
                content(json(dto)).
                contentType(contentType)).andExpect(status().isBadRequest());

    }

    //Metodos auxiliares
    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    private JsonParser getParser() {
        JsonParser parser = (JsonParser) JsonParserFactory.getJsonParser();
        return parser;
    }

}
