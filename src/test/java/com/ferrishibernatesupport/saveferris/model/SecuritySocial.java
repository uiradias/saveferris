package com.ferrishibernatesupport.saveferris.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import java.util.Date;

/**
 * Created by uiradias on 31/08/14.
 */
@Entity(name="SecuritySocial")
@SequenceGenerator(name = "ENTITY_SEQ", sequenceName = "SECURITYSOCIAL_SEQ")
public class SecuritySocial extends BaseModel  {

    private static final long serialVersionUID = -1222617786762385525L;

    @Column
    private String number;

    @Column
    private Date expedition;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpedition() {
        return expedition;
    }

    public void setExpedition(Date expedition) {
        this.expedition = expedition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SecuritySocial that = (SecuritySocial) o;

        if (number != null ? !number.equals(that.number) : that.number != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }
}
