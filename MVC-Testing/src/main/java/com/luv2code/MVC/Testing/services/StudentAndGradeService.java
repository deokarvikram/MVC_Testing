package com.luv2code.MVC.Testing.services;

import com.luv2code.MVC.Testing.models.*;
import com.luv2code.MVC.Testing.repository.HistoryGradeDao;
import com.luv2code.MVC.Testing.repository.MathgradeDao;
import com.luv2code.MVC.Testing.repository.ScienceGradeDao;
import com.luv2code.MVC.Testing.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class StudentAndGradeService {

    @Autowired
    StudentDao studentDao;

    @Autowired
    @Qualifier("mathGrade")
    MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrade")
    ScienceGrade scienceGrade;

    @Autowired
    MathgradeDao mathgradeDao;

    @Autowired
    ScienceGradeDao scienceGradeDao;

    @Autowired
    HistoryGradeDao historyGradeDao;

    @Autowired
    @Qualifier("historyGrade")
    HistoryGrade historyGrade;



    public void createStudent(String firstname,String lastname, String emaild)
    {
        CollegeStudent student=new CollegeStudent(firstname,lastname,emaild);

        studentDao.save(student);


    }

    public boolean checkifStudentIsNull(int i) {



        if(!studentDao.findById(i).isPresent())
            return true;
        return false;

    }

    public void deleteStudent(int i) {

        studentDao.deleteById(i);
        historyGradeDao.deleteByStudentId(i);
        mathgradeDao.deleteByStudentId(i);
        scienceGradeDao.deleteByStudentId(i);
    }

    public Iterable<CollegeStudent> getGradeBook() {

        return studentDao.findAll();
    }

    public boolean createGrade(double grade, int id, String gradeType) {

        if(checkifStudentIsNull(id))
            return false;
        if(grade>=0 && grade<=100) {
            if (gradeType.equals("math")) {

               // mathGrade.setId(id);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(id);
                mathgradeDao.save(mathGrade);
                return true;
            }

            if (gradeType.equals("science")) {

              //  scienceGrade.setId(id);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(id);
                scienceGradeDao.save(scienceGrade);
                return true;
            }

            if (gradeType.equals("history")) {

               // historyGrade.setId(id);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(id);
                historyGradeDao.save(historyGrade);
                return true;
            }
        }
        return false;
    }

    public int deleteGrade(int id,String gradeType) {

                int studentId=-1;
                if(gradeType.equals("math"))
                { Optional<MathGrade> mathGrade=mathgradeDao.findById(id);
                if(!mathGrade.isPresent())
                    return  studentId;

                MathGrade mgrade=mathGrade.get();
                studentId=mgrade.getStudentId();
                mathgradeDao.deleteById(id);
                }
        if(gradeType.equals("science"))
        {
            Optional<ScienceGrade> scienceGrade=scienceGradeDao.findById(id);
            if(!scienceGrade.isPresent())
                return  studentId;

            ScienceGrade sgrade=scienceGrade.get();
            studentId=sgrade.getStudentId();
            scienceGradeDao.deleteById(id);
        }

        if(gradeType.equals("history"))
        { Optional<HistoryGrade> historyGrade=historyGradeDao.findById(id);
            if(!historyGrade.isPresent())
                return  studentId;

            HistoryGrade mgrade=historyGrade.get();
            studentId=mgrade.getStudentId();
            historyGradeDao.deleteById(id);
        }

return studentId;
    }

    public GradebookCollegeStudent getStudentInformation(int i) {

        if(checkifStudentIsNull(i))
            return null;

        CollegeStudent student= studentDao.findById(i).get();
        List<MathGrade> mathGradeList=mathgradeDao.findGradeByStudentId(i);
        List<HistoryGrade> historyGradeList=historyGradeDao.findGradeByStudentId(i);
        List<ScienceGrade> scienceGradeList=scienceGradeDao.findGradeByStudentId(i);

        StudentGrades studentGrades=new StudentGrades();
        studentGrades.setHistoryGrade(historyGradeList);
        studentGrades.setMathGrade(mathGradeList);
        studentGrades.setScienceGrade(scienceGradeList);

        GradebookCollegeStudent collegeStudent=new GradebookCollegeStudent(student.getId(),student.getFirstName(),student.getLastName(),student.getEmailAddress(),studentGrades);

        return collegeStudent;

    }

    public void configureStudentInformation(int id, Model m)
    {
        GradebookCollegeStudent studentEntity=getStudentInformation(id);

        m.addAttribute("student",studentEntity);
        if(studentEntity.getStudentGrades().getMathGrade().size()>0)
        {
            m.addAttribute("mathAverage",studentEntity.getStudentGrades().findGradePointAverage(studentEntity.getStudentGrades().getMathGradeResults()));
        }
        else
            m.addAttribute("mathAverage","NA");

        if(studentEntity.getStudentGrades().getHistoryGrade().size()>0)
        {
            m.addAttribute("historyAverage",studentEntity.getStudentGrades().findGradePointAverage(studentEntity.getStudentGrades().getHistoryGradeResults()));
        }
        else
            m.addAttribute("historyAverage","NA");
        if(studentEntity.getStudentGrades().getScienceGrade().size()>0)
        {
            m.addAttribute("scienceAverage",studentEntity.getStudentGrades().findGradePointAverage(studentEntity.getStudentGrades().getScienceGradeResults()));
        }
        else
            m.addAttribute("scienceAverage","NA");

    }
}
