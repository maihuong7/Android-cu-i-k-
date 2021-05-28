package com.example.drink_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    Toolbar toolbarad;
    Button btn_login;
    EditText ed_email, ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        toolbarad = (Toolbar) findViewById(R.id.toolbardangnhap);
        btn_login = (Button) findViewById(R.id.buttonlogin);
        ed_email = (EditText) findViewById(R.id.email);
        ed_password = (EditText) findViewById(R.id.password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        ActionBar();
    }

    private void ActionBar() {
        setSupportActionBar(toolbarad);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarad.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public  void login(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.0.2/server/login.php"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")){
                    startActivity(new Intent(getApplicationContext(),Quanlisp.class));
                }else {
                    Toast.makeText(getApplicationContext(),"Sai email hoặc mật khẩu rồi:)", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", ed_email.getText().toString());
                params.put("password", ed_password.getText().toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }


}