package com.example.skm_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class IsiSurveiActivity extends AppCompatActivity implements KeyEvent.Callback {
    private LinearLayout ll;
    private ArrayList<RadioGroup> rgList = new ArrayList<RadioGroup>();
    private RadioGroup rg;
    private ArrayList<RadioButton> rbList = new ArrayList<RadioButton>();
    private RadioButton rb;
    private Button pencet;
    private Integer[] intArray = new Integer[50];
    private Integer skor;
    private TextView tv;
    private String id_layanan;
    private String id_responden;
    private EditText kritik;
    private Dialog myDialog;
    public String id_isi_submit;

    String url_storeJawab = "http://skm.diskominfotik.blitarkota.go.id/apk/android/storeJawab.php";
    String url_storeItemJawab = "http://skm.diskominfotik.blitarkota.go.id/apk/android/storeItemJawab.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_survei);

        ll = findViewById(R.id.vo);
        myDialog = new Dialog(this);

        id_layanan = getIntent().getStringExtra("id_layanan");
        id_responden = getIntent().getStringExtra("id_responden");
        getData(parseInt(id_responden),parseInt(id_layanan));
    }

    @Override
    public void onBackPressed(){
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(this, PilihSurveiActivity.class);
        startActivity(setIntent);
        finish();
    }

    public void getData(final Integer id_resp, final Integer id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://skm.diskominfotik.blitarkota.go.id/apk/android/getKuesioner.php/?id=" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        ArrayList<String> list = new ArrayList<String>();
                        final ArrayList<String> list_id = new ArrayList<String>();
                        final ArrayList<String> id_kues = new ArrayList<String>();
                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        skor = 0;

                        JSONArray mJsonArray = new JSONArray(response);
                        if(mJsonArray.length()==0){
                            tv = new TextView(IsiSurveiActivity.this);
                            tv.setPadding(10,50,10,0);
                            tv.setText("Mohon maaf, survei dengan layanan ini sedang tidak tersedia");
                            tv.setTextSize(16);
                            tv.setTextColor(000000);
                            ll.addView(tv, p);

                            pencet = new Button(IsiSurveiActivity.this);
                            pencet.setText("Kembali");
                            ll.addView(pencet, p);

                            pencet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(IsiSurveiActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                        else{
                            for(int i=0;i<mJsonArray.length();i++){
                                JSONObject mJsonObject = mJsonArray.getJSONObject(i);

                                String konten = mJsonObject.getString("konten_pertanyaan");
                                String id_tanya = mJsonObject.getString("id_pertanyaan");
                                String id_kuesioner = mJsonObject.getString("id_kuesioner");
                                list.add(konten);
                                list_id.add(id_tanya);
                                id_kues.add(id_kuesioner);

                                tv = new TextView(IsiSurveiActivity.this);
                                tv.setPadding(10,0,10,0);
                                tv.setText((i+1) + ". " + list.get(i));
                                tv.setTextSize(16);
                                ll.addView(tv, p);

                                rg = new RadioGroup(IsiSurveiActivity.this);
                                rgList.add(rg);
                                ll.addView(rgList.get(i), p);

                                String[] ab = {"Tidak Setuju", "Kurang Setuju", "Setuju", "Sangat Setuju"};
                                for (int j = 0; j < ab.length; j++) {
                                    rb = new RadioButton(IsiSurveiActivity.this);
                                    rb.setText(ab[j]);
                                    rb.setId(j+1);
                                    rbList.add(rb);
                                    rgList.get(i).addView(rbList.get(i*4+j), p);
                                }
                                addListenerRadio(i);
                            }

                            tv = new TextView(IsiSurveiActivity.this);
                            tv.setPadding(10,0,10,0);
                            tv.setText("Kritik dan Saran");
                            tv.setTextSize(18);
                            ll.addView(tv, p);

                            kritik = new EditText(IsiSurveiActivity.this);
                            kritik.setPadding(10,0,0,10);
                            kritik.setEms(30);
                            ll.addView(kritik, p);

                            pencet = new Button(IsiSurveiActivity.this);
                            pencet.setText("Kirim");
                            ll.addView(pencet, p);

                            pencet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int count = 0;
                                    for(int i=0;i<intArray.length;i++){
                                        if(intArray[i]==null){
                                            break;
                                        }
                                        skor = skor + intArray[i];
                                        count = count +1;
                                    }
                                    int percentage = skor * 100 /(count * 4) ;
                                    try {
                                        storeJawab(id_resp, parseInt(id_kues.get(0)), percentage, kritik.getText().toString(), list_id, intArray);

                                        myDialog.setContentView(R.layout.popup_thankyou);
                                        ImageButton closeButton = myDialog.findViewById(R.id.ib_close);

                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                myDialog.dismiss();
                                                Intent intent = new Intent(IsiSurveiActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                        myDialog.show();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error Response",error.toString());
                }
            }
        );

        queue.add(stringRequest);
    }

    public void addListenerRadio(final int j){
        rgList.get(j).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = rgList.get(j).getCheckedRadioButtonId();
                intArray[j] = selectedId;
            }
        });
    }

    private void storeItemJawab(String id_isi_submit, ArrayList<String> list_id, Integer[] intArray) {
        Integer id = parseInt(id_isi_submit);
        for(int k = 0; k < intArray.length; k++) {
            if(intArray[k]==null){
                break;
            }
            String url = url_storeItemJawab + "?id_isi_submit=" + id + "&id_pertanyaan=" + parseInt(list_id.get(k)) + "&nilai=" + intArray[k];
            Log.e("URL", url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(IsiSurveiActivity.this, "err" + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(IsiSurveiActivity.this);
            requestQueue.add(stringRequest);
        }
    }

    private void storeJawab(final int id_resp, final int id, final int skor, final String kritik, final ArrayList<String> list_id, final Integer[] intArray) throws UnsupportedEncodingException {

        String url = url_storeJawab+ "?id_responden="+id_resp+"&id_kuesioner="+id+"&skor="+skor+"&kritik="+kritik;
        Log.e("URL",url);
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                id_isi_submit = response.replaceAll("\\D", "");
                storeItemJawab(id_isi_submit, list_id, intArray);
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(IsiSurveiActivity.this,"err"+error.toString(),Toast.LENGTH_LONG).show();
                }
            }
        );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue=Volley.newRequestQueue(IsiSurveiActivity.this);
        requestQueue.add(stringRequest);
    }
}
