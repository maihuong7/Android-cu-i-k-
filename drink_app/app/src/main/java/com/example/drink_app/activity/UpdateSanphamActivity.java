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
import com.example.drink_app.model.Sanpham;
import com.example.drink_app.ultil.Server;

import java.util.HashMap;
import java.util.Map;

public class UpdateSanphamActivity extends AppCompatActivity {
    EditText edttenSp, edtgiaSp, edtanhSp, edtmotaSp, edtIDSp;
    Button btnupdate, btnhuy;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sanpham);

        Intent intent = getIntent();
        Sanpham sanpham = (Sanpham) intent.getSerializableExtra("dataSanpham");
        Toast.makeText(this, sanpham.getTensanpham(), Toast.LENGTH_SHORT).show();
        Anhxa();

        id = sanpham.getID();
        edttenSp.setText(sanpham.getTensanpham());
        edtgiaSp.setText(sanpham.getGiasanpham()+"");
        edtanhSp.setText(sanpham.getHinhanhsanpham());
        edtmotaSp.setText(sanpham.getMotasanpham());
        edtIDSp.setText(sanpham.getIDSanpham()+"");

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tensp = edttenSp.getText().toString().trim();
                String giasp = edtgiaSp.getText().toString().trim();
                String anhsp = edtanhSp.getText().toString().trim();
                String motasp = edtmotaSp.getText().toString().trim();
                String idsp = edtIDSp.getText().toString().trim();
                if (tensp.matches("") || giasp.matches("") || anhsp.matches("") || motasp.matches("") || idsp.matches("")){
                    Toast.makeText(UpdateSanphamActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    CapNhatSanPham();
                }
            }
        });

    }
    private void CapNhatSanPham(){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String duongdan= Server.Duongdanupdate;
        Log.i("check",duongdan);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Success")){
                    Toast.makeText(UpdateSanphamActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateSanphamActivity.this, Quanlisp.class));
                }else {
                    Toast.makeText(UpdateSanphamActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateSanphamActivity.this, "Xảy ra lỗi. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idSp",String.valueOf(id));
                params.put("tenSp", edttenSp.getText().toString().trim());
                params.put("giaSp", edtgiaSp.getText().toString().trim());
                params.put("hinhanhSp", edtanhSp.getText().toString().trim());
                params.put("motaSp", edtanhSp.getText().toString().trim());
                params.put("idsanphamSp", edtIDSp.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void Anhxa() {
        btnupdate = (Button) findViewById(R.id.buttonxacnhanupdate);
        btnhuy = (Button) findViewById(R.id.buttontroveupdate);
        edttenSp = (EditText) findViewById(R.id.edittextupdatetensp);
        edtgiaSp = (EditText) findViewById(R.id.edittextupdategiasp);
        edtanhSp = (EditText) findViewById(R.id.edittextupdateanhsp);
        edtmotaSp = (EditText) findViewById(R.id.edittextupdatemotasp);
        edtIDSp = (EditText) findViewById(R.id.edittextupdateidsp);
    }
}