package com.dataservicios.aliados.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.dataservicios.aliados.R;
import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.Concourse;
import com.dataservicios.aliados.model.Client;
import com.dataservicios.aliados.servicesApi.RestApiAdapter;
import com.dataservicios.aliados.servicesApi.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConcourseDetailsTwoFragment extends Fragment {
    private static final String LOG_TAG = ConcourseDetailsTwoFragment.class.getSimpleName();

    private TextView tv_objetivo_cobertura,tv_avance_cobertura,tv_avance_cobertura_percent,tv_objetivo_volumen,tv_avance_volumen, tv_avance_volumen_percent,tv_puntos,tv_soles,tv_updated_at;
    private TextView tv_concurso,tv_fuerza_venta,tv_tipo ;

    private Client user;

    private String concurse_id;
    private int concourse_detail_id;

    private Dialog dialog;

    private DatabaseHelper helper;

    public ConcourseDetailsTwoFragment(Client user,  String concurse_id, int concourse_detail_id) {
        // Required empty public constructor
        this.user = user;
      //  this.month = month;
        this.concurse_id = concurse_id;
        this.concourse_detail_id = concourse_detail_id;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_concourse_details_two, container, false);

        this.user = user;
    //    this.month = month;
        this.concurse_id = concurse_id;

        DatabaseManager.init(getContext());
        helper = DatabaseManager.getInstance().getHelper();

        tv_concurso = (TextView)  rootView.findViewById(R.id.tv_concurso);
        tv_fuerza_venta = (TextView)  rootView.findViewById(R.id.tv_fuerza_venta);
        tv_tipo = (TextView)  rootView.findViewById(R.id.tv_tipo);

        tv_objetivo_cobertura=(TextView) rootView.findViewById(R.id.tv_objetivo_cobertura);
        tv_avance_cobertura=(TextView) rootView.findViewById(R.id.tv_avance_cobertura);
        tv_avance_cobertura_percent=(TextView) rootView.findViewById(R.id.tv_avance_cobertura_percent);
        tv_objetivo_volumen=(TextView) rootView.findViewById(R.id.tv_objetivo_volumen);
        tv_avance_volumen=(TextView) rootView.findViewById(R.id.tv_avance_volumen);
        tv_avance_volumen_percent=(TextView) rootView.findViewById(R.id.tv_avance_volumen_percent);
        tv_puntos=(TextView) rootView.findViewById(R.id.tv_puntos);
        tv_soles=(TextView) rootView.findViewById(R.id.tv_soles);
        tv_updated_at = (TextView) rootView.findViewById(R.id.tv_updated_at);

        /*tv_fuerza_venta.setText(user.getFfvv().toString());*/

        getDataConcourse();

        return rootView;
    }

    private void getDataConcourse(){

        dialog  = new Dialog(getContext());

        showProgressDialog();

        ArrayList<Concourse> concourses  = new ArrayList<Concourse>();
        Concourse concourse = new Concourse();

        Map<String, String> map = new HashMap<String, String>();
        /*map.put("id", user.getId_data());*/
      //  map.put("month", month.getMonth_number());
//        map.put("concourse_id", concurse_id);
        map.put("concourse_detail_id", String.valueOf(concourse_detail_id) );
   //     map.put("year", String.valueOf(month.getYear_number()));

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Service service =  restApiAdapter.getClientService(getContext());
        Call<JsonObject> call =  service.getAllConcourseDetail(map);

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

                            JsonArray dataConcourseDetail = resultJson.getAsJsonArray("concourseDetail");

                            for (int i = 0; i < dataConcourseDetail.size(); i++) {
                                JsonObject dataConcourse =  dataConcourseDetail.get(i).getAsJsonObject();


                                // Cargando Datos en tabla
                                String fullname = (dataConcourse.get("fullname").isJsonNull()) ? "" : dataConcourse.get("fullname").getAsString();
                                String type_concourse = (dataConcourse.get("type_concourse").isJsonNull()) ? "" : dataConcourse.get("type_concourse").getAsString();

                                String puntos = (dataConcourse.get("puntos").isJsonNull()) ? "" : String.valueOf(dataConcourse.get("puntos").getAsFloat());
                                String soles = (dataConcourse.get("soles").isJsonNull()) ? "" : String.valueOf(dataConcourse.get("soles").getAsFloat());
                                String avance_cobertura = (dataConcourse.get("avance_cobertura").isJsonNull()) ? "" : String.valueOf(dataConcourse.get("avance_cobertura").getAsFloat());
                                String objetivo_cobertura = (dataConcourse.get("objetivo_cobertura").isJsonNull()) ? "" : String.valueOf(dataConcourse.get("objetivo_cobertura").getAsFloat());
                                String avance_cobertura_porc = (dataConcourse.get("avance_cobertura_porc").isJsonNull()) ? "" : String.valueOf(dataConcourse.get("avance_cobertura_porc").getAsFloat());
                                String objetivo_volumen = (dataConcourse.get("objetivo_volumen").isJsonNull()) ? "" : String.valueOf(dataConcourse.get("objetivo_volumen").getAsFloat());
                                String avance_volumen = (dataConcourse.get("avance_volumen").isJsonNull()) ? "" : String.valueOf(dataConcourse.get("avance_volumen").getAsFloat());
                                String avance_volumen_porc = (dataConcourse.get("avance_volumen_porc").isJsonNull()) ? "" : String.valueOf(dataConcourse.get("avance_volumen_porc").getAsFloat());

                                tv_concurso.setText(fullname);
                                tv_tipo.setText(type_concourse);


                                tv_objetivo_cobertura.setText(objetivo_cobertura);
                                tv_avance_cobertura.setText(avance_cobertura);
                                tv_avance_cobertura_percent.setText(avance_cobertura_porc);
                                tv_objetivo_volumen.setText(objetivo_volumen);
                                tv_avance_volumen.setText(avance_volumen);
                                tv_avance_volumen_percent.setText(avance_volumen_porc);
                                tv_puntos.setText(puntos);
                                tv_soles.setText(soles);

                                //  Log.d(LOG_TAG, "Creando: " + dataConcourse.get("fullname").getAsString());

                            }

                            JsonArray updatedAtArrayJson = resultJson.getAsJsonArray("updated_at");
                            for (int i = 0; i < updatedAtArrayJson.size(); i++) {
                                JsonObject dataUpdated_at =  updatedAtArrayJson.get(i).getAsJsonObject();

                                String updated_at = (dataUpdated_at.get("updated_at").isJsonNull()) ? "" : dataUpdated_at.get("updated_at").getAsString();
                                tv_updated_at.setText(updated_at);

                            }


                        } else {
                            Toast.makeText(getActivity(), "No se encontró el usuario, verifique el código", Toast.LENGTH_LONG).show();
                        }

                        dialog.dismiss();

                    }else {
                        try {
                            Log.d(LOG_TAG,"Error no se pudo obtener información: " + response.errorBody().string());
                            // processInteractor.showErrorServer("Error: intetelo nuevamente");
                            Toast.makeText(getActivity(), "No se pudo obtener información", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Server error, no se pudo obtener información", Toast.LENGTH_SHORT).show();

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