package com.tellmedoctor.tmdfacebook.model;

import java.io.Serializable;

/**
 * Created by kmoore on 5/2/15.
 */
public class profile implements Serializable {

    private int id;
    private String Email;
    private String Firstname;
    private String Lastname;
    private String Gender;
    private String DOB;
    private String Phonenumber;
    private String Lang;
    private String Role;

    public profile() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public String getLang() {
        return Lang;
    }

    public void setLang(String lang) {
        Lang = lang;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
