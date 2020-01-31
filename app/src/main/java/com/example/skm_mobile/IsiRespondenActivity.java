package com.example.skm_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class IsiRespondenActivity extends AppCompatActivity {
    EditText nama,instansi;
    RadioGroup jabatan,jenis_kelamin,pendidikan_terakhir;
    RadioButton jb,jk,pt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra("msg").equals("1")) {
            setContentView(R.layout.activity_responden_lpse);
        } else if (getIntent().getStringExtra("msg").equals("2") || getIntent().getStringExtra("msg").equals("3")) {
            setContentView(R.layout.activity_responden_sandi);
        }

        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    storeResponden();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void storeResponden() throws UnsupportedEncodingException {
        nama = findViewById(R.id.editText);
        jabatan = findViewById(R.id.Jabatan);
        jenis_kelamin = findViewById(R.id.JK);
        pendidikan_terakhir = findViewById(R.id.PT);

        jb = findViewById(jabatan.getCheckedRadioButtonId());
        jk = findViewById(jenis_kelamin.getCheckedRadioButtonId());
        pt = findViewById(pendidikan_terakhir.getCheckedRadioButtonId());

        String url_insert = "http://192.168.110.111/android/storeResponden.php";
        String name=  URLEncoder.encode(nama.getText().toString(),"UTF8");
        String grade = URLEncoder.encode(jb.getText().toString(),"UTF8");
        String gender = URLEncoder.encode(jk.getText().toString(),"UTF8");
        String education = URLEncoder.encode(pt.getText().toString(),"UTF8");
        String company;

        if(getIntent().getStringExtra("msg").equals("1")) {
            instansi = findViewById(R.id.editText2);
            company = URLEncoder.encode(instansi.getText().toString(),"UTF8");
            url_insert = url_insert + "?nama=" + name + "&asal=" + "" + "&pekerjaan=" + grade + "&instansi=" + company + "&jenis=" + gender + "&pendidikan=" + education;
            Log.e("URL", url_insert);
        }
        else if(getIntent().getStringExtra("msg").equals("2") || getIntent().getStringExtra("msg").equals("3")) {
            EditText alamat = findViewById(R.id.editText4);
            EditText pekerjaan = findViewById(R.id.editText2);
            instansi = findViewById(R.id.editText3);

            String address = URLEncoder.encode(alamat.getText().toString(),"UTF8");
            String job = URLEncoder.encode(pekerjaan.getText().toString(),"UTF8");
            company = URLEncoder.encode(instansi.getText().toString(),"UTF8");

            url_insert = url_insert + "?nama=" + name + "&asal=" + address + "&pekerjaan=" + job + "&instansi=" + company + "&jenis=" + gender + "&pendidikan=" + education;
            Log.e("URL", url_insert);
        }
        StringRequest stringRequest= new StringRequest(com.android.volley.Request.Method.GET, url_insert, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                String id_responden = response.replaceAll("\\D", "");
                do_next_activity(id_responden);
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(IsiRespondenActivity.this,"err"+error.toString(),Toast.LENGTH_LONG).show();
                }
            }
        );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        nama.setText("");
        jabatan.clearCheck();
        instansi.setText("");
        jenis_kelamin.clearCheck();
        pendidikan_terakhir.clearCheck();
    }

    public void do_next_activity(String id)
    {
        Intent intent = new Intent(IsiRespondenActivity.this, IsiSurveiActivity.class);
        intent.putExtra("id_layanan", getIntent().getStringExtra("msg"));
        intent.putExtra("id_responden", id);
        startActivity(intent);
    }
}
