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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.aliados.model.Award;
import com.dataservicios.aliados.model.Category;
import com.dataservicios.aliados.model.Program;
import com.dataservicios.aliados.repo.AwardRepo;
import com.dataservicios.aliados.repo.CategoryRepo;
import com.dataservicios.aliados.repo.ProgramRepo;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConcourseFragment extends Fragment {
    private static final String LOG_TAG = ConcourseFragment.class.getSimpleName();

    // private ViewButton btn_0;
    private Activity activity;
    private TextView tv_fecha_inicio, tv_fecha_fin, tv_description,tv_title;
    private TextView tv_premio, tv_punto_potencial, tv_alcance_llave,tv_gana_llave,tv_punto_final,tv_updated_at ;
    private TableLayout tbl_categories;

    private Fragment fragment;
    private LinearLayout ly_container;
    private Client user;
    private Dialog dialog;

    private DatabaseHelper helper;
    private CategoryRepo categoryRepo;
    private ProgramRepo programRepo;
    private AwardRepo awardRepo;

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

        categoryRepo        = new CategoryRepo(getContext());
        programRepo         = new ProgramRepo(getContext());
        awardRepo           = new AwardRepo(getContext());


        tv_fecha_inicio     = rootView.findViewById(R.id.tv_fecha_inicio);
        tv_fecha_fin        = rootView.findViewById(R.id.tv_fecha_fin);
        tv_description      = rootView.findViewById(R.id.tv_description);
        tv_premio           = rootView.findViewById(R.id.tv_premio);
        tv_punto_potencial  = rootView.findViewById(R.id.tv_punto_potencial);
        tv_alcance_llave    = rootView.findViewById(R.id.tv_alcance_llave);
        tv_gana_llave       = rootView.findViewById(R.id.tv_gana_llave);
        tv_punto_final      = rootView.findViewById(R.id.tv_punto_final);
        tv_updated_at      = rootView.findViewById(R.id.tv_updated_at);
        tv_title      = rootView.findViewById(R.id.tv_title);

        tbl_categories        = (TableLayout) rootView.findViewById(R.id.tbl_categories);



        Program program             = (Program) programRepo.findFirstReg();
        List<Category> categories   = (List<Category>) categoryRepo.findAll();
        Award award                 = (Award) awardRepo.findFirstReg();

        tv_fecha_inicio.setText(program.getDate_start());
        tv_fecha_fin.setText(program.getDate_end());
        tv_description.setText(program.getDescription());

        tv_premio.setText("1 PUNTO POR CADA S/ 177 *Incluye IGV");
        tv_punto_potencial.setText("5101");
        tv_alcance_llave.setText(String.valueOf(award.getAvance_total()) + " %");
        tv_gana_llave.setText(( award.getKeyv_total() == 1) ? "SI" : "NO");
        tv_punto_final.setText("--");
        tv_title.setText(program.getFullname());

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


        tbl_categories.removeAllViews();
        for(Category category: categories){
            View row_table = LayoutInflater.from(getContext()).inflate(R.layout.row_table_categories_general, null, false);
            TextView textView0 = row_table.findViewById(R.id.tv_title);
            TextView textView1 = row_table.findViewById(R.id.tv_description);
            textView0.setText(String.valueOf(category.getFullname()));
            textView1.setText(category.getDescription());

            tbl_categories.addView(row_table);
        }








//        ly_container = (LinearLayout) rootView.findViewById(R.id.ly_container);
//        ly_container.removeAllViews();

        return rootView;
    }


    private void getDataConcourse() {

        dialog = new Dialog(getContext());

        showProgressDialog();

        ArrayList<Concourse> concourses = new ArrayList<Concourse>();
        Concourse concourse = new Concourse();

        Map<String, String> map = new HashMap<String, String>();
        /*map.put("id", user.getId_data());*/
        //   map.put("month", month.getMonth_number());
        // map.put("month", "04");
        //    map.put("year", String.valueOf(month.getYear_number()));

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Service service = restApiAdapter.getClientService(getContext());
//        Call<JsonObject> call =  service.getConcoursesForSeller(map);
        Call<JsonObject> call = service.getAllConcoursesForSeller(map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {

                    JsonObject resultJson;
                    if (response.isSuccessful()) {
                        Log.d(LOG_TAG, response.body().toString());
                        resultJson = response.body();
                        Boolean status = resultJson.get("success").getAsBoolean();
                        if (status) {

                            JsonArray dataConcourses = resultJson.getAsJsonArray("concourses");

                            for (int i = 0; i < dataConcourses.size(); i++) {
                                JsonObject dataConcourse = dataConcourses.get(i).getAsJsonObject();

                                // Cargando Datos en tabla

                                View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_buttons_councurse_dynamic, null, false);
                                TextView txt_concurse = layout.findViewById(R.id.tv_0);
                                txt_concurse.setText(dataConcourse.get("fullname").getAsString());

                                ViewButton btn_0 = layout.findViewById(R.id.btn_0);

                                btn_0.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Toast.makeText(getContext(), dataConcourse.get("fullname").getAsString(), Toast.LENGTH_SHORT).show();

                                        //fragment = new ConcourseDetailFragment(user,month,dataConcourse.get("_id").getAsString());
                                        fragment = new ConcourseDetailFragment(user, dataConcourse.get("_id").getAsString(), dataConcourse.get("concurse_detail_id").getAsInt());
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

                    } else {
                        try {
                            Log.d(LOG_TAG, "Error no se pudo obtener información: " + response.errorBody().string());
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

    private void showProgressDialog() {
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
        dialog.addContentView(ll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();

    }
}