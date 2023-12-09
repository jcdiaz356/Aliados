package com.devsystem.aliados.servicesApi;

import android.content.Context;

import com.devsystem.aliados.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestApiAdapter {


    public Service getClientService(Context context){

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                       // .addHeader("Authorization", "Bearer " + getToken(context))
                        .addHeader("Accept","application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ROOT_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(Service.class);
    }


//    public String getToken(Context context){
//
//        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_file_key),Context.MODE_PRIVATE);
//        String retrivedToken  = preferences.getString(context.getString(R.string.pref_saved_token_key),null);//second parameter default value.
//
//        return   retrivedToken ;
//
//    }





}
