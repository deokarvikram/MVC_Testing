package com.luv2code.MVC.Testing.repository;

import com.luv2code.MVC.Testing.models.HistoryGrade;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryGradeDao extends CrudRepository<HistoryGrade,Integer> {
    List<HistoryGrade> findGradeByStudentId(int i);

    void deleteByStudentId(int i);
}
