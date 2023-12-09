package com.devsystem.aliados.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.devsystem.aliados.R;
import com.devsystem.aliados.db.DatabaseManager;
import com.devsystem.aliados.model.Award;
import com.devsystem.aliados.model.Client;
import com.devsystem.aliados.model.Program;
import com.devsystem.aliados.repo.AwardRepo;
import com.devsystem.aliados.repo.ClientProgramRepoImpl;
import com.devsystem.aliados.repo.ProgramRepo;
import com.devsystem.aliados.util.ShowProcessDialog;
import com.devsystem.aliados.views.ClientProgramView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class WelcomeFragment extends Fragment implements ClientProgramView {

    private static final String LOG_TAG = WelcomeFragment.class.getSimpleName();
    private Activity activity;
    private Spinner spn_program;
    private TextView tv_user_name,tv_dex,tv_fuerza_venta,tv_llave_general,tv_foods,tv_home_care,tv_porc_gestion,tv_updated_at,tv_llave;
    private ImageView iv_porc_gestion;
    private Client client;
    private Award award;
    private ProgramRepo programRepo;
    private AwardRepo awardRepo;
    private ClientProgramRepoImpl clientProgramRepoImpl;
    private ShowProcessDialog showProcessDialog;
    public WelcomeFragment(Client client) {
        // Required empty public constructor
        this.client = client;
     //   this.activity = getActivity();
        clientProgramRepoImpl = new ClientProgramRepoImpl(this);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);

        DatabaseManager.init(getContext());

        showProcessDialog = new ShowProcessDialog(getContext());

        programRepo = new ProgramRepo(activity);
        awardRepo = new AwardRepo(activity);

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
        tv_llave = (TextView) rootView.findViewById(R.id.tv_llave);
        tv_user_name.setText(client.getFullname());

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
                clientProgramRepoImpl.getClientProgram(client.getId(),program_id,activity);
               // Toast.makeText(getContext(), label + String.valueOf(program_id) , Toast.LENGTH_SHORT).show();
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

        Map<String, String> map = new HashMap<String, String>();

        DatabaseManager.init(activity);
        awardRepo = new AwardRepo(activity);
        award = (Award) awardRepo.findFirstReg();

        // Crea un objeto NumberFormat para el formato deseado
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        // Aplica el formato al número
        String planSi = numberFormat.format(award.getPlan_total());

        tv_llave_general.setText("S/."+planSi);
        String llave_value = ( award.getKeyv_total() == 1) ? "SI" : "NO" ;
        tv_llave.setText(llave_value);
        String realSi = numberFormat.format(award.getReal_total());
        tv_foods.setText("S/."+realSi);
        String faltante = numberFormat.format(award.getPlan_total()-award.getReal_total());
        tv_home_care.setText("S/."+faltante);
        tv_porc_gestion.setText(String.valueOf(Math.round(award.getAvance_total())) + " %" );

        // Crea un objeto SimpleDateFormat para el formato deseado
        SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            // Convierte la cadena a un objeto Date
            Date miFecha = formatoOriginal.parse(award.getFecha());

            // Crea un objeto SimpleDateFormat para el formato deseado
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

            // Aplica el formato a la fecha
            String fechaFormateada = formatoFecha.format(miFecha);

            // Muestra la fecha formateada en tu TextView
            tv_updated_at.setText(fechaFormateada);
        } catch (ParseException e) {
            // Maneja la excepción en caso de error al analizar la cadena
            e.printStackTrace();
            // Puedes mostrar un mensaje de error o realizar alguna otra acción adecuada
        }


        /*float value_key_personal = Float.valueOf(award.getAvance_total());*/
        float value_key_personal = award.getAvance_total().floatValue();
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
        if( value_key_personal >= 100)  iv_porc_gestion.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.crono_10xxxhdpi));

    }


    @Override
    public void getDataSuccess(boolean success) {

        if(success) {
            getDataWelcome();
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