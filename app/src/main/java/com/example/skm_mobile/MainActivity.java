package com.example.skm_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar();
        drawerLayout = findViewById(R.id.menuUtama);

        navigationView = findViewById(R.id.navigasi);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void panduanBtn(View view) {
        Intent intent = new Intent(this, PanduanActivity.class);
        startActivity(intent);
    }

    public void pilihBtn(View view) {
        Intent intent = new Intent(this, PilihSurveiActivity.class);
        startActivity(intent);
    }

    public void openMenuBar(View view){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        menuItem.setChecked(true);

        switch (id){
            case R.id.homeMenu :
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.panduan :
                Intent intent = new Intent(this, PanduanActivity.class);
                startActivity(intent);
                break;
            case R.id.kuesLPSE :
                Intent int1 = new Intent (this, IsiRespondenActivity.class);
                int1.putExtra("msg", "1");
                startActivity(int1);
                break;
            case R.id.kuesPPID :
                Intent int2 = new Intent (this, IsiRespondenActivity.class);
                int2.putExtra("msg", "2");
                startActivity(int2);
                break;
            case R.id.kuesSandi :
                Intent int3 = new Intent (this, IsiRespondenActivity.class);
                int3.putExtra("msg", "3");
                startActivity(int3);
                break;
        }
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        return false;
    }
}