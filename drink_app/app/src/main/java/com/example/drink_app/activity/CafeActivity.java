package com.example.drink_app.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.drink_app.R;
import com.example.drink_app.adapter.CafeAdapter;
import com.example.drink_app.adapter.TraSuaAdapter;
import com.example.drink_app.model.Sanpham;
import com.example.drink_app.ultil.CheckConnection;
import com.example.drink_app.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CafeActivity  extends AppCompatActivity {
    Toolbar toolbarcf;
    ListView lvcf;
    CafeAdapter cafeAdapter;
    ArrayList<Sanpham> mangcf;
    int idcf;
    int page=1;
    View footerview;
    boolean isLoading;
    boolean limitdata;
    mHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe);
        Anhxa();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            GetIdloaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreDta();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
            finish();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), com.example.drink_app.activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadMoreDta() {
        lvcf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSanPham.class);//chuyenmanghinhsangtrangchitiet
                intent.putExtra("thongtinsanpham", mangcf.get(i));
                startActivity(intent);
            }
        });
        lvcf.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        String duongdan= Server.Duongdancafe+String.valueOf(Page);
        Log.i("check",duongdan);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Tencf ="";
                int Giacf =0;
                String Hinhanhcf ="";
                String Motacf="";
                int Idspcf=0;
                if (response !=null && response.length() !=2){
                    //hienthingoacvuongnenchosaucung2phantudebiethetdulieu
//                    Log.i("check","respone ok");
                    lvcf.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tencf = jsonObject.getString("tensp");
                            Giacf = jsonObject.getInt("giasp");
                            Hinhanhcf = jsonObject.getString("hinhanhsp");
                            Motacf = jsonObject.getString("motasp");
                            Idspcf = jsonObject.getInt("idsanpham");
                            mangcf.add(new Sanpham(id, Tencf, Giacf, Hinhanhcf,Motacf, Idspcf));
                            Log.e("mangdt",mangcf.toString());
                            cafeAdapter.notifyDataSetChanged();
                            Log.i("check","size: "+mangcf.size());
                        }
                    } catch (JSONException e) {
                        Log.e("json error",e.getMessage());
                        e.printStackTrace();
                    }
                }
                else {
                    Log.i("check","respone null");
                    limitdata = true;
                    lvcf.removeFooterView(footerview);
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
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("idsanpham",String.valueOf(idcf));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarcf);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarcf.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
//
    private void GetIdloaisp() {
        idcf = getIntent().getIntExtra("idloaisanpham", -1);
//        Log.d("giatriloaisanpham",idcf+"");
    }

    private void Anhxa() {
        toolbarcf = (Toolbar) findViewById(R.id.toolbarcafe);
        lvcf = (ListView) findViewById(R.id.listviewcafe);
        mangcf = new ArrayList<>();
        cafeAdapter = new CafeAdapter(getApplicationContext(), mangcf);
        lvcf.setAdapter(cafeAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null );
        mHandler = new mHandler();
    }
    public class  mHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvcf.addFooterView(footerview);
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