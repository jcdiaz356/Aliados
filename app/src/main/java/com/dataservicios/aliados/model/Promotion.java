package com.dataservicios.aliados.model;

import java.util.ArrayList;

public class Promotion {
  private int id ;
  private String name;
  private String created_at ;
  private String updated_at ;
  private ArrayList<PromotionDetail> promotion_details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<PromotionDetail> getPromotion_details() {
        return promotion_details;
    }

    public void setPromotion_details(ArrayList<PromotionDetail> promotion_details) {
        this.promotion_details = promotion_details;
    }
}
