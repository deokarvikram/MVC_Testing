package com.luv2code.MVC.Testing;

import com.luv2code.MVC.Testing.models.CollegeStudent;
import com.luv2code.MVC.Testing.models.GradebookCollegeStudent;
import com.luv2code.MVC.Testing.models.MathGrade;
import com.luv2code.MVC.Testing.repository.MathgradeDao;
import com.luv2code.MVC.Testing.repository.StudentDao;
import com.luv2code.MVC.Testing.services.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradebookControllerTest {


    private static MockHttpServletRequest request;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    StudentAndGradeService studentAndGradeServiceMock;

    @Autowired
    StudentAndGradeService studentAndGradeService;

    @Autowired
    StudentDao studentDao;

    @Value("${sql.create.student}")
    String createStudent;

    @Value("${sql.create.math.grade}")
    String createMathGrade;

    @Value("${sql.create.history.grade}")
    String createHistoryGrade;

    @Value("${sql.create.science.grade}")
    String createScienceGrade;

    @Value("${sql.delete.student}")
    String deleteStudent;

    @Value("${sql.delete.math}")
    String deleteMath;

    @Value("${sql.delete.science}")
    String deleteScience;

    @Value("${sql.delete.history}")
    String deleteHistory;

    @BeforeEach
    public void beforeEach()
    {
        jdbc.execute(createStudent);

        jdbc.execute(createHistoryGrade);
        jdbc.execute(createMathGrade);
        jdbc.execute(createScienceGrade);
    }

    @BeforeAll
    public static void setup()
    {
        request = new MockHttpServletRequest();
        request.setParameter("firstname","chad");
        request.setParameter("lastname","Dorby");
        request.setParameter("emailAddress","chad@gmail.com");
    }


    @Test
    public void getStudentHttpRequest() throws Exception {
        CollegeStudent studentOne=new CollegeStudent("chad","roby","cr@gmail.com");
        CollegeStudent studentTwo=new CollegeStudent("vikram","deokar","vd@gmail.com");

        List<CollegeStudent> students=new ArrayList<>(Arrays.asList(studentOne,studentTwo));

        when(studentAndGradeServiceMock.getGradeBook()).thenReturn(students);

        assertIterableEquals(studentAndGradeServiceMock.getGradeBook(),students);

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"index");
    }

    @Test
    public void createStudentHttpRequest() throws Exception {

        CollegeStudent student=new CollegeStudent("vikram","Deokar","vd@gmail.com");

        List<CollegeStudent> collegeStudentList=new ArrayList<>(Arrays.asList(student));

        when(studentAndGradeServiceMock.getGradeBook()).thenReturn(collegeStudentList);

        MvcResult mvcResult=this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)                .param("firstname",request.getParameterValues("firstname"))
                .param("lastname",request.getParameterValues("lastname"))
                .param("emailAddress",request.getParameterValues("emailAddress")))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mvc=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mvc,"index");

        CollegeStudent verifyStudent=studentDao.findByEmailAddress("chad@gmail.com");

        assertNotNull(verifyStudent,"should not null");

    }

    @Test
    public void deleteStudentHttpRequest() throws Exception {
        assertTrue(studentDao.findById(1).isPresent());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}",1))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav= mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"index");
    }

    @Test
    public void deleteStudentHttpRequestErrorpage() throws Exception {
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}",0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav= mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"error");
    }

    @Test
    public void studentInformationExists() throws Exception {
        assertTrue(studentDao.findById(1).isPresent());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",1))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"studentInformation");
    }

    @Test
    public void studentInformationDoesNotExists() throws Exception {
        assertFalse(studentDao.findById(0).isPresent());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"error");
    }

    @Test
    public void createValidGradeHttpRequest() throws Exception {
        assertTrue(studentDao.findById(1).isPresent());

        GradebookCollegeStudent student=studentAndGradeService.getStudentInformation(1);

        assertEquals(1,student.getStudentGrades().getMathGradeResults().size());

        MvcResult mvcResult=mockMvc.perform( MockMvcRequestBuilders.post("/grade")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grade","80.5")
                .param("gradeType","math")
                .param("studentId","1"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"studentInformation");
        student=studentAndGradeService.getStudentInformation(1);

        assertEquals(2,student.getStudentGrades().getMathGradeResults().size());

    }

    @Test
    public void createGradeInvalidStudent() throws Exception {
        MvcResult mvcResult=mockMvc.perform( MockMvcRequestBuilders.post("/grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade","80.5")
                        .param("gradeType","math")
                        .param("studentId","0"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"error");
    }

    @Test
    public void createGradeInvalidSubject() throws Exception {
        MvcResult mvcResult=mockMvc.perform( MockMvcRequestBuilders.post("/grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade","80.5")
                        .param("gradeType","English")
                        .param("studentId","1"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"error");
    }


    @AfterEach
    public void afterEach()
    {
        jdbc.execute(deleteStudent);
        jdbc.execute(deleteHistory);
        jdbc.execute(deleteMath);
        jdbc.execute(deleteScience);
    }
}
