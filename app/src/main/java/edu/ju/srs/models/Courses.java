package edu.ju.srs.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




public class Courses   implements Serializable {

    private String cname;
    private List<map> max;
    private List<map> result;

    public Courses(String cname, Map<String, String> max, Map<String, String> result) {
        this.cname = cname;
        this.max=new ArrayList<>() ;
        this.result=new ArrayList<>();
        Iterator<String> value = max.values().iterator();
        Iterator<String> keys = max.keySet().iterator();
        while (value.hasNext()){
            this.max.add(new map(keys.next(),value.next()));
        }
        Iterator<String> valu = result.values().iterator();
        Iterator<String> key = result.keySet().iterator();
        while (valu.hasNext()){
            this.result.add(new map(key.next(),valu.next()));
        }

    }

    public String getCname() {
        return cname;
    }

    public List<map> getMax() {
        return max;
    }

    public List<map> getResult() {
        return result;
    }
}

