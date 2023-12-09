package com.devsystem.aliados.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.devsystem.aliados.R;
import com.devsystem.aliados.db.DatabaseManager;
import com.devsystem.aliados.model.Award;
import com.devsystem.aliados.model.Category;
import com.devsystem.aliados.model.CategoryAwardDetail;
import com.devsystem.aliados.model.Client;
import com.devsystem.aliados.model.Program;
import com.devsystem.aliados.repo.AwardRepo;
import com.devsystem.aliados.repo.CategoryAwardDetailRepo;
import com.devsystem.aliados.repo.CategoryRepo;
import com.devsystem.aliados.repo.ProgramRepo;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ConcourseDetailsTwoFragment extends Fragment {
    private static final String LOG_TAG = ConcourseDetailsTwoFragment.class.getSimpleName();

    private TextView tv_concurso,tv_fuerza_venta,tv_tipo,tv_title ;
    private TextView tv_premio, tv_punto_potencial, tv_alcance_llave,tv_gana_llave,tv_punto_final,tv_updated_at ;
    private Client                      client;
    private CategoryRepo                categoryRepo;
    private ProgramRepo                 programRepo;
    private AwardRepo                   awardRepo;
    private CategoryAwardDetailRepo     categoryAwardDetailRepo;
    private LinearLayout ly_categories_indicator;
    private DecimalFormat formatea = new DecimalFormat("###,###");


    public ConcourseDetailsTwoFragment(Client client) {
        // Required empty public constructor
        this.client = client;
      //  this.month = month;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_concourse_details_two, container, false);

        DatabaseManager.init(getContext());

        categoryRepo                = new CategoryRepo(getContext());
        programRepo                 = new ProgramRepo(getContext());
        awardRepo                   = new AwardRepo(getContext());
        categoryAwardDetailRepo     = new CategoryAwardDetailRepo(getContext());

        Program program                                     = (Program) programRepo.findFirstReg();
        List<Category> categories                           = (List<Category>) categoryRepo.findAll();
        Award award                                         = (Award) awardRepo.findFirstReg();
        List<CategoryAwardDetail> categoryAwardDetails      = (List<CategoryAwardDetail>) categoryAwardDetailRepo.findAll();

        tv_concurso                 = (TextView)  rootView.findViewById(R.id.tv_concurso);
        tv_fuerza_venta             = (TextView)  rootView.findViewById(R.id.tv_fuerza_venta);
        tv_tipo                     = (TextView)  rootView.findViewById(R.id.tv_tipo);
        tv_punto_potencial          = rootView.findViewById(R.id.tv_punto_potencial);
        tv_alcance_llave            = rootView.findViewById(R.id.tv_alcance_llave);
        tv_gana_llave               = rootView.findViewById(R.id.tv_gana_llave);
        tv_punto_final              = rootView.findViewById(R.id.tv_punto_final);
        tv_updated_at               = rootView.findViewById(R.id.tv_updated_at);
        tv_title                    = rootView.findViewById(R.id.tv_title);
        tv_updated_at               = (TextView) rootView.findViewById(R.id.tv_updated_at);

        ly_categories_indicator     = rootView.findViewById(R.id.ly_categories_indicator);

        tv_punto_potencial.setText(String.valueOf(formatea.format(award.getPoint_total())));
        tv_alcance_llave.setText(String.valueOf(Math.round(award.getAvance_total())) + " %");
        tv_gana_llave.setText(( award.getKeyv_total() == 1) ? "SI" : "NO");
        tv_punto_final.setText(String.valueOf(formatea.format(award.getPoint_real_total())));
        tv_title.setText(client.getClient_type().getFullname());

        SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date miFecha = formatoOriginal.parse(award.getFecha()); // Convierte la cadena a un objeto Date
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());// Crea un objeto SimpleDateFormat para el formato deseado
            String fechaFormateada = formatoFecha.format(miFecha);// Aplica el formato a la fecha
            tv_updated_at.setText(fechaFormateada); // Muestra la fecha formateada en tu TextView
        } catch (ParseException e) {
            // Maneja la excepción en caso de error al analizar la cadena
            e.printStackTrace();
            // Puedes mostrar un mensaje de error o realizar alguna otra acción adecuada
        }

        ly_categories_indicator.removeAllViews();
        for(CategoryAwardDetail categoryAwardDetail: categoryAwardDetails){
            View ly_group = LayoutInflater.from(getContext()).inflate(R.layout.list_group, null, false);
            TextView tvTitle = ly_group.findViewById(R.id.tvTitle);
            tvTitle.setText(String.valueOf(categoryAwardDetail.getFullname()));
            ly_categories_indicator.addView(ly_group);

            View ly_item;
            TextView tv_description;


            ly_item = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null, false);
            tv_description = ly_item.findViewById(R.id.tv_description);
            tv_description.setText( "PLAN (PEN): " +  String.valueOf(formatea.format(categoryAwardDetail.getAward_detail().getPlan()))  );
            ly_categories_indicator.addView(ly_item);

            ly_item = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null, false);
            tv_description = ly_item.findViewById(R.id.tv_description);
            tv_description.setText( "REAL (PEN): " +  String.valueOf(formatea.format(categoryAwardDetail.getAward_detail().getRealv())));
            ly_categories_indicator.addView(ly_item);

            ly_item = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null, false);
            tv_description = ly_item.findViewById(R.id.tv_description);
            tv_description.setText( "% ALCANCE: " +  String.valueOf(Math.round(categoryAwardDetail.getAward_detail().getAvance())) + " % ");
            ly_categories_indicator.addView(ly_item);

            ly_item = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null, false);
            tv_description = ly_item.findViewById(R.id.tv_description);
            tv_description.setText( "PUNTOS POTENCIALES: " +  String.valueOf(formatea.format(categoryAwardDetail.getAward_detail().getPoints())));
            ly_categories_indicator.addView(ly_item);


        }




        return rootView;
    }

}