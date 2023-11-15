package com.dataservicios.aliados.model;

import com.j256.ormlite.field.DatabaseField;

public class Category {
    /*"category": {
        "id": 4,
                "fullname": "1001001 - Pastas",
                "fullname_group": "PASTAS",
                "description": "Alianza,Don Vittorio, Lavaggi y Nocilini",
                "created_at": "2023-11-12T18:42:21.000000Z",
                "updated_at": null
    }*/
    @DatabaseField(id = true)
    private int id;

    private String fullname;

    private String fullname_group;

    private String description;

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

    public String getFullname_group() {
        return fullname_group;
    }

    public void setFullname_group(String fullname_group) {
        this.fullname_group = fullname_group;
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
}
