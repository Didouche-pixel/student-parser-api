package com.model;

import java.util.Date;

public class Student {
    private String studentNumber;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String address;
    private Integer tdGroup;
    private Integer tpGroup;
    private long id;

    // Constructeurs
    public Student() {}

    public Student(String firstName, String lastName, Date birthDate, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
    }

    // Getters et Setters
    public String getStudentNumber() {
        return studentNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getTdGroup() {
        return tdGroup;
    }

    public void setTdGroup(Integer tdGroup) {
        this.tdGroup = tdGroup;
    }

    public Integer getTpGroup() {
        return tpGroup;
    }

    public void setTpGroup(Integer tpGroup) {
        this.tpGroup = tpGroup;
    }
}