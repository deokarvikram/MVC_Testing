package com.luv2code.MVC.Testing.repository;

import com.luv2code.MVC.Testing.models.CollegeStudent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao extends CrudRepository<CollegeStudent,Integer> {

    CollegeStudent findByEmailAddress(String email);
}
