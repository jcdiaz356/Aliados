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


import com.ornach.nobobutton.NoboButton;
import com.dataservicios.aliados.R;
import com.dataservicios.aliados.model.Promotion;
import com.dataservicios.aliados.model.Client;
import com.dataservicios.aliados.servicesApi.RestApiAdapter;
import com.dataservicios.aliados.servicesApi.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PromotionsFragment extends Fragment {


    private static final String LOG_TAG = PromotionsFragment.class.getSimpleName();
    //private Activity activity = this;
    private NoboButton btn_one_page;
    private Dialog dialog;
    private ArrayList<Promotion> promotions = null ;
    private LinearLayout ly_contendor;
    private Client user;

    public PromotionsFragment(Client user) {
        // Required empty public constructor
        this.user = user;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_promotions, container, false);


//        btn_one_page           = (NoboButton) rootView.findViewById(R.id.btn_one_page);
//        btn_one_page.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CustomDialog cdd = new CustomDialog(getActivity());
//                cdd.show();
//            }
//        });










        dialog  = new Dialog(getContext());
        showProgressDialog();
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Service service =  restApiAdapter.getClientService(getContext());
        Call<ArrayList<Promotion>> call =  service.getDataPromotions();

        call.enqueue(new Callback<ArrayList<Promotion>>() {
            @Override
            public void onResponse(Call<ArrayList<Promotion>> call, Response<ArrayList<Promotion>> response) {


                try {


                    if(response.isSuccessful()){
                        Log.d(LOG_TAG,response.body().toString());
                        promotions = response.body();

                        List<Promotion> items = null;

                        ly_contendor = (LinearLayout) rootView.findViewById(R.id.ly_contendor);
                        ly_contendor.removeAllViews();

                        for (Promotion object: promotions) {
                            // Cargando Datos en tabla
                            View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_button_promotions, null, false);
                            TextView txt_promotions =  layout.findViewById(R.id.txt_promotions);
                            txt_promotions.setText(object.getName());

                            txt_promotions.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                     CustomDialog cdd = new CustomDialog(getActivity(),object.getId(),user);
                                     cdd.show();
                                    Toast.makeText(getContext(), object.getName().toString(),Toast.LENGTH_SHORT).show();
                                }
                            });

                            ly_contendor.addView(layout);

                            Log.d(LOG_TAG, "Creando: " + object.toString());
                        }

                        dialog.dismiss();




                    }else {
                        try {
                            Log.d(LOG_TAG,"Error credenciales: " + response.errorBody().string());
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
            public void onFailure(Call<ArrayList<Promotion>> call, Throwable t) {
                dialog.dismiss();
                Log.d(LOG_TAG, "Error En red: " + t.getMessage());
                Log.d(LOG_TAG, t.getMessage());
                Toast.makeText(getContext(), "Server error, no se pudo obtener información", Toast.LENGTH_SHORT).show();

            }

        });





        return rootView;
    }


    private void showProgressDialog(){
        int llPadding = 50;
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setBackgroundColor(getActivity().getColor(R.color.red_500));

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