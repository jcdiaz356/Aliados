package com.dataservicios.aliados.views.fragments;

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

import com.dataservicios.aliados.model.AwardDetail;
import com.dataservicios.aliados.model.Program;
import com.dataservicios.aliados.repo.AwardDetailRepo;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatusAccountFragment extends Fragment {
    private static final String LOG_TAG = StatusAccountFragment.class.getSimpleName();

    private Spinner spn_month;
    private TextView tv_soles_ganados;
    private TableLayout tbl_concurse;
    private DatabaseHelper helper;
    private AwardDetailRepo awardDetailRepo;
    private ProgramRepo programRepo;
    private DecimalFormat formatea = new DecimalFormat("###,###");

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

        awardDetailRepo = new AwardDetailRepo(getContext());

        spn_month           = (Spinner) rootView.findViewById(R.id.spn_month);
        tv_soles_ganados    = (TextView) rootView.findViewById(R.id.tv_soles_ganados);
        tbl_concurse = (TableLayout) rootView.findViewById(R.id.tbl_concurse);
        programRepo = new ProgramRepo(getContext());


        this.getDataAwardDetails();



        ArrayList<Program> programs = (ArrayList<Program>) programRepo.findAll();
        showLoadPrograms(programs);

        spn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int program_id = ((Program) spn_month.getSelectedItem()).getId () ;
                String label = ((Program) spn_month.getSelectedItem()).getMonth() ;
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



    private void getDataAwardDetails(){
        ArrayList<AwardDetail> ValuesAwardDetail = (ArrayList<AwardDetail>) awardDetailRepo.findAll();
        tbl_concurse.removeAllViews();
        for (AwardDetail awardDetail: ValuesAwardDetail) {
            /*Log.d(LOG_TAG, "id: " + awardDetail.getId() + " fullname: " + awardDetail.getFullname() + " ganados: " + awardDetail.getGanados());*/
            String CategoryName = awardDetail.getCategory().getFullname();

            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            // Aplica el formato al número
            String MontoSoles = String.valueOf(formatea.format(awardDetail.getPoint_real()));

            String ganados = numberFormat.format(awardDetail.getReal_total());
            tv_soles_ganados.setText(String.valueOf(formatea.format(awardDetail.getPoint_real_total())));

            /*String MontoSoles = String.valueOf(awardDetail.getRealv());*/

            View row_table = LayoutInflater.from(getContext()).inflate(R.layout.row_table_concourse, null, false);
            TextView textView0 = row_table.findViewById(R.id.textView0);
            TextView textView1 = row_table.findViewById(R.id.textView1);
            textView0.setText( CategoryName);
            textView1.setText( MontoSoles);

            tbl_concurse.addView(row_table);
        }
    }

    private void showLoadPrograms(ArrayList<Program> programs) {
        // this.months      = months;
        ArrayAdapter<Program>    adapter             = new ArrayAdapter<Program>(this.getContext(),R.layout.simple_spinner_item, programs);
        spn_month.setAdapter(adapter);
    }



}