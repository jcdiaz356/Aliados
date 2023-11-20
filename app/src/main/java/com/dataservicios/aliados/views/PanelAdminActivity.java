package com.dataservicios.aliados.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dataservicios.aliados.R;
import com.dataservicios.aliados.repo.ClientRepo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.views.fragments.ConcourseFragment;
import com.dataservicios.aliados.views.fragments.StatusAccountFragment;
import com.dataservicios.aliados.views.fragments.WelcomeFragment;
import com.dataservicios.aliados.model.Client;

public class PanelAdminActivity extends AppCompatActivity {
    private static final String LOG_TAG = PanelAdminActivity.class.getSimpleName();

    private Activity activity = this;
    private BottomNavigationView nav_view;
    private Fragment fragment;
    private Toolbar toolbar;
    private Client client;

    private ClientRepo clientRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paneladmin);
        showToolbar("",true);

        Bundle bundle = getIntent().getExtras();
        int client_id   = bundle.getInt("client_id");

        DatabaseManager.init(activity);
        clientRepo = new ClientRepo(activity);
        client = (Client) clientRepo.findById(client_id);

        fragment = new WelcomeFragment(client);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();

        nav_view = (BottomNavigationView) findViewById(R.id.nav_view);

        nav_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.page_home:

                        fragment = new WelcomeFragment(client);
                        //getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();

                        break;
                    case R.id.page_status_account:
                        fragment = new StatusAccountFragment(client);
                        //getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                    case R.id.page_concourse:
                        fragment = new ConcourseFragment(client);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
//                    case R.id.page_promotions:
//                        fragment = new PromotionsFragment(client);
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
//                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                                .addToBackStack(null).commit();
//                        break;
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

               clientRepo.deleteAll();

                i = new Intent(activity, LoginActivity.class);
                i.putExtra("session_create"              , false);
                startActivity(i);
                finish();
                return true;

            case R.id.navigation_change_password:

                i = new Intent(activity, ChangePasswordActivity.class);
                i.putExtra("code"              , client.getCode());
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