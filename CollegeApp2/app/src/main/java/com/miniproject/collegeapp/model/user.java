package com.miniproject.collegeapp.model;

public class user {
    private String name;
    private String phone;
    private String department;
    private String post;
    public user()
    {

    }

    public user(String name, String phone, String department, String post) {
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}

