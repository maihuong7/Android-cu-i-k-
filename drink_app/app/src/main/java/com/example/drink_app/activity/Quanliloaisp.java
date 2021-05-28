package com.example.drink_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.drink_app.R;
import com.example.drink_app.adapter.LoaispAdapter;
import com.example.drink_app.adapter.QuanLiLoaiSpAdapter;
import com.example.drink_app.adapter.QuanlispAdapter;
import com.example.drink_app.model.Loaisp;
import com.example.drink_app.model.Sanpham;
import com.example.drink_app.ultil.CheckConnection;
import com.example.drink_app.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Quanliloaisp extends AppCompatActivity {
    Toolbar toolbarloaisp;
    ListView listViewloaisanpham;
    Button buttonthemloaisanpham;
    ArrayList<Loaisp> mangloaisp;
    QuanLiLoaiSpAdapter loaispAdapter;
    int idsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanliloaisp);
        Anhxa();
        ActionBar();
        GetData();
        EvenButton();
    }

    public void DeleteLoaiSanpham(final int idsp){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String duongdan= Server.Duongdandeleteloaisp;
        Log.i("check",duongdan);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(Quanliloaisp.this, "Xóa thàng công", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Quanliloaisp.this, "Lỗi xóa!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Quanliloaisp.this, "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idcualoaisanpham",String.valueOf(idsp));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void EvenButton() {
        buttonthemloaisanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Thongtinthemloaisp.class);
                startActivity(intent);
            }
        });
    }

    private void GetData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Duongdanloaisp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int id = 0;
                String tenloaisp = "";
                String hinhanhloaisp = "";
                if (response != null){
                    for (int i = 0 ; i < response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenloaisp = jsonObject.getString("tenloaisp");
                            hinhanhloaisp = jsonObject.getString("hinhanhloaisp");
                            mangloaisp.add(new Loaisp(id,tenloaisp,hinhanhloaisp));
                            loaispAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToast_Short(getApplicationContext(),error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionBar() {
        setSupportActionBar(toolbarloaisp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarloaisp.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Anhxa() {
        toolbarloaisp = (Toolbar) findViewById(R.id.toolbarquanliloaisp);
        listViewloaisanpham = (ListView) findViewById(R.id.liteviewloaisp);
        buttonthemloaisanpham = (Button) findViewById(R.id.buttonthemloaisp);
        mangloaisp = new ArrayList<>();
        loaispAdapter = new QuanLiLoaiSpAdapter(mangloaisp, this, R.layout.dong_loai_sanpham);
        listViewloaisanpham.setAdapter(loaispAdapter);

    }
}