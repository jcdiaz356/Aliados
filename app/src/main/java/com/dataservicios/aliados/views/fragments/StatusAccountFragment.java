package com.dataservicios.aliados.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.aliados.model.AwardDetail;
import com.dataservicios.aliados.model.Program;
import com.dataservicios.aliados.repo.AwardDetailRepo;
import com.dataservicios.aliados.repo.ClientProgramRepo;
import com.dataservicios.aliados.repo.ClientProgramRepoImpl;
import com.dataservicios.aliados.repo.ProgramRepo;
import com.dataservicios.aliados.util.ShowProcessDialog;
import com.dataservicios.aliados.views.ClientProgramView;
import com.dataservicios.aliados.R;
import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.Client;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class StatusAccountFragment extends Fragment implements ClientProgramView {
    private static final String LOG_TAG = StatusAccountFragment.class.getSimpleName();

    private Spinner spn_month;
    private TextView tv_soles_ganados,tv_punto_acumulado;
    private TableLayout tbl_concurse;
    private DatabaseHelper helper;
    private AwardDetailRepo awardDetailRepo;
    private ProgramRepo programRepo;
    private DecimalFormat formatea = new DecimalFormat("###,###");
    private ShowProcessDialog showProcessDialog;

  //  private Month month;
    private Client client;
    private ClientProgramRepo clientProgramRepo;

    public StatusAccountFragment(Client user) {
        this.client = user;
        clientProgramRepo = new ClientProgramRepoImpl(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status__account, container, false);


        showProcessDialog = new ShowProcessDialog(getContext());

        awardDetailRepo = new AwardDetailRepo(getContext());

        spn_month           = (Spinner) rootView.findViewById(R.id.spn_month);
        tv_soles_ganados    = (TextView) rootView.findViewById(R.id.tv_soles_ganados);
        tv_punto_acumulado  = (TextView) rootView.findViewById(R.id.tv_punto_acumulado);
        tbl_concurse = (TableLayout) rootView.findViewById(R.id.tbl_concurse);
        programRepo = new ProgramRepo(getContext());



        ArrayList<Program> programs = (ArrayList<Program>) programRepo.findAll();
        showLoadPrograms(programs);

        spn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int program_id = ((Program) spn_month.getSelectedItem()).getId () ;
                String label = ((Program) spn_month.getSelectedItem()).getMonth() ;
                //
                programRepo.findById(program_id);
               // Toast.makeText(getContext(), label + String.valueOf(program_id) , Toast.LENGTH_SHORT).show();
                clientProgramRepo.getClientProgram(client.getId(),program_id,getActivity());


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


            SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date miFecha = null;
            String fechaFormateada;
            try {
                miFecha = formatoOriginal.parse(awardDetail.getDate_acumulated());
                // Crea un objeto SimpleDateFormat para el formato deseado
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                // Aplica el formato a la fecha
                fechaFormateada = formatoFecha.format(miFecha);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            tv_soles_ganados.setText(String.valueOf(formatea.format(awardDetail.getPoint_real_total())));
            //tv_punto_acumulado.setText("Puntos acumulados al " +  String.valueOf(awardDetail.getDate_acumulated()).substring(0,10) + " S/. " + awardDetail.getPoint_acumulated());
            tv_punto_acumulado.setText("Puntos acumulados al " +  fechaFormateada + " S/. " + formatea.format(awardDetail.getPoint_acumulated()));

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


    @Override
    public void getDataSuccess(boolean success) {

        if(success) {
            getDataAwardDetails();
        }
    }

    @Override
    public void getDataError(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void serverError(Throwable error) {

        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar() {
        showProcessDialog.showDialog();
    }

    @Override
    public void hideProgresbar() {
        showProcessDialog.hideDialog();
    }
}