package com.luv2code.MVC.Testing.repository;

import com.luv2code.MVC.Testing.models.Grade;
import com.luv2code.MVC.Testing.models.MathGrade;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface MathgradeDao extends CrudRepository<MathGrade, Integer> {
    List<MathGrade> findGradeByStudentId(int i);

    void deleteByStudentId(int i);
}
