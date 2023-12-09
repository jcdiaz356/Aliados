package com.devsystem.aliados.repo;

import android.app.Activity;
import android.util.Log;

import com.devsystem.aliados.model.Award;
import com.devsystem.aliados.model.AwardDetail;
import com.devsystem.aliados.model.Category;
import com.devsystem.aliados.model.CategoryAwardDetail;
import com.devsystem.aliados.model.Client;
import com.devsystem.aliados.model.ClientType;
import com.devsystem.aliados.model.ClientTypeCategory;
import com.devsystem.aliados.model.Program;
import com.devsystem.aliados.servicesApi.RestApiAdapter;
import com.devsystem.aliados.servicesApi.Service;
import com.devsystem.aliados.views.LoginView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepoImple implements LoginRepo {
    private static final String LOG_TAG = LoginRepoImple.class.getSimpleName();
    private LoginView                       loginView;
    private CategoryRepo                    categoryRepo;
    private ClientRepo                      clientRepo;
    private ProgramRepo                     programRepo;
    private AwardRepo                       awardRepo;
    private AwardDetailRepo                 awardDetailRepo;
    private ClientTypeRepo                  clientTypeRepo;
    private ClientTypeCategoryRepo          clientTypeCategoryRepo;
    private CategoryAwardDetailRepo         categoryAwardDetailRepo;

    public LoginRepoImple(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void signIn(String code, String password, boolean sesionSave, Activity activity) {

        loginView.showProgressBar();

        categoryRepo                = new CategoryRepo(activity);
        clientRepo                  = new ClientRepo(activity);
        programRepo                 = new ProgramRepo(activity);
        awardRepo                   = new AwardRepo(activity);
        awardDetailRepo             = new AwardDetailRepo(activity);
        clientTypeRepo              = new ClientTypeRepo(activity);
        clientTypeCategoryRepo      = new ClientTypeCategoryRepo(activity);
        categoryAwardDetailRepo     = new CategoryAwardDetailRepo(activity);

        Map<String, String> map = new HashMap<String, String>();
        map.put("code", code);
        map.put("password", password);

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Service service =  restApiAdapter.getClientService(activity);
        Call<JsonObject> call =  service.getClientForCodeAndPassword(map);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject responseJsonObject;
                    if (response.isSuccessful()) {
                        Log.d(LOG_TAG, response.body().toString());
                        responseJsonObject = response.body();
                        Boolean status = responseJsonObject.get("success").getAsBoolean();
                        if (status) {

                            clientTypeRepo.deleteAll();
                            JsonArray clientTypesJsonArray = responseJsonObject.getAsJsonArray("clientTypes");
                            ClientType[] itemsClientType = new Gson().fromJson(clientTypesJsonArray, ClientType[].class);
                            for (ClientType object : itemsClientType) {
                                clientTypeRepo.create(object);
                            }
                            ArrayList<ClientType> clientTypes = (ArrayList<ClientType>) clientTypeRepo.findAll();

                            clientTypeCategoryRepo.deleteAll();
                            JsonArray clientTypeCategoriesJsonArray = responseJsonObject.getAsJsonArray("clientTypeCategories");
                            ClientTypeCategory[] clientTypeCategories = new Gson().fromJson(clientTypeCategoriesJsonArray, ClientTypeCategory[].class);
                            for (ClientTypeCategory object : clientTypeCategories) {
                                clientTypeCategoryRepo.create(object);
                            }
                            ArrayList<ClientTypeCategory> clientTypeCategories1  = (ArrayList<ClientTypeCategory>) clientTypeCategoryRepo.findAll();


                            // **********************************
                            // Creando el objeto client
                            // *********************************
                            JsonObject clientJsonObject = responseJsonObject.getAsJsonObject("client");
                            Client client = new Gson().fromJson(clientJsonObject, Client.class);
                            if (sesionSave) client.setSave_ssesion(1);
                            else client.setSave_ssesion(0);
                           // client_id = client.getId();
                            // ********* Eliminando  los usuarios si existe *********************
                            List<Client> items = null;
                            //items = helper.getUserDao().queryForAll();
                            items = (List<Client>) clientRepo.findAll();
                            for (Client object : items) {
                                clientRepo.delete(object);
                            }
                            clientRepo.create(client);
                            //TODO: por verificar el modelo categories
                            categoryRepo.deleteAll();
                            JsonArray categoriesJson = responseJsonObject.getAsJsonArray("categories");
                            Category[] itemsategories = new Gson().fromJson(categoriesJson, Category[].class);
                            for (Category object : itemsategories) {
                                categoryRepo.create(object);
                            }
                            ArrayList<Category> categories = (ArrayList<Category>) categoryRepo.findAll();
                            // **********************************
                            // Creando el array de objeto Programs
                            // *********************************
                            programRepo.deleteAll();
                            JsonArray programs = responseJsonObject.getAsJsonArray("programs");
                            // ********* Eliminando  los programs si existe *********************
                            Program[] itemsPrograms = new Gson().fromJson(programs, Program[].class);
                            for (Program object : itemsPrograms) {
                                programRepo.create(object);
                            }
                            List<Program> ObjectPrograms = (List<Program>) programRepo.findAll();
                            // **********************************
                            // Creando el objeto Amount
                            // *********************************
                            awardRepo.deleteAll();
                            JsonObject award = responseJsonObject.getAsJsonObject("award");
                            Award newAward = new Gson().fromJson(award, Award.class);
                            awardRepo.create(newAward);

                            awardDetailRepo.deleteAll();
                            JsonArray awardDetails = responseJsonObject.getAsJsonArray("award_details");
                            AwardDetail[] newAwardDetail = new Gson().fromJson(awardDetails, AwardDetail[].class);

                            for (AwardDetail object : newAwardDetail) {
                                awardDetailRepo.create(object);
                            }
                            List<AwardDetail> ValuesAwardDetail = (List<AwardDetail>) awardDetailRepo.findAll();


                            categoryAwardDetailRepo.deleteAll();
                            JsonArray categoryAwardDetailJsonArray = responseJsonObject.getAsJsonArray("categories_award_details");
                            CategoryAwardDetail[] categoryAwardDetails = new Gson().fromJson(categoryAwardDetailJsonArray, CategoryAwardDetail[].class);
                            for (CategoryAwardDetail object : categoryAwardDetails) {
                                categoryAwardDetailRepo.create(object);
                            }
                            ArrayList<CategoryAwardDetail> categoryAwardDetails1  = (ArrayList<CategoryAwardDetail>) categoryAwardDetailRepo.findAll();



                            loginView.hideProgresbar();
                            loginView.loginSuccess(client);

                        } else {
                            loginView.loginError("El código o la contraseña es incorrecta");
                            loginView.hideProgresbar();
                        }
                    }else {
                        try {
                            Log.d(LOG_TAG,"Error credenciales: " + response.errorBody().string());
                            loginView.loginError("El código o la contraseña es incorrecta");
                            loginView.hideProgresbar();

                        } catch (IOException e) {
                            e.printStackTrace();
                            loginView.serverError(e);
                            loginView.hideProgresbar();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Error SQL: " + e.getMessage());
                    loginView.serverError(e);
                    loginView.hideProgresbar();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(LOG_TAG, "Error En red: " + t.getMessage());
                Log.d(LOG_TAG, t.getMessage());
                loginView.serverError(t);
                loginView.hideProgresbar();
            }
        });
    }
}
