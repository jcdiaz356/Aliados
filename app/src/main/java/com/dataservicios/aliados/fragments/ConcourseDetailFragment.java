package com.dataservicios.aliados.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.ornach.nobobutton.ViewButton;
import com.dataservicios.aliados.R;
import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.Concourse;
import com.dataservicios.aliados.model.Month;
import com.dataservicios.aliados.model.User;
import com.dataservicios.aliados.servicesApi.RestApiAdapter;
import com.dataservicios.aliados.servicesApi.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConcourseDetailFragment extends Fragment {
    private static final String LOG_TAG = ConcourseDetailFragment.class.getSimpleName();

    private ViewButton btn_view_more;
    private Fragment fragment;
    private TextView tv_fecha_inicio,tv_fecha_fin,tv_description,tv_sku,tv_umc ;
    private TextView tv_concurso,tv_fuerza_venta,tv_tipo ;

    private User user;
    private Month month;
    private String concurse_id;
    private int concourse_detail_id;

    private Dialog dialog;

    private DatabaseHelper helper;

    public ConcourseDetailFragment(User user, Month month, String concurse_id,int concourse_detail_id) {
        // Required empty public constructor
        this.user = user;
        this.month = month;
        this.concurse_id = concurse_id;
        this.concourse_detail_id = concourse_detail_id;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_concourse_detail, container, false);

        DatabaseManager.init(getContext());
        helper = DatabaseManager.getInstance().getHelper();

        btn_view_more = rootView.findViewById(R.id.btn_view_more);

        tv_concurso = (TextView)  rootView.findViewById(R.id.tv_concurso);
        tv_fuerza_venta = (TextView)  rootView.findViewById(R.id.tv_fuerza_venta);
        tv_tipo = (TextView)  rootView.findViewById(R.id.tv_tipo);

        tv_fecha_inicio = (TextView)  rootView.findViewById(R.id.tv_fecha_inicio);
        tv_fecha_fin = (TextView)  rootView.findViewById(R.id.tv_fecha_fin);
        tv_description = (TextView)  rootView.findViewById(R.id.tv_description);
        tv_sku = (TextView)  rootView.findViewById(R.id.tv_sku);
        tv_umc = (TextView)  rootView.findViewById(R.id.tv_umc);


        tv_fuerza_venta.setText(user.getFfvv().toString());


        getDataConcourse();

        btn_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ConcourseDetailsTwoFragment(user,month,concurse_id,concourse_detail_id);
                //getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null).commit();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void getDataConcourse(){

        dialog  = new Dialog(getContext());

        showProgressDialog();

        ArrayList<Concourse> concourses  = new ArrayList<Concourse>();
        Concourse concourse = new Concourse();

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", user.getId_data());
        map.put("month", month.getMonth_number());
//         map.put("concourse_id", concurse_id);
       map.put("concourse_detail_id", String.valueOf(concourse_detail_id));
        map.put("year", String.valueOf(month.getYear_number()));

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Service service =  restApiAdapter.getClientService(getContext());
//        Call<JsonObject> call =  service.getConcourseDetail(map);
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

                                String date_concourse_start = (dataConcourse.get("date_concourse_start").isJsonNull()) ? "" : dataConcourse.get("date_concourse_start").getAsString();
                                String date_concourse_end = (dataConcourse.get("date_concourse_end").isJsonNull()) ? "" : dataConcourse.get("date_concourse_end").getAsString();
                                String description = (dataConcourse.get("description").isJsonNull()) ? "" : dataConcourse.get("description").getAsString();
                                String sku = (dataConcourse.get("sku").isJsonNull()) ? "" : dataConcourse.get("sku").getAsString();
                                String umc = (dataConcourse.get("umc").isJsonNull()) ? "" : dataConcourse.get("umc").getAsString();

                                tv_concurso.setText(fullname);
                                tv_tipo.setText(type_concourse);

                                tv_fecha_inicio.setText(date_concourse_start);
                                tv_fecha_fin.setText(date_concourse_end);
                                tv_description.setText(description);
                                tv_sku.setText(sku);
                                tv_umc.setText(umc);

                              //  Log.d(LOG_TAG, "Creando: " + dataConcourse.get("fullname").getAsString());

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