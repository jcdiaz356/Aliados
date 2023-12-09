package com.devsystem.aliados.servicesApi;


import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Service {

    @POST("getClientForProgram")
//    Call<JsonObject> getUserForCode(@Body String code);
    Call<JsonObject> getClientForProgram(@Body Map<String, String> map);
    @POST("getClientForCodeAndPassword")
//    Call<JsonObject> getUserForCode(@Body String code);
    Call<JsonObject> getClientForCodeAndPassword(@Body Map<String, String> map);

    @POST("getUserLoginForCode")
//    Call<JsonObject> getUserForCode(@Body String code);
    Call<JsonObject> getUserLoginForCode(@Body Map<String, String> map);

    @POST("changePassword")
//    Call<JsonObject> getUserForCode(@Body String code);
    Call<JsonObject> changePassword(@Body Map<String, String> map);

}
