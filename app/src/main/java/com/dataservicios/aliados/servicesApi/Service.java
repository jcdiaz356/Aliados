package com.dataservicios.aliados.servicesApi;


import com.google.gson.JsonObject;
import com.dataservicios.aliados.model.Promotion;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Service {

    @POST("getClientForCodeAndPassword")
//    Call<JsonObject> getUserForCode(@Body String code);
    Call<JsonObject> getClientForCodeAndPassword(@Body Map<String, String> map);

    @POST("getUserLoginForCode")
//    Call<JsonObject> getUserForCode(@Body String code);
    Call<JsonObject> getUserLoginForCode(@Body Map<String, String> map);


    @POST("changePassword")
//    Call<JsonObject> getUserForCode(@Body String code);
    Call<JsonObject> changePassword(@Body Map<String, String> map);


    // Obteniendo un promoción
    @GET("promotions/{id}")
    Call<Promotion> getDataPromotion(@Path("id") int id);



    // Obteniendo las promociones
    @GET("promotions")
    Call<ArrayList<Promotion>> getDataPromotions();

    // Obteniendo un promoción por zonas
    @POST("promotionsForZone")
    Call<Promotion> promotionsForZone(@Body Map<String, String> map);


    // Obteniendo las promociones
    @POST("getIndicatorsHome")
    Call<JsonObject> getIndicatorsHome(@Body Map<String, String> map);


    // Obteniendo los concursos
    @POST("getConcoursesForSeller")
    Call<JsonObject> getConcoursesForSeller(@Body Map<String, String> map);

    @POST("getAllConcoursesForSeller")
    Call<JsonObject> getAllConcoursesForSeller(@Body Map<String, String> map);
    // Obteniendo las promociones

    @POST("getEstadoCuenta")
    Call<JsonObject> getEstadoCuenta(@Body Map<String, String> map);

    @POST("getConcourseDetail")
    Call<JsonObject> getConcourseDetail(@Body Map<String, String> map);
    @POST("getAllConcourseDetail")
    Call<JsonObject> getAllConcourseDetail(@Body Map<String, String> map);


    /**
     *
     * @param map
     * @return
     */
    @POST("sanctum/token")
    Call<JsonObject> getLogin(@Body Map<String, String> map);

//    @Multipart
//    @POST("process")
//    Call<JsonObject> saveProcess(@Body Map<String, String> map,@Part MultipartBody.Part image);

    @Multipart
    @POST("process")
    Call<JsonObject> saveProcess(
            @Part("turn_id")            RequestBody turn_id,
            @Part("dock_id")            RequestBody dock_id,
            @Part("user_id")            RequestBody user_id,
            @Part("supervisor")         RequestBody supervisor,
            @Part("date")               RequestBody date,
            @Part("time")               RequestBody time,
            @Part("cheker")             RequestBody cheker,
            @Part("entri")              RequestBody entri,
            @Part("date_star")          RequestBody date_star,
            @Part("date_end")           RequestBody date_end,
            @Part("observations")       RequestBody observations,
            @Part("incidencess")        RequestBody incidencess,
            @Part MultipartBody.Part    image_one,
            @Part MultipartBody.Part    image_two
    );






}
