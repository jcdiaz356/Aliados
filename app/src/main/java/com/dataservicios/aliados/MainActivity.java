package com.dataservicios.aliados;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.User;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Activity activity = this;
    private ProgressBar pbLoading;
    private TextView tvVersion, tvLoad;
    private int i=0;
    private DatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseManager.init(activity);
        helper = DatabaseManager.getInstance().getHelper();

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

                List<User> items = null;
                User user = null;
                try {
                    items = helper.getUserDao().queryForAll();
                } catch (SQLException e) {
                    Toast.makeText(activity, "Error interno database", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                Log.d(LOG_TAG, "Obteniedo Todos los clientes para limpiar");
                for (User object : items) {
                    user = (User) object;
                }
                if(user == null) {
                    Intent i = new Intent(activity, LoginActivity.class);
                    // i.putExtra("session_create"              , sessionCreate);
                    startActivity(i);
                    finish();
                } else {
                    if(user.getSave_ssesion() == 1) {
                        Intent intent = new Intent(activity, PanelAdminActivity.class);
                        int user_id = user.getId();
                        intent.putExtra("user_id"              , user_id);
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
