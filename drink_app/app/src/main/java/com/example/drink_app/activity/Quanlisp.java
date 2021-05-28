package com.example.drink_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.drink_app.R;
import com.example.drink_app.adapter.CafeAdapter;
import com.example.drink_app.adapter.QuanlispAdapter;
import com.example.drink_app.adapter.TraSuaAdapter;
import com.example.drink_app.model.Sanpham;
import com.example.drink_app.ultil.CheckConnection;
import com.example.drink_app.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Quanlisp extends AppCompatActivity {
    Toolbar toolbarsp;
    ListView lvsp;
    Button buttonthem;
    Button buttonchuyentrang;
    QuanlispAdapter quanlispAdapter;
    ArrayList<Sanpham> mangsp;
    int idsp;
    int page=1;
    View footerview;
    boolean isLoading;
    boolean limitdata;
    mHandler mHandler;

//    String urlDelete = "http://192.168.0.2/server/delete.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanlisp);


        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            Anhxa();
            GetIdloaisp();
            ActionBar();
            GetData(page);
            LoadMoreDta();
            EvenButton();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại INTERNET");
            finish();
        }

    }

    private void EvenButton() {
        buttonthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Thongtinthemsanpham.class);
                startActivity(intent);
            }
        });
        buttonchuyentrang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Quanliloaisp.class);
                startActivity(intent);
            }
        });
    }
    public void DeleteSanpham(final int idsp){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String duongdan= Server.Duongdandelete;
        Log.i("check",duongdan);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(Quanlisp.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Quanlisp.this, "Lỗi xóa!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Quanlisp.this, "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idcuasanpham",String.valueOf(idsp));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void LoadMoreDta() {
        lvsp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSanPham.class);//chuyenmanghinhsangtrangchitiet
                intent.putExtra("thongtinsanpham", mangsp.get(i));
                startActivity(intent);
            }
        });
        lvsp.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int FirstItem, int VisibleItem , int TotalItem) {
                if (FirstItem +VisibleItem==TotalItem && TotalItem!=0 && isLoading == false && limitdata == false){
                    isLoading =true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan= Server.Duongdantrasua+String.valueOf(Page);
        Log.i("check",duongdan);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Tensp ="";
                int Giasp =0;
                String Hinhanhsp ="";
                String Motasp="";
                int Idspsp=0;
                if (response !=null && response.length() !=2){
                    //hienthingoacvuongnenchosaucung2phantudebiethetdulieu
//                    Log.i("check","respone ok");
                    lvsp.removeFooterView(footerview);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tensp = jsonObject.getString("tensp");
                            Giasp = jsonObject.getInt("giasp");
                            Hinhanhsp = jsonObject.getString("hinhanhsp");
                            Motasp = jsonObject.getString("motasp");
                            Idspsp = jsonObject.getInt("idsanpham");
                            mangsp.add(new Sanpham(id, Tensp, Giasp, Hinhanhsp,Motasp, Idspsp));
                            Log.e("mangsp",mangsp.toString());
                            quanlispAdapter.notifyDataSetChanged();
                            Log.i("check","size: "+mangsp.size());
                        }
                    } catch (JSONException e) {
//                        Log.e("json error",e.getMessage());
                        e.printStackTrace();
                    }
                }else {
                    limitdata = true;
                    lvsp.removeFooterView(footerview);
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Đã hết dữ liệu");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            //day du lieu len
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id",String.valueOf(idsp));
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionBar() {
        setSupportActionBar(toolbarsp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarsp.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void GetIdloaisp() {
        idsp = getIntent().getIntExtra("idsanpham", -1);
    }

    private void Anhxa() {
        toolbarsp = (Toolbar) findViewById(R.id.toolbarquanlisp);
        lvsp = (ListView) findViewById(R.id.listviewsp);
        buttonthem = (Button) findViewById(R.id.buttonadd);
        buttonchuyentrang = (Button) findViewById(R.id.buttonquatrang);
        mangsp = new ArrayList<>();
        quanlispAdapter = new QuanlispAdapter(this, R.layout.dong_tatcasp, mangsp);
        lvsp.setAdapter(quanlispAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null );
        mHandler = new mHandler();
    }
    public class  mHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvsp.addFooterView(footerview);
                    break;
                case  1:
                    GetData(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }
    public class ThreadData extends  Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }


}