package com.example.drink_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.drink_app.R;
import com.example.drink_app.model.Loaisp;
import com.example.drink_app.ultil.Server;

import java.util.HashMap;
import java.util.Map;

public class UpdateLoaiSpActivity extends AppCompatActivity {
    EditText edttenloaiSp, edtanhloaiSp;
    Button btnupdateloai, btnhuyloai;
    int id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_loai_sp);

        Intent intent = getIntent();
        Loaisp loaisp = (Loaisp) intent.getSerializableExtra("dataLoaiSp");
        Toast.makeText(this, loaisp.getTenloaisp(), Toast.LENGTH_SHORT).show();

        Anhxa();

        id = loaisp.getId();
        edttenloaiSp.setText(loaisp.getTenloaisp());
        edtanhloaiSp.setText(loaisp.getHinhanhloaisp());

        btnhuyloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnupdateloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenloaisp = edttenloaiSp.getText().toString().trim();
                String anhloaisp = edtanhloaiSp.getText().toString().trim();
                if (tenloaisp.matches("") || anhloaisp.matches("") ){
                    Toast.makeText(UpdateLoaiSpActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                }else {
                    CapNhatLoaiSanPham();
                }
            }
        });
    }

    private void CapNhatLoaiSanPham(){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String duongdan= Server.Duongdanupdateloaisp;
        Log.i("check",duongdan);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Success")){
                    Toast.makeText(UpdateLoaiSpActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateLoaiSpActivity.this, Quanliloaisp.class));
                }else {
                    Toast.makeText(UpdateLoaiSpActivity.this, "Lỗi cập nhật!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateLoaiSpActivity.this, "Xảy ra lỗi. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idloaiSp",String.valueOf(id));
                params.put("tenloaiSp", edttenloaiSp.getText().toString().trim());
                params.put("hinhanhloaiSp", edtanhloaiSp.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void Anhxa() {
        btnupdateloai = (Button) findViewById(R.id.buttonxacnhanupdateloai);
        btnhuyloai = (Button) findViewById(R.id.buttontroveupdateloai);
        edttenloaiSp = (EditText) findViewById(R.id.edittextupdatetenloaisp);
        edtanhloaiSp = (EditText) findViewById(R.id.edittextupdateanhloaisp);
    }
}