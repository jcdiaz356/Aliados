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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private Activity activity = this ;
    private NoboButton btn_signup;
    private Dialog dialog;
    private TextInputEditText txt_usercode,txt_password;
    private TextInputLayout txt_input_usercode,txt_input_passsword;
    private CheckBox chk_session;
    private DatabaseHelper helper;
    private int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        DatabaseManager.init(activity);
        helper = DatabaseManager.getInstance().getHelper();



        dialog  = new Dialog(activity);
        btn_signup = (NoboButton)  findViewById(R.id.btn_signup);

        txt_input_usercode = (TextInputLayout) findViewById(R.id.txt_input_usercode);
        txt_input_passsword = (TextInputLayout) findViewById(R.id.txt_input_passsword);
        chk_session = (CheckBox) findViewById(R.id.chk_session);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog  = new Dialog(activity);

                showProgressDialog();
                String code = txt_input_usercode.getEditText().getText().toString().trim();
                String password = txt_input_passsword.getEditText().getText().toString().trim();

//                code = "485706";
//                txt_input_usercode.getEditText().setText(code);

                Map<String, String> map = new HashMap<String, String>();
                 map.put("code", code);
                 map.put("password", password);

                RestApiAdapter restApiAdapter = new RestApiAdapter();
                Service service =  restApiAdapter.getClientService(activity);
                Call<JsonObject> call =  service.getUserLoginForCode(map);

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
                                    JsonObject user = userJsonObject.getAsJsonObject("user");
                                    // **********************************
                                    // Creando el objeto user
                                    // *********************************
                                    User newUser = new User();
                                    newUser.setId(user.get("code").getAsInt());
                                    newUser.setId_data(user.get("id").getAsString());
                                    newUser.setCode(user.get("code").getAsString());
                                    newUser.setName(user.get("name").getAsString());
                                    newUser.setFfvv(user.get("ffvv").getAsString());
                                    newUser.setDex(user.get("dex").getAsString());
                                    newUser.setDex_id(user.get("dex_id").getAsString());
                                    newUser.setZone_id(user.get("zone_id").getAsInt());
                                    if(chk_session.isChecked()) newUser.setSave_ssesion(1); else  newUser.setSave_ssesion(0);
                                    user_id = newUser.getId();

                                    // ********* Eliminando todo los usuarios si existe *********************
                                    List<User> items = null;
                                    items = helper.getUserDao().queryForAll();
                                    Log.d(LOG_TAG, "Obteniedo Todos los clientes para limpiar");
                                    for (User object : items) {
                                        helper.getUserDao().deleteById(object.getId());
                                        Log.d(LOG_TAG, "Eliminando User:" + object.toString());
                                    }
                                    helper.getUserDao().create(newUser);

                                    // **********************************
                                    // Creando el objeto mounth
                                    // *********************************
                                   // JsonArray months = userJsonObject.getAsJsonArray("months");
                                    JsonArray months = userJsonObject.getAsJsonArray("months");
                                    // ********* Eliminando todo los meses si existe *********************
                                    List<Month> itemsMonth = null;
                                    itemsMonth = helper.getMonthDao().queryForAll();
                                    Log.d(LOG_TAG, "Obteniedo Todos los clientes para limpiar");
                                    for (Month object : itemsMonth) {
                                        helper.getMonthDao().deleteById(object.getId());
                                        Log.d(LOG_TAG, "Eliminando Mes:" + object.toString());
                                    }


                                    for (int i = 0; i < months.size(); i++) {
                                        JsonObject month =  months.get(i).getAsJsonObject();
                                        Month newMont = new Month();
                                        newMont.setId(month.get("month_number").getAsInt());
                                        newMont.setMonth_number(month.get("month_number").getAsString());
                                        newMont.setMount(month.get("month").getAsString());
                                        newMont.setYear_number(month.get("year_number").getAsInt());
                                        helper.getMonthDao().create(newMont);
                                    }

                                    Intent intent = new Intent(activity, PanelAdminActivity.class);
                                    intent.putExtra("user_id"              , user_id);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(activity, "El código o la contraseña es incorrecta", Toast.LENGTH_LONG).show();
                                }

                                dialog.dismiss();

                            }else {
                                try {
                                    Log.d(LOG_TAG,"Error credenciales: " + response.errorBody().string());
                                    // processInteractor.showErrorServer("Error: intetelo nuevamente");
                                    Toast.makeText(activity, "No se pudo obtener información", Toast.LENGTH_SHORT).show();
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