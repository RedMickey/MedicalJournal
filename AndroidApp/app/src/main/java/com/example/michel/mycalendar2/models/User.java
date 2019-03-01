package com.example.michel.mycalendar2.models;

import java.sql.Timestamp;
import java.util.Date;

public class User {

    private Integer id;
    private String name;
    private String surname;
    private Integer genderId;
    private Integer birthdayYear;
    private String email;
    private String password;
    private Date synchronizationTime;
    private Integer roleId;
    private Integer isCurrent;

    public User(){
        id = 1;
        name = "";
        surname = "";
        genderId = 1;
        synchronizationTime = new Timestamp(new Date().getTime());
        roleId = 1;
        isCurrent = 0;
    }

    public User(Integer id, String name, String surname,
                Integer genderId, Integer birthdayYear, String email,
                String password, Date synchronizationTime){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.genderId = genderId;
        this.birthdayYear = birthdayYear;
        this.email = email;
        this.password = password;
        this.synchronizationTime = synchronizationTime;
        roleId = 1;
        this.isCurrent = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public Integer getBirthdayYear() {
        return birthdayYear;
    }

    public void setBirthdayYear(Integer birthdayYear) {
        this.birthdayYear = birthdayYear;
    }

    public Date getSynchronizationTime() {
        return synchronizationTime;
    }

    public void setSynchronizationTime(Date synchronizationTime) {
        this.synchronizationTime = synchronizationTime;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public Integer getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Integer isCurrent) {
        this.isCurrent = isCurrent;
    }
}
