package com.dataservicios.aliados.model;

import com.j256.ormlite.field.DatabaseField;

public class AwardDetail {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private int program_id;

    @DatabaseField
    private int client_id;

    @DatabaseField
    private int plan;

    @DatabaseField
    private int plan_total;

    @DatabaseField
    private int realv;

    @DatabaseField
    private int real_total;

    @DatabaseField
    private Double avance;

    @DatabaseField
    private Double avance_total;

    @DatabaseField
    private int points;

    @DatabaseField
    private int point_total;

    @DatabaseField
    private int keyv_total;

    @DatabaseField
    private int point_real;

    @DatabaseField
    private int point_real_total;

    @DatabaseField
    private int point_acumulated;

    @DatabaseField
    private String date_acumulated;

    @DatabaseField
    private String fecha;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Category category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgram_id() {
        return program_id;
    }

    public void setProgram_id(int program_id) {
        this.program_id = program_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getPlan_total() {
        return plan_total;
    }

    public void setPlan_total(int plan_total) {
        this.plan_total = plan_total;
    }

    public int getRealv() {
        return realv;
    }

    public void setRealv(int realv) {
        this.realv = realv;
    }

    public int getReal_total() {
        return real_total;
    }

    public void setReal_total(int real_total) {
        this.real_total = real_total;
    }

    public Double getAvance() {
        return avance;
    }

    public void setAvance(Double avance) {
        this.avance = avance;
    }

    public Double getAvance_total() {
        return avance_total;
    }

    public void setAvance_total(Double avance_total) {
        this.avance_total = avance_total;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoint_total() {
        return point_total;
    }

    public void setPoint_total(int point_total) {
        this.point_total = point_total;
    }

    public int getKeyv_total() {
        return keyv_total;
    }

    public void setKeyv_total(int keyv_total) {
        this.keyv_total = keyv_total;
    }

    public int getPoint_real() {
        return point_real;
    }

    public void setPoint_real(int point_real) {
        this.point_real = point_real;
    }

    public int getPoint_real_total() {
        return point_real_total;
    }

    public void setPoint_real_total(int point_real_total) {
        this.point_real_total = point_real_total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPoint_acumulated() {
        return point_acumulated;
    }

    public void setPoint_acumulated(int point_acumulated) {
        this.point_acumulated = point_acumulated;
    }

    public String getDate_acumulated() {
        return date_acumulated;
    }

    public void setDate_acumulated(String date_acumulated) {
        this.date_acumulated = date_acumulated;
    }
}
