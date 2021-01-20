package com.miniproject.collegeapp.model;

import java.util.List;

public class Data {
    private String title;
    private String toDate;
    private String date;
    private String description;
    private String id;
    private String name;
    private String Department;
    private String pdfDoc;
    private List<String> adepartment;



    public Data() {
    }

    public Data(String title, String date, String toDate, String description, String id, String name, String Department,String pdfDoc, List<String> adepartment) {
        this.title = title;
        this.date = date;
        this.toDate = toDate;
        this.description = description;
        this.id = id;
        this.name=name;
        this.Department=Department;
        this.pdfDoc=pdfDoc;
        this.adepartment=adepartment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public  String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        this.Department = department;
    }
    public String getPdfDoc() {
        return pdfDoc;
    }


    public void setPdfDoc(String pdfDoc) {
        this.pdfDoc = pdfDoc;
    }
    public List<String> getAdepartment() {
        return adepartment;
    }
    public void setAdepartment(List<String> Adepartment)
    {
        this.adepartment=adepartment;
    }

}
