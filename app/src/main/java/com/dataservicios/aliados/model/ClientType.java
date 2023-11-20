package com.dataservicios.aliados.model;

import com.j256.ormlite.field.DatabaseField;

public class ClientType {
   @DatabaseField(id = true)
   private int  id ;
   @DatabaseField
   private String  fullname ;
   @DatabaseField
   private String  award ;
   @DatabaseField
   private String  created_at ;
   @DatabaseField
   private String  updated_at ;

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

   public String getAward() {
      return award;
   }

   public void setAward(String award) {
      this.award = award;
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
