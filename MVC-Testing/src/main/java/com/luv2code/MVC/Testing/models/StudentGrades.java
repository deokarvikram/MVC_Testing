package com.luv2code.MVC.Testing.models;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class StudentGrades {

    List<Double> mathGradeResults;

    List<MathGrade> mathGrade;



    List<HistoryGrade> historyGrade;

    List<ScienceGrade> scienceGrade;

    public List<MathGrade> getMathGrade() {

        return mathGrade;
    }

    public void setMathGrade(List<MathGrade> mathGrade) {
        this.mathGrade = mathGrade;
    }

    public List<HistoryGrade> getHistoryGrade() {
        return historyGrade;
    }

    public void setHistoryGrade(List<HistoryGrade> historyGrade) {
        this.historyGrade = historyGrade;
    }

    public List<ScienceGrade> getScienceGrade() {
        return scienceGrade;
    }

    public void setScienceGrade(List<ScienceGrade> scienceGrade) {
        this.scienceGrade = scienceGrade;
    }

    public StudentGrades()
    {}

    public StudentGrades(List<Double> mathGradeResults)
    {
        this.mathGradeResults=mathGradeResults;
    }


    public double addGradeResultsForSingleClass(List<Double> grade)
    {
        double result=0;

        for(double i:grade)
            result+=i;

        return result;
    }

    public double findGradePointAverage(List<Double> grades)
    {
            int lenghtOfGrades = grades.size();
            double sum=addGradeResultsForSingleClass(grades);
            double result=sum/lenghtOfGrades;

        BigDecimal resultRound=BigDecimal.valueOf(result);
        resultRound=resultRound.setScale(2, RoundingMode.HALF_UP);
        return resultRound.doubleValue();
    }

    public boolean isGradeGreater(double gradeOne,double gradeTwo)
    {
        if(gradeOne>gradeTwo)
            return true;
        return false;
    }

    public Object checkNull(Object obj)
    {
        if(obj!=null)
            return obj;

        return null;
    }

    public List<Double> getMathGradeResults()
    {
        List<Double> list=new ArrayList<>();
        for(MathGrade m:mathGrade)
            list.add(m.getGrade());

        return list;
    }

    public void setMathGradeResults(List<Double> mathGradeResults)
    {


    }

    @Override
    public String toString() {
        return "StudentGrades{" +
                "mathGradeResults=" + mathGradeResults +
                "}";
    }

    public List<Double> getHistoryGradeResults() {

        List<Double> list=new ArrayList<>();
        for(HistoryGrade m:historyGrade)
            list.add(m.getGrade());

        return list;
    }

    public List<Double> getScienceGradeResults() {
        List<Double> list=new ArrayList<>();
        for(ScienceGrade m:scienceGrade)
            list.add(m.getGrade());

        return list;
    }
}
