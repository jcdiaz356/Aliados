package com.dataservicios.aliados;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.fragments.ConcourseFragment;
import com.dataservicios.aliados.fragments.PromotionsFragment;
import com.dataservicios.aliados.fragments.StatusAccountFragment;
import com.dataservicios.aliados.fragments.WelcomeFragment;
import com.dataservicios.aliados.model.Client;

import java.sql.SQLException;
import java.util.List;

public class PanelAdminActivity extends AppCompatActivity {
    private static final String LOG_TAG = PanelAdminActivity.class.getSimpleName();

    private Activity activity = this;
    private BottomNavigationView nav_view;
    private Fragment fragment;
    private Toolbar toolbar;
    private Client user;
    private DatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paneladmin);
        showToolbar("",true);

        Bundle bundle = getIntent().getExtras();
        int user_id   = bundle.getInt("user_id");

        DatabaseManager.init(activity);
        helper = DatabaseManager.getInstance().getHelper();

        try {
            user =  helper.getClientDao().queryForId(user_id);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(activity, "No se pudo encontrar el usuario", Toast.LENGTH_LONG).show();
            return;
        }
        fragment = new WelcomeFragment(user);
        //getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();

        nav_view = (BottomNavigationView) findViewById(R.id.nav_view);

        nav_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.page_home:

                        fragment = new WelcomeFragment(user);
                        //getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();

                        break;
                    case R.id.page_status_account:
                        fragment = new StatusAccountFragment(user);
                        //getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                    case R.id.page_concourse:
                        fragment = new ConcourseFragment(user);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                    case R.id.page_promotions:
                        fragment = new PromotionsFragment(user);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                    default:
                        throw new IllegalArgumentException("item not implemented : " + item.getItemId());
                }
                return true;
            }
        });







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()) {

            case R.id.navigation_close:

                List<Client> items = null;
                try {
                    items = helper.getClientDao().queryForAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Log.d(LOG_TAG, "Obteniedo Todos los clientes para limpiar");
                for (Client object : items) {
                    try {
                        helper.getClientDao().deleteById(object.getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.d(LOG_TAG, "Eliminando User:" + object.toString());
                }


                i = new Intent(activity, LoginActivity.class);
                i.putExtra("session_create"              , false);
                startActivity(i);
                finish();
                return true;

            case R.id.navigation_change_password:



                i = new Intent(activity, ChangePasswordActivity.class);
                i.putExtra("code"              , user.getCode());
                startActivity(i);
               // finish();

        }

        return super.onOptionsItemSelected(item);
    }

    private void showToolbar(String title, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }





}