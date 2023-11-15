package com.dataservicios.aliados.model;

import com.j256.ormlite.field.DatabaseField;

public class Award {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private Double avance_total;


    @DatabaseField
    private String fecha;

    @DatabaseField
    private int plan_total;


    @DatabaseField
    private int real_total;

    @DatabaseField
    private int keyv_total;

    @DatabaseField
    private String created_at;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Client client;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Program program;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getAvance_total() {
        return avance_total;
    }

    public void setAvance_total(Double avance_total) {
        this.avance_total = avance_total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getPlan_total() {
        return plan_total;
    }

    public void setPlan_total(int plan_total) {
        this.plan_total = plan_total;
    }

    public int getReal_total() {
        return real_total;
    }

    public void setReal_total(int real_total) {
        this.real_total = real_total;
    }

    public int getKeyv_total() {
        return keyv_total;
    }

    public void setKeyv_total(int keyv_total) {
        this.keyv_total = keyv_total;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }
}
