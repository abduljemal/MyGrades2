package edu.ju.srs.models;

import java.io.Serializable;



public class Summary   implements Serializable{

    private String semId;
    private String CGP;
    private String SGP;
    private String STAT;
    private String Sem;

    public Summary(String semId, String CGP, String SGP, String STAT, String sem) {
        this.semId = semId;
        this.CGP = CGP;
        this.SGP = SGP;
        this.STAT = STAT;
        Sem = sem;
    }





    public String getSGP() {
        return SGP;
    }

    public String getCGP() {
        return CGP;
    }

    public String getSemId() {
        return semId;
    }

    public String getSTAT() {
        return STAT;
    }

    public String getSem() {
        return Sem;
    }

    @Override
    public String toString() {
        return "";
    }
}
