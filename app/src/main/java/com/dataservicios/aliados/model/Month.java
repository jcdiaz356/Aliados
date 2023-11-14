package com.dataservicios.aliados.model;

import com.j256.ormlite.field.DatabaseField;

public class Month {

    @DatabaseField(id = true)
   private int id;
    @DatabaseField
   private String month_number;
    @DatabaseField
   private int year_number;
    @DatabaseField
   private String mount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonth_number() {
        return month_number;
    }

    public void setMonth_number(String month_number) {
        this.month_number = month_number;
    }

    public int getYear_number() {
        return year_number;
    }

    public void setYear_number(int year_number) {
        this.year_number = year_number;
    }

    public String getMount() {
        return mount;
    }

    public void setMount(String mount) {
        this.mount = mount;
    }

    @Override
    public String toString () {
        return mount;
    }
}
