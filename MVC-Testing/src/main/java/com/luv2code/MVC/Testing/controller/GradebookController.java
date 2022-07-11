package com.luv2code.MVC.Testing.controller;

import com.luv2code.MVC.Testing.models.CollegeStudent;
import com.luv2code.MVC.Testing.models.GradebookCollegeStudent;
import com.luv2code.MVC.Testing.services.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

    @Autowired
    StudentAndGradeService studentAndGradeService;

    @RequestMapping(value="/",method= RequestMethod.GET)
    public String getStudent(Model m)
    {
        Iterable<CollegeStudent> students=studentAndGradeService.getGradeBook();
        m.addAttribute("students",students);
        return "index";
    }

    @PostMapping("/")
    public String createStudent(@ModelAttribute("student") CollegeStudent student,Model m)
    {
        studentAndGradeService.createStudent(student.getFirstName(),student.getLastName(),student.getEmailAddress());
        Iterable<CollegeStudent> students=studentAndGradeService.getGradeBook();
        m.addAttribute("students",students);
        return "index";
    }

    @GetMapping("/delete/student/{id}")
    public String deleteStudent(@PathVariable int id,Model m)
    {
        if(studentAndGradeService.checkifStudentIsNull(id))
            return "error";

        studentAndGradeService.deleteStudent(id);
        Iterable<CollegeStudent> students=studentAndGradeService.getGradeBook();
        m.addAttribute("students",students);
        return "index";
    }

    @GetMapping("/studentInformation/{id}")
    public String studentInformation(@PathVariable int id, Model m)
    {
            if(studentAndGradeService.checkifStudentIsNull(id))
                return "error";

        studentAndGradeService.configureStudentInformation(id,m);

        return "studentInformation";
    }

    @PostMapping("/grades")
    public String createGrade(@RequestParam("grade") double grade,
                              @RequestParam("gradeType") String gradeType,
                              @RequestParam("studentId") int id,Model m)
    {
                if(studentAndGradeService.checkifStudentIsNull(id))
                    return "error";

                if(!studentAndGradeService.createGrade(grade,id,gradeType))
                    return "error";

        studentAndGradeService.configureStudentInformation(id,m);

        return "studentInformation";
    }

    @GetMapping("/grades/{id}/{gradeType}")
    public String deleteGrade(@PathVariable int id, @PathVariable String gradeType, Model m)
    {
        int sid= studentAndGradeService.deleteGrade(id,gradeType);
        if(sid==-1)
            return "error";
        studentAndGradeService.configureStudentInformation(sid,m);

        return "studentInformation";
    }

}
