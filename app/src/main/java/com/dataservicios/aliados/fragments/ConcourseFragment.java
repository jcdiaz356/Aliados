package com.dataservicios.aliados.fragments;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ornach.nobobutton.ViewButton;
import com.dataservicios.aliados.R;
import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.Concourse;
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


public class ConcourseFragment extends Fragment {
    private static final String LOG_TAG = ConcourseFragment.class.getSimpleName();

   // private ViewButton btn_0;
   private Activity activity;
    private Spinner spn_month;
    private Fragment fragment;
    private LinearLayout ly_container;
    private Client user;
    private Dialog dialog;

    private DatabaseHelper helper;

    public ConcourseFragment(Client user) {
        // Required empty public constructor
        this.user = user;
        this.activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_concourse, container, false);
        DatabaseManager.init(getContext());
        helper = DatabaseManager.getInstance().getHelper();

        spn_month           = (Spinner) rootView.findViewById(R.id.spn_month);


//       // ArrayList<Month> months = null;
//        try {
////            months = (ArrayList<Month>) helper.getMonthDao().queryForAll();
//            months = (ArrayList<Month>) helper.getMonthDao().queryBuilder().orderBy("id",false).query();
//            showMonts(months);
//        } catch (SQLException e) {
//            Toast.makeText(getContext(), "No se encontraron datos", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }



        spn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String label = parent.getItemAtPosition(position).toString();
//                int mount_id = ((Month) spn_month.getSelectedItem()).getId () ;
//                String label = ((Month) spn_month.getSelectedItem () ).getMount () ;
        //        Toast.makeText(getContext(), label + String.valueOf(mount_id) , Toast.LENGTH_SHORT).show();

//                ly_container.removeAllViews();
//                try {
//                    month =  helper.getMonthDao().queryForId(mount_id);
//                    getDataConcourse();
//
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "No se pudo encontrar datos", Toast.LENGTH_LONG).show();
//                    return;
//                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        ly_container = (LinearLayout) rootView.findViewById(R.id.ly_container);
        ly_container.removeAllViews();



        return rootView;
    }

//    private void showMonts(ArrayList<Month> months) {
//        // this.months      = months;
//        ArrayAdapter<Month>    adapter             = new ArrayAdapter<Month>(this.getContext(),R.layout.simple_spinner_item, months);
//        spn_month.setAdapter(adapter);
//    }

    private void getDataConcourse(){

        dialog  = new Dialog(getContext());

        showProgressDialog();

        ArrayList<Concourse> concourses  = new ArrayList<Concourse>();
        Concourse concourse = new Concourse();


        Map<String, String> map = new HashMap<String, String>();
        /*map.put("id", user.getId_data());*/
     //   map.put("month", month.getMonth_number());
       // map.put("month", "04");
    //    map.put("year", String.valueOf(month.getYear_number()));

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Service service =  restApiAdapter.getClientService(getContext());
//        Call<JsonObject> call =  service.getConcoursesForSeller(map);
        Call<JsonObject> call =  service.getAllConcoursesForSeller(map);

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

                            JsonArray dataConcourses = resultJson.getAsJsonArray("concourses");

                            for (int i = 0; i < dataConcourses.size(); i++) {
                                JsonObject dataConcourse =  dataConcourses.get(i).getAsJsonObject();

                                // Cargando Datos en tabla

                                View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_buttons_councurse_dynamic, null, false);
                                TextView txt_concurse =  layout.findViewById(R.id.tv_0);
                                txt_concurse.setText(dataConcourse.get("fullname").getAsString());

                                ViewButton btn_0 = layout.findViewById(R.id.btn_0);

                                btn_0.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Toast.makeText(getContext(), dataConcourse.get("fullname").getAsString(),Toast.LENGTH_SHORT).show();

                                        //fragment = new ConcourseDetailFragment(user,month,dataConcourse.get("_id").getAsString());
                                        fragment = new ConcourseDetailFragment(user,dataConcourse.get("_id").getAsString(),dataConcourse.get("concurse_detail_id").getAsInt());
                                        //getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                .addToBackStack(null).commit();
                                    }
                                });


                                ly_container.addView(layout);
                                Log.d(LOG_TAG, "Creando: " + dataConcourse.get("fullname").getAsString());

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