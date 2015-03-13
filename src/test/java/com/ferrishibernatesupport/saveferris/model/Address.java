package com.ferrishibernatesupport.saveferris.model;

import javax.persistence.*;

/**
 * Created by uiradias on 03/09/14.
 */
@Entity(name="Address")
@SequenceGenerator(name = "ENTITY_SEQ", sequenceName = "ADDRESS_SEQ")
public class Address extends BaseModel {

    private static final long serialVersionUID = 4102579827162641479L;

    public enum Type{
        STREET, AVENUE;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private Type type;

    @Column
    private String description;

    @Column(name = "address_number")
    private Integer number;

    @Column
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "personid", nullable = false)
    private Person person;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
