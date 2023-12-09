package com.devsystem.aliados.repo;

import android.app.Activity;
import android.util.Log;

import com.devsystem.aliados.model.Award;
import com.devsystem.aliados.model.AwardDetail;
import com.devsystem.aliados.model.CategoryAwardDetail;
import com.devsystem.aliados.model.ClientType;
import com.devsystem.aliados.model.ClientTypeCategory;
import com.devsystem.aliados.servicesApi.RestApiAdapter;
import com.devsystem.aliados.servicesApi.Service;
import com.devsystem.aliados.views.ClientProgramView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientProgramRepoImpl implements ClientProgramRepo {
    private static final String LOG_TAG = ClientProgramRepoImpl.class.getSimpleName();
    private AwardRepo                       awardRepo;
    private AwardDetailRepo                 awardDetailRepo;
    private ClientTypeRepo                  clientTypeRepo;
    private ClientTypeCategoryRepo          clientTypeCategoryRepo;
    private CategoryAwardDetailRepo         categoryAwardDetailRepo;

    private ClientProgramView clientProgramView;

    public ClientProgramRepoImpl(ClientProgramView clientProgramView) {
        this.clientProgramView = clientProgramView;
    }

    @Override
    public void getClientProgram(int userId, int programId, Activity activity) {

        clientProgramView.showProgressBar();

        awardRepo                   = new AwardRepo(activity);
        awardDetailRepo             = new AwardDetailRepo(activity);
        clientTypeRepo              = new ClientTypeRepo(activity);
        clientTypeCategoryRepo      = new ClientTypeCategoryRepo(activity);
        categoryAwardDetailRepo     = new CategoryAwardDetailRepo(activity);

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(userId));
        map.put("program_id", String.valueOf(programId));

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Service service =  restApiAdapter.getClientService(activity);
        Call<JsonObject> call =  service.getClientForProgram(map);
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

                            clientProgramView.hideProgresbar();
                            clientProgramView.getDataSuccess(true);

                        } else {
                            clientProgramView.hideProgresbar();
                            clientProgramView.getDataError("No se pudo obtener los datos");
                            clientProgramView.getDataSuccess(false);
                        }
                    }else {

                        clientProgramView.hideProgresbar();
                        clientProgramView.getDataError("No se pudo obtener los datos");
                        clientProgramView.getDataSuccess(false);
//                        try {
//                            Log.d(LOG_TAG,"Error credenciales: " + response.errorBody().string());
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            clientProgramView.hideProgresbar();
//                            clientProgramView.serverError(e);
//                            clientProgramView.getDataSuccess(false);
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Error SQL: " + e.getMessage());
                    clientProgramView.hideProgresbar();
                    clientProgramView.serverError(e);
                    clientProgramView.getDataSuccess(false);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(LOG_TAG, "Error En red: " + t.getMessage());
                Log.d(LOG_TAG, t.getMessage());
                clientProgramView.hideProgresbar();
                clientProgramView.serverError(t);
                clientProgramView.getDataSuccess(false);
            }
        });

    }
}
