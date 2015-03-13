package com.ferrishibernatesupport.saveferris.httpsearchapi;

/**
 * Created by uiradias on 06/09/14.
 */
public class CriteriaOrder {

    public CriteriaOrder() { }

    public CriteriaOrder(String attr, String type) {
        this.attr = attr;
        this.type = type;
    }

    private String attr;

    private String type = "ASC";

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }


}
