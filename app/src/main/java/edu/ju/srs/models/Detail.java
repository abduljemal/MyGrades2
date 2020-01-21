package edu.ju.srs.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Detail    implements Serializable {
    private String semesterId;
    private List<Courses>  courses;

    public Detail(String semesterId, List<Courses> courses) {
        this.semesterId = semesterId;
        this.courses=new ArrayList<>();
        this.courses.addAll(courses);
    }


    public String getSemesterId() {
        return semesterId;
    }

    public List<Courses> getCourses() {
        return courses;
    }
}
