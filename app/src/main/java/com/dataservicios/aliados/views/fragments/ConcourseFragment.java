package com.dataservicios.aliados.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dataservicios.aliados.R;
import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.Client;
import com.dataservicios.aliados.model.ClientTypeCategory;
import com.dataservicios.aliados.model.Program;
import com.dataservicios.aliados.repo.AwardRepo;
import com.dataservicios.aliados.repo.ClientTypeCategoryRepo;
import com.dataservicios.aliados.repo.ProgramRepo;
import com.ornach.nobobutton.ViewButton;

import java.util.List;
public class ConcourseFragment extends Fragment {
    private static final String LOG_TAG = ConcourseFragment.class.getSimpleName();

    // private ViewButton btn_0;
    private Activity activity;
    private TextView tv_fecha_inicio, tv_fecha_fin, tv_description,tv_title;
    private TextView tv_premio;
    private TableLayout tbl_categories;

    private Fragment fragment;
    private LinearLayout ly_container;
    private Client client;

    private DatabaseHelper helper;
    private ProgramRepo programRepo;
    private AwardRepo awardRepo;
    private ClientTypeCategoryRepo clientTypeCategoryRepo;
    private ViewButton btn_view_more;

    public ConcourseFragment(Client client) {
        // Required empty public constructor
        this.client = client;
        this.activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_concourse, container, false);
        DatabaseManager.init(getContext());

        programRepo                 = new ProgramRepo(getContext());
        awardRepo                   = new AwardRepo(getContext());
        clientTypeCategoryRepo      = new ClientTypeCategoryRepo(getContext());

        tv_fecha_inicio     = rootView.findViewById(R.id.tv_fecha_inicio);
        tv_fecha_fin        = rootView.findViewById(R.id.tv_fecha_fin);
        tv_description      = rootView.findViewById(R.id.tv_description);
        tv_premio           = rootView.findViewById(R.id.tv_premio);

        tv_title            = rootView.findViewById(R.id.tv_title);
        btn_view_more       = rootView.findViewById(R.id.btn_view_more);

        tbl_categories        = (TableLayout) rootView.findViewById(R.id.tbl_categories);

        Program program                                     = (Program) programRepo.findFirstReg();
        List<ClientTypeCategory> clientTypeCategories       = (List<ClientTypeCategory>) clientTypeCategoryRepo.findAll();

        tv_fecha_inicio.setText(program.getDate_start().substring(0,10));
        tv_fecha_fin.setText(program.getDate_end().substring(0,10));
        tv_description.setText(program.getDescription());
        tv_title.setText(client.getClient_type().getFullname());
        tv_premio.setText(client.getClient_type().getAward());

        tbl_categories.removeAllViews();
        for(ClientTypeCategory category: clientTypeCategories){
            View row_table = LayoutInflater.from(getContext()).inflate(R.layout.row_table_categories_general, null, false);
            TextView textView0 = row_table.findViewById(R.id.tv_title);
            TextView textView1 = row_table.findViewById(R.id.tv_description);
            textView0.setText(String.valueOf(category.getFullname()));
            textView1.setText(category.getDescription());
            tbl_categories.addView(row_table);
        }


        btn_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ConcourseDetailsTwoFragment(client);
                //getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null).commit();
            }
        });


        return rootView;
    }

}