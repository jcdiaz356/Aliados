package com.dataservicios.aliados;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ornach.nobobutton.NoboButton;
import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.Month;
import com.dataservicios.aliados.model.User;
import com.dataservicios.aliados.servicesApi.RestApiAdapter;
import com.dataservicios.aliados.servicesApi.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String LOG_TAG = ChangePasswordActivity.class.getSimpleName();

    private Activity activity = this ;
    private NoboButton btn_change_password;
    private Dialog dialog;
    private TextInputEditText txt_usercode,txt_password;
    private TextInputLayout txt_input_passsword,txt_input_re_passsword;
    private DatabaseHelper helper;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        DatabaseManager.init(activity);
        helper = DatabaseManager.getInstance().getHelper();

        Bundle bundle = getIntent().getExtras();
        code   = bundle.getString("code");

        dialog  = new Dialog(activity);
        btn_change_password = (NoboButton)  findViewById(R.id.btn_change_password);

        txt_input_passsword = (TextInputLayout) findViewById(R.id.txt_input_passsword);
        txt_input_re_passsword = (TextInputLayout) findViewById(R.id.txt_input_re_passsword);

        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog  = new Dialog(activity);

                showProgressDialog();
                String pasword = txt_input_passsword.getEditText().getText().toString().trim();
                String re_password = txt_input_re_passsword.getEditText().getText().toString().trim();

//                code = "485706";
//                txt_input_usercode.getEditText().setText(code);

                if(pasword.equals("")){
                    dialog.dismiss();
                    Toast.makeText(activity, "Debe ingresar una contraseña", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!pasword.equals(re_password)){
                    dialog.dismiss();
                    Toast.makeText(activity, "La contraseña no es igual, vuelva a intentar", Toast.LENGTH_LONG).show();
                   return;
                }


                Map<String, String> map = new HashMap<String, String>();
                map.put("code", code);
                map.put("password", pasword);
                map.put("re_password", re_password);

                RestApiAdapter restApiAdapter = new RestApiAdapter();
                Service service =  restApiAdapter.getClientService(activity);
                Call<JsonObject> call =  service.changePassword(map);

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        try {

                            JsonObject userJsonObject;
                            if(response.isSuccessful()){
                                Log.d(LOG_TAG,response.body().toString());
                                userJsonObject = response.body();
                                Boolean status = userJsonObject.get("success").getAsBoolean();
                                if(status){

                                    Toast.makeText(activity, "Se cambió correctamente la contraseña, vuelva ha iniciar sesión", Toast.LENGTH_LONG).show();
                                    finish();

                                } else {
                                    Toast.makeText(activity, "No se pudo cambiar contraseña", Toast.LENGTH_LONG).show();
                                }

                                dialog.dismiss();

                            }else {
                                try {
                                    Log.d(LOG_TAG,"Erroral actualizar contraseña: " + response.errorBody().string());
                                    // processInteractor.showErrorServer("Error: intetelo nuevamente");
                                    Toast.makeText(activity, "No se pudo cambiar la contraseña", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(activity, "Server error, no se pudo obtener información", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                            Log.d(LOG_TAG, "Error SQL: " + e.getMessage());
                            Toast.makeText(activity, "Server error, no se pudo obtener información", Toast.LENGTH_SHORT).show();
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
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showProgressDialog(){
        int llPadding = 50;
        LinearLayout ll = new LinearLayout(activity);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setBackgroundColor(activity.getColor(R.color.red_500));

        // ll.setAlpha((float) 0.5);

        ll.setGravity(Gravity.CENTER);

        ProgressBar progressBar = new ProgressBar(activity);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);

        TextView tvText = new TextView(activity);
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