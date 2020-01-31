package com.example.skm_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PilihSurveiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_survei);
    }

    public void LpseBtn(View view) {
        Intent int1 = new Intent (this, IsiRespondenActivity.class);
        int1.putExtra("msg", "1");
        startActivity(int1);
    }

    public void PpidBtn(View view) {
        Intent int2 = new Intent (this, IsiRespondenActivity.class);
        int2.putExtra("msg", "2");
        startActivity(int2);
    }

    public void SandiBtn(View view) {
        Intent int3 = new Intent (this, IsiRespondenActivity.class);
        int3.putExtra("msg","3");
        startActivity(int3);
    }

}
