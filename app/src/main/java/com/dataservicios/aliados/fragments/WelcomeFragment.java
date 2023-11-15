package com.dataservicios.aliados.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.aliados.model.Program;
import com.dataservicios.aliados.repo.ProgramRepo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.dataservicios.aliados.R;
import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.Client;
import com.dataservicios.aliados.servicesApi.RestApiAdapter;
import com.dataservicios.aliados.servicesApi.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WelcomeFragment extends Fragment {

    private static final String LOG_TAG = WelcomeFragment.class.getSimpleName();

    private Activity activity;
    private Spinner spn_program;
    private TextView tv_user_name,tv_dex,tv_fuerza_venta,tv_llave_general,tv_foods,tv_home_care,tv_porc_gestion,tv_updated_at;
    private ImageView iv_porc_gestion;
    private Dialog dialog;
    private Client client;

    private ProgramRepo programRepo;

    public WelcomeFragment(Client client) {
        // Required empty public constructor
        this.client = client;
        this.activity = getActivity();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);

        DatabaseManager.init(getContext());

        programRepo = new ProgramRepo(activity);

        spn_program           = (Spinner) rootView.findViewById(R.id.spn_program);
        tv_user_name    = (TextView) rootView.findViewById(R.id.tv_user_name);
        tv_dex      = (TextView) rootView.findViewById(R.id.tv_dex) ;
        tv_fuerza_venta = (TextView) rootView.findViewById(R.id.tv_fuerza_venta);

        tv_llave_general = (TextView) rootView.findViewById(R.id.tv_llave_general);
        tv_foods = (TextView) rootView.findViewById(R.id.tv_foods);
        tv_home_care = (TextView) rootView.findViewById(R.id.tv_home_care);

        tv_porc_gestion = (TextView) rootView.findViewById(R.id.tv_porc_gestion);
        tv_updated_at = (TextView) rootView.findViewById(R.id.tv_updated_at);
        iv_porc_gestion = (ImageView) rootView.findViewById(R.id.iv_porc_gestion);

        tv_user_name.setText(client.getFullname());
//        tv_dex.setText(user.getDex());
//        tv_fuerza_venta.setText(user.getFfvv());




//        ArrayList<Month> months = null;
//        try {
////            months = (ArrayList<Month>) helper.getMonthDao().queryForAll();
//            months = (ArrayList<Month>) helper.getMonthDao().queryBuilder().orderBy("id",false).query();
//            showMonts(months);
//        } catch (SQLException e) {
//            Toast.makeText(getContext(), "No se encontraron datos", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }

        ArrayList<Program> programs = (ArrayList<Program>) programRepo.findAll();
        showLoadPrograms(programs);

        spn_program.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // String label = parent.getItemAtPosition(position).toString();
                int program_id = ((Program) spn_program.getSelectedItem()).getId () ;
                String label = ((Program) spn_program.getSelectedItem()).getMonth() ;
               //
                programRepo.findById(program_id);
                Toast.makeText(getContext(), label + String.valueOf(program_id) , Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return rootView;
    }

    private void showLoadPrograms(ArrayList<Program> programs) {
       // this.months      = months;
        ArrayAdapter<Program>    adapter             = new ArrayAdapter<Program>(this.getContext(),R.layout.simple_spinner_item, programs);
        spn_program.setAdapter(adapter);
    }

    private void getDataWelcome(){

        dialog  = new Dialog(getContext());

        showProgressDialog();


        Map<String, String> map = new HashMap<String, String>();
        /*map.put("id", user.getId_data());*/
//        map.put("month", month.getMonth_number());
//        map.put("year", String.valueOf(month.getYear_number()));

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Service service =  restApiAdapter.getClientService(getContext());
        Call<JsonObject> call =  service.getIndicatorsHome(map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {

                    JsonObject resultJson;
                    if(response.isSuccessful()){
                        Log.d(LOG_TAG,response.body().toString());
                        resultJson = response.body();
                        Boolean status = resultJson.get("success").getAsBoolean();
                        if(status){

                            JsonArray indicators = resultJson.getAsJsonArray("indicators");

                            for (int i = 0; i < indicators.size(); i++) {
                                JsonObject month =  indicators.get(i).getAsJsonObject();

                                String key_gen = (month.get("key_gen").isJsonNull()) ? "0" : String.valueOf(month.get("key_gen").getAsDouble());
                                String key_food = (month.get("key_food").isJsonNull()) ? "0" : String.valueOf(month.get("key_food").getAsDouble());
                                String key_home = (month.get("key_home").isJsonNull()) ? "0" : String.valueOf(month.get("key_home").getAsDouble());
                                String key_personal = (month.get("key_personal").isJsonNull()) ? "0" : String.valueOf(month.get("key_personal").getAsDouble());
                                String porc_gestion = (month.get("porc_gestion").isJsonNull()) ? "0" : String.valueOf(month.get("porc_gestion").getAsDouble());
                                String created_at = (month.get("created_at").isJsonNull()) ? "" : String.valueOf(month.get("created_at").getAsString());
                                String updated_at = (month.get("updated_at").isJsonNull()) ? "" : String.valueOf(month.get("updated_at").getAsString());

                                //String created_at_ = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date(created_at));
                                tv_llave_general.setText(key_gen + " %");
                                tv_foods.setText(key_food + " %");
                                tv_home_care.setText(key_home + " %");

                                tv_porc_gestion.setText(porc_gestion);
                                tv_updated_at.setText(updated_at);

                                float value_key_personal = Float.valueOf(porc_gestion);
                                if(value_key_personal >=0 && value_key_personal < 10)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_1xxxhdpi));
                                if(value_key_personal >=10 && value_key_personal < 20)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_2xxxhdpi));
                                if(value_key_personal >=20 && value_key_personal < 30)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_3xxxhdpi));
                                if(value_key_personal >=30 && value_key_personal < 40)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_4xxxhdpi));
                                if(value_key_personal >=40 && value_key_personal < 50)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_4xxxhdpi));
                                if(value_key_personal == 50)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_5xxxhdpi));
                                if(value_key_personal >50 && value_key_personal < 60)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_6xxxhdpi));
                                if(value_key_personal >=60 && value_key_personal < 70)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_7xxxhdpi));
                                if(value_key_personal >=70 && value_key_personal < 80)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_8xxxhdpi));
                                if(value_key_personal >=80 && value_key_personal < 90)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_9xxxhdpi));
                                if(value_key_personal >=90 && value_key_personal < 100)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_9xxxhdpi));
                                if( value_key_personal == 100)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_10xxxhdpi));

                            }


                        } else {
                            Toast.makeText(activity, "No se encontró el usuario, verifique el código", Toast.LENGTH_LONG).show();
                        }

                        dialog.dismiss();

                    }else {
                        try {
                            Log.d(LOG_TAG,"Error no se pudo obtener información: " + response.errorBody().string());
                            // processInteractor.showErrorServer("Error: intetelo nuevamente");
                            Toast.makeText(activity, "No se pudo obtener información", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Server error, no se pudo obtener información", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    Log.d(LOG_TAG, "Error SQL: " + e.getMessage());
                    Toast.makeText(getContext(), "Server error, no se pudo obtener información", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Log.d(LOG_TAG, "Error En red: " + t.getMessage());
                Log.d(LOG_TAG, t.getMessage());
                Toast.makeText(activity, "Server error, no se pudo obtener información", Toast.LENGTH_SHORT).show();

            }

        });
    }


    private void showProgressDialog(){
        int llPadding = 50;
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setBackgroundColor(getContext().getColor(R.color.red_500));

        // ll.setAlpha((float) 0.5);

        ll.setGravity(Gravity.CENTER);

        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);

        TextView tvText = new TextView(getContext());
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.WHITE);
        tvText.setTextSize(15);

        ll.addView(progressBar);
        ll.addView(tvText);
        dialog.setCancelable(false);
        dialog.addContentView(ll,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();

    }

}