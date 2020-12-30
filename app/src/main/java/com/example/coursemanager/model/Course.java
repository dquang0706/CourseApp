package com.example.coursemanager.model;

import java.io.Serializable;
import java.util.Date;

public class Course implements Serializable {
    private int courseCode;
    private String name;
    private Date startDay;
    private String tutorName;

    public Course(int courseCode, String name, Date startDay, String tutorName) {
        this.courseCode = courseCode;
        this.name = name;
        this.startDay = startDay;
        this.tutorName = tutorName;
    }

    public int getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(int courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }
}
