package com.dataservicios.aliados.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dataservicios.aliados.R;
import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.Client;
import com.dataservicios.aliados.repo.ClientRepo;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Activity activity = this;
    private ProgressBar pbLoading;
    private TextView tvVersion, tvLoad;
    private int i=0;
    private DatabaseHelper helper;
    private ClientRepo userRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseManager.init(activity);

        userRepo = new ClientRepo(activity);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvLoad = (TextView) findViewById(R.id.tvLoad);

        PackageInfo pinfo;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pinfo.versionName;
            tvVersion.setText("Ver. " + versionName);
            //ET2.setText(versionNumber);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        CountDownTimer mCountDownTimer;

        mCountDownTimer=new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i + millisUntilFinished);
                i++;
               // pbLoading.setProgress((int)i*100/(5000/1000));
                tvLoad.setText("Cargando información...");

            }

            @Override
            public void onFinish() {

                List<Client> items = null;
                Client client = null;
//                try {
                   // items = helper.getUserDao().queryForAll();
                    items = (List<Client>) userRepo.findAll();
//                } catch (SQLException e) {
//                    Toast.makeText(activity, "Error interno database", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
                Log.d(LOG_TAG, "Obteniedo Todos los clientes para limpiar");
                for (Client object : items) {
                    client = (Client) object;
                }
                if(client == null) {
                    Intent i = new Intent(activity, LoginActivity.class);
                    // i.putExtra("session_create"              , sessionCreate);
                    startActivity(i);
                    finish();
                } else {
                    if(client.getSave_ssesion() == 1) {
                        Intent intent = new Intent(activity, PanelAdminActivity.class);
                        int client_id = client.getId();
                        intent.putExtra("client_id"              , client_id);
                        startActivity(intent);
                        finish();
                    } else{
                        Intent i = new Intent(activity, LoginActivity.class);
                        // i.putExtra("session_create"              , sessionCreate);
                        startActivity(i);
                        finish();
                    }
                }



            }
        };
        mCountDownTimer.start();

    }
}
