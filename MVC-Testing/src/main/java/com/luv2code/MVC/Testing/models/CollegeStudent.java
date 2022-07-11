package com.luv2code.MVC.Testing.models;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="student")
public class CollegeStudent implements Student{

    @Id
   // @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @Column(name="email_address")
    private String emailAddress;





    public CollegeStudent()
    {}

    public CollegeStudent(String firstname, String lastname, String emailAddress) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }



    public void setfirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setlastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }



    @Override
    public String toString() {
        return "CollegeStudent{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", emailAddress='" + emailAddress + '\'' +

                '}';
        //", studentGrades=" + studentGrades +
    }

    @Override
    public String studentInformation() {
        return studentFullName()+" "+getEmailAddress();
    }

    @Override
    public String studentFullName() {
        return getFirstName()+" "+getLastName();
    }

    private String getfirstnameAndId()
    {
        return getFirstName()+" "+getId();
    }
}
