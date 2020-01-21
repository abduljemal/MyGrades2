package edu.ju.srs.models;

import java.io.Serializable;


public class map   implements Serializable {

    private String key;
    private String value;

    public map(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}