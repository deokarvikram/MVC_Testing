package com.luv2code.MVC.Testing;

import com.luv2code.MVC.Testing.models.HistoryGrade;
import com.luv2code.MVC.Testing.models.MathGrade;
import com.luv2code.MVC.Testing.models.ScienceGrade;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class MvcTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvcTestingApplication.class, args);
	}

	@Bean
	@Scope("prototype")
	@Qualifier("mathGrade")
	public MathGrade getMathGrade()
	{
		return new MathGrade();
	}

	@Bean
	@Scope("prototype")
	@Qualifier("scienceGrade")
	public ScienceGrade getScienceGrade()
	{
		return new ScienceGrade();
	}

	@Bean
	@Scope("prototype")
	@Qualifier("historyGrade")
	public HistoryGrade getHistoryGrade()
	{
		return new HistoryGrade();
	}

}
