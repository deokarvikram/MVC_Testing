package com.luv2code.MVC.Testing.repository;

import com.luv2code.MVC.Testing.models.ScienceGrade;
import com.luv2code.MVC.Testing.models.StudentGrades;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScienceGradeDao extends CrudRepository<ScienceGrade,Integer> {
    List<ScienceGrade> findGradeByStudentId(int i);

    void deleteByStudentId(int i);
}
