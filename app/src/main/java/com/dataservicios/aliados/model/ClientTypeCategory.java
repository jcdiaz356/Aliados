package com.dataservicios.aliados.model;

import com.j256.ormlite.field.DatabaseField;

public class ClientTypeCategory {
    @DatabaseField(id = true)
    private int id ;
     @DatabaseField
    private String fullname_group ;
     @DatabaseField
    private String fullname ;
     @DatabaseField
    private String description ;
     @DatabaseField
    private String created_at ;
     @DatabaseField
    private String updated_at ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname_group() {
        return fullname_group;
    }

    public void setFullname_group(String fullname_group) {
        this.fullname_group = fullname_group;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
