package edu.ju.srs.models;

import java.io.Serializable;



public class PersonalInfo    implements Serializable {

    private String Sex;
    private String Name;
    private String Dept;
    private String IDNo;

    public PersonalInfo(String name, String dept, String IDNo,String sex) {
        this.Name = name;
        this.Dept = dept;
        this.IDNo = IDNo;
        this.Sex=sex;
    }




    public String getName() {
        return Name;
    }

    public String getDept() {
        return Dept;
    }

    public String getIDNo() {
        return IDNo;
    }

    public String getSex() {
        return Sex;
    }
}
