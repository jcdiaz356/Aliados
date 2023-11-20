package com.dataservicios.aliados.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dataservicios.aliados.R;
import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.Client;
import com.dataservicios.aliados.repo.LoginRepoImple;
import com.dataservicios.aliados.util.ShowProcessDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ornach.nobobutton.NoboButton;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private Activity activity = this;
    private NoboButton btn_signup;
    // private Dialog dialog;
    private TextInputEditText txt_usercode, txt_password;
    private TextInputLayout txt_input_usercode, txt_input_passsword;
    private CheckBox chk_session;
    private ShowProcessDialog showProcessDialog;
    private DatabaseHelper helper;
    private int client_id;
    private LoginRepoImple loginRepoImple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DatabaseManager.init(activity);
        helper = DatabaseManager.getInstance().getHelper();

        loginRepoImple          = new LoginRepoImple(this);

        showProcessDialog = new ShowProcessDialog(activity);

        btn_signup = (NoboButton) findViewById(R.id.btn_signup);

        txt_input_usercode = (TextInputLayout) findViewById(R.id.txt_input_usercode);
        txt_input_passsword = (TextInputLayout) findViewById(R.id.txt_input_passsword);
        chk_session = (CheckBox) findViewById(R.id.chk_session);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = txt_input_usercode.getEditText().getText().toString().trim();
                String password = txt_input_passsword.getEditText().getText().toString().trim();
                boolean session ;
               // code = "1000019353";
                txt_input_usercode.getEditText().setText(code);
                if(chk_session.isChecked()) session = true; else  session = false;
                loginRepoImple.signIn(code ,password,session,activity);
            }
        });
    }

    @Override
    public void loginSuccess(Client client) {
        Intent intent = new Intent(activity, PanelAdminActivity.class);
        intent.putExtra("client_id", client.getId());
        startActivity(intent);
        finish();
    }

    @Override
    public void loginError(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void serverError(Throwable error) {
        Toast.makeText(activity, error.getMessage().toString(), Toast.LENGTH_LONG).show();
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