package com.dataservicios.aliados.model;

import com.j256.ormlite.field.DatabaseField;

public class User {

   @DatabaseField(id = true)
   private int id;
   @DatabaseField
   private String name;
   @DatabaseField
   private String code;
   @DatabaseField
   private String id_data;
   @DatabaseField
   private String ffvv;
   @DatabaseField
   private String dex;
   @DatabaseField
   private String dex_id;
   @DatabaseField
   private int zone_id;
   @DatabaseField
   private int save_ssesion;



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

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }


   public String getId_data() {
      return id_data;
   }

   public void setId_data(String id_data) {
      this.id_data = id_data;
   }

   public String getFfvv() {
      return ffvv;
   }

   public void setFfvv(String ffvv) {
      this.ffvv = ffvv;
   }

   public String getDex() {
      return dex;
   }

   public void setDex(String dex) {
      this.dex = dex;
   }

   public String getDex_id() {
      return dex_id;
   }

   public void setDex_id(String dex_id) {
      this.dex_id = dex_id;
   }

   public int getSave_ssesion() {
      return save_ssesion;
   }

   public void setSave_ssesion(int save_ssesion) {
      this.save_ssesion = save_ssesion;
   }

   public int getZone_id() {
      return zone_id;
   }

   public void setZone_id(int zone_id) {
      this.zone_id = zone_id;
   }
}
