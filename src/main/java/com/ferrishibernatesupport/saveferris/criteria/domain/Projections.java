package com.ferrishibernatesupport.saveferris.criteria.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uiradias on 20/04/14.
 */
public class Projections {

    private List<String> attributes;

    public static Projections add(String... attributes){
        Projections projections = new Projections();
        for (String attribute : attributes) {
            projections.add(attribute);
        }
        return projections;
    }

    public void add(List<String> attributes){
        if(this.attributes==null){
            this.attributes = new ArrayList<String>();
        }
        attributes.addAll(attributes);
    }

    public void add(String attribute){
        if(this.attributes==null){
            this.attributes = new ArrayList<String>();
        }
        attributes.add(attribute);
    }

    public List<String> getAttributes() {
        return attributes;
    }
}
