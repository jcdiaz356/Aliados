package com.dataservicios.aliados.model;

import com.j256.ormlite.field.DatabaseField;

public class Client {

   @DatabaseField(id = true)
   private int id;
   @DatabaseField
   private String fullname;
   @DatabaseField
   private String code;
   @DatabaseField
   private String zone;
   @DatabaseField
   private String oficina;
   @DatabaseField
   private int save_ssesion;

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

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getZone() {
      return zone;
   }

   public void setZone(String zone) {
      this.zone = zone;
   }

   public String getOficina() {
      return oficina;
   }

   public void setOficina(String oficina) {
      this.oficina = oficina;
   }

   public int getSave_ssesion() {
      return save_ssesion;
   }

   public void setSave_ssesion(int save_ssesion) {
      this.save_ssesion = save_ssesion;
   }
}
