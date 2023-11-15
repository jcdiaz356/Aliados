package com.dataservicios.aliados.model;

import com.j256.ormlite.field.DatabaseField;

public class Program {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String fullname;


    @DatabaseField
    private String description;

    @DatabaseField
    private String date_start;


    @DatabaseField
    private String date_end;

    @DatabaseField
    private String month;

    @DatabaseField
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
