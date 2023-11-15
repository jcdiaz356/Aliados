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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class StatusAccountFragment extends Fragment {
    private static final String LOG_TAG = StatusAccountFragment.class.getSimpleName();

    private Spinner spn_month;
    private Dialog dialog;
    private TextView tv_soles_ganados;
    private TableLayout tbl_concurse;
    private DatabaseHelper helper;


  //  private Month month;
    private Client user;

    public StatusAccountFragment(Client user) {
        this.user = user;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status__account, container, false);

        DatabaseManager.init(getContext());
        helper = DatabaseManager.getInstance().getHelper();

        spn_month           = (Spinner) rootView.findViewById(R.id.spn_month);
        tv_soles_ganados    = (TextView) rootView.findViewById(R.id.tv_soles_ganados);
        tbl_concurse = (TableLayout) rootView.findViewById(R.id.tbl_concurse);


//        ArrayList<Month> months = null;
//        try {
//           // months = (ArrayList<Month>) helper.getMonthDao().queryForAll();
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
//                Toast.makeText(getContext(), label + String.valueOf(mount_id) , Toast.LENGTH_SHORT).show();
//
//                try {
//                    month =  helper.getMonthDao().queryForId(mount_id);
//                    getData();
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









        return rootView;
    }


//    private void showMonts(ArrayList<Month> months) {
//        // this.months      = months;
//        ArrayAdapter<Month>    adapter             = new ArrayAdapter<Month>(this.getContext(),R.layout.simple_spinner_item, months);
//        spn_month.setAdapter(adapter);
//    }


    private void getData(){

        dialog  = new Dialog(getContext());

        showProgressDialog();


        Map<String, String> map = new HashMap<String, String>();
        /*map.put("id", user.getId_data());*/
//        map.put("month", month.getMonth_number());
//        map.put("year", String.valueOf(month.getYear_number()));

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Service service =  restApiAdapter.getClientService(getContext());
        Call<JsonObject> call =  service.getEstadoCuenta(map);

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


                            JsonArray dataJsonArray = resultJson.getAsJsonArray("allForConcourse");
                            tbl_concurse.removeAllViews();
                            for (int i = 0; i < dataJsonArray.size(); i++) {
                                JsonObject dataObject =  dataJsonArray.get(i).getAsJsonObject();
                                String fullname = (dataObject.get("fullname").isJsonNull()) ? "" : String.valueOf(dataObject.get("fullname").getAsString());
                                String ganados = (dataObject.get("ganados").isJsonNull()) ? "0" : String.valueOf(dataObject.get("ganados").getAsString());

                                View row_table = LayoutInflater.from(getContext()).inflate(R.layout.row_table_concourse, null, false);
                                TextView textView0 = row_table.findViewById(R.id.textView0);
                                TextView textView1 = row_table.findViewById(R.id.textView1);
                                textView0.setText(String.valueOf(i+1) + ". " + fullname);
                                textView1.setText("S/. " + ganados);
                                int finalI = i;
                                textView0.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(getContext(), fullname,Toast.LENGTH_SHORT).show();


                                    }
                                });
                                tbl_concurse.addView(row_table);
                            }


                            JsonArray jsonArray = resultJson.getAsJsonArray("allSoles");

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonObject object =  jsonArray.get(i).getAsJsonObject();
                                String ganados = (object.get("ganados").isJsonNull()) ? "" : String.valueOf(object.get("ganados").getAsDouble());
                                tv_soles_ganados.setText("S/. " + ganados);
                            }

                        } else {
                            Toast.makeText(getContext(), "No se encontró datos", Toast.LENGTH_LONG).show();
                        }

                        dialog.dismiss();

                    }else {
                        try {
                            Log.d(LOG_TAG,"Error no se pudo obtener información: " + response.errorBody().string());
                            // processInteractor.showErrorServer("Error: intetelo nuevamente");
                            Toast.makeText(getContext(), "No se pudo obtener información", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Server error, no se pudo obtener información", Toast.LENGTH_SHORT).show();

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