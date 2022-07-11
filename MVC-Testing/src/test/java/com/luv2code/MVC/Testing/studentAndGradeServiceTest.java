package com.luv2code.MVC.Testing;

import com.luv2code.MVC.Testing.models.*;
import com.luv2code.MVC.Testing.repository.HistoryGradeDao;
import com.luv2code.MVC.Testing.repository.MathgradeDao;
import com.luv2code.MVC.Testing.repository.ScienceGradeDao;
import com.luv2code.MVC.Testing.repository.StudentDao;
import com.luv2code.MVC.Testing.services.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestPropertySource("/application.properties")
@SpringBootTest
public class studentAndGradeServiceTest {


    @Autowired
    StudentAndGradeService studentAndGradeService;

    @Autowired
    StudentDao studentDao;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    MathgradeDao mathGradeDao;

    @Autowired
    ScienceGradeDao scienceGradeDao;

    @Autowired
    HistoryGradeDao historyGradeDao;

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

    @Test
    public void createstudentAndGradeService()
    {
        studentAndGradeService.createStudent("chad","Roby","chad.roby@l2code.com");
        CollegeStudent student=  studentDao.findByEmailAddress("chad.roby@l2code.com");
        assertEquals("chad.roby@l2code.com",student.getEmailAddress(),"find by email");
    }

    @Test
    public void isStudentNullCheck()
    {
        assertTrue(studentAndGradeService.checkifStudentIsNull(40));
//        assertFalse(studentAndGradeService.checkifStudentIsNull(0));
    }

    @Test
    public void deleteStudent()
    {
        Optional<CollegeStudent> student=studentDao.findById(1);
        Optional<MathGrade> mathGrade=mathGradeDao.findById(1);
        Optional<ScienceGrade> scienceGrade=scienceGradeDao.findById(1);
        Optional<HistoryGrade> historyGrade=historyGradeDao.findById(1);

        assertTrue(student.isPresent());
        assertTrue(mathGrade.isPresent());
        assertTrue(historyGrade.isPresent());
        assertTrue(scienceGrade.isPresent());

        studentAndGradeService.deleteStudent(1);
        student=studentDao.findById(1);
        mathGrade=mathGradeDao.findById(1);
        historyGrade=historyGradeDao.findById(1);
        scienceGrade=scienceGradeDao.findById(1);

        assertFalse(student.isPresent());
        assertFalse(mathGrade.isPresent());
        assertFalse(historyGrade.isPresent());
        assertFalse(scienceGrade.isPresent());

    }

    @Sql("/insertdata.sql")
    @Test
    public void getGradeBookService()
    {
        Iterable<CollegeStudent> collegeStudents=studentAndGradeService.getGradeBook();

        List<CollegeStudent> collegeStudentList=new ArrayList<>();

        for(CollegeStudent clg:collegeStudents)
            collegeStudentList.add(clg);

        assertEquals(5,collegeStudentList.size());


    }

    @Test
    public void createGrade()
    {

        assertTrue(studentAndGradeService.createGrade(80.5,1,"math"));
        Iterable<MathGrade> mathGrades=mathGradeDao.findGradeByStudentId(1);
        //assertTrue(mathGrades.iterator().hasNext(),"student has math grade");

        assertTrue(((Collection<MathGrade>)mathGrades).size()==2,"student has math grade");


        assertTrue(studentAndGradeService.createGrade(80.5,1,"science"));
        Iterable<ScienceGrade> scienceGrades=scienceGradeDao.findGradeByStudentId(1);
        //assertTrue(scienceGrades.iterator().hasNext(),"student has science grade");
        assertTrue(((Collection<ScienceGrade>)scienceGrades).size()==2,"student has science grade");

        assertTrue(studentAndGradeService.createGrade(80.5,1,"history"));
        Iterable<HistoryGrade> historyGrades=historyGradeDao.findGradeByStudentId(1);
       // assertTrue(historyGrades.iterator().hasNext(),"student has history grade");
        assertTrue(((Collection<HistoryGrade>)historyGrades).size()==2,"student has history grade");
    }

    @Test
    public void deleteGrade()
    {
        assertEquals(1,studentAndGradeService.deleteGrade(1,"math"),"Return student id after delete");
        assertEquals(1,studentAndGradeService.deleteGrade(1,"science"),"Return student id after delete");
        assertEquals(1,studentAndGradeService.deleteGrade(1,"history"),"Return student id after delete");

    }

    @Test
    public void deleteGradeInvalid()
    {
        assertEquals(0,studentAndGradeService.deleteGrade(0,"math"));
        assertEquals(0,studentAndGradeService.deleteGrade(1,"Literature"));
    }

    @Test
    public void invalidTests()
    {
        assertFalse(studentAndGradeService.createGrade(180,1,"math"));
        assertFalse(studentAndGradeService.createGrade(-80,1,"math"));
        assertFalse(studentAndGradeService.createGrade(80.5,2,"math"));
        assertFalse(studentAndGradeService.createGrade(80.5,1,"english"));

    }

    @Test
    public void getStudentInformation()
    {
       GradebookCollegeStudent student=studentAndGradeService.getStudentInformation(1);

        assertNotNull(student);

        assertEquals("chad",student.getFirstName());
        assertEquals("roby",student.getLastName());
        assertEquals("chad@l2code.com",student.getEmailAddress());
        assertTrue(student.getStudentGrades().getHistoryGrade().size()==1);
        assertTrue(student.getStudentGrades().getMathGrade().size()==1);
        assertTrue(student.getStudentGrades().getScienceGrade().size()==1);

    }

    @Test
    public void studentInformationServiceNull()
    {
        GradebookCollegeStudent student=studentAndGradeService.getStudentInformation(0);

        assertNull(student);
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
