package com.example.drink_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.drink_app.R;
import com.example.drink_app.ultil.CheckConnection;
import com.example.drink_app.ultil.Server;

import java.util.HashMap;
import java.util.Map;

public class Thongtinthemloaisp extends AppCompatActivity {
    EditText edttenloaisanpham, edthinhanhloaisanpham;
    Button btnthemloai, btnhuyloai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtinthemloaisp);
        Anhxa();
        btnhuyloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            Evenbutton();
        }else{
            CheckConnection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
        }
    }

    private void Evenbutton() {
        btnthemloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hinhanhloai = edthinhanhloaisanpham.getText().toString().trim();
                String tenloai = edttenloaisanpham.getText().toString().trim();
                if (tenloai.length()>0  && hinhanhloai.length()>0 ){
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdanloaithemsanpham, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("maloaisanpham", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("tenloaisanpham",tenloai);
                            hashMap.put("hinhanhloaisanpham",hinhanhloai);
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                }else {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Hãy kiểm tra lại dữ liệu");
                }
            }
        });

    }

    private void Anhxa() {
        edttenloaisanpham = (EditText) findViewById(R.id.edittextthemtenloai);
        edthinhanhloaisanpham = (EditText) findViewById(R.id.edittextthemhinhanhloai);
        btnthemloai = (Button) findViewById(R.id.buttonxacnhanthemloai);
        btnhuyloai =(Button) findViewById(R.id.buttontrovethemloai);
    }
}