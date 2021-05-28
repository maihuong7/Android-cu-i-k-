package com.example.drink_app.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
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
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.drink_app.R;
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

public class TraSuaActivity extends AppCompatActivity {
    Toolbar toolbardt;
    ListView lvdt;
    TraSuaAdapter traSuaAdapter;
    ArrayList<Sanpham> mangdt;
    int iddt;
    int page=1;
    View footerview;
    boolean isLoading;
    boolean limitdata;
    mHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_sua);
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
//        MenuItem searchItem = menu.findItem(R.id.menusearch);
//        SearchView searchView = (SearchView) menu.findItem(R.id.menusearch).getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(TraSuaActivity.this, query, Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                traSuaAdapter.filter(newText.trim());
//                Log.d("AAA",newText);
//                return false;
//            }
//        });
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.menugiohang:
//                Intent intent = new Intent(getApplicationContext(), com.example.drink_app.activity.Giohang.class);
//                startActivity(intent);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void LoadMoreDta() {
        lvdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSanPham.class);//chuyenmanghinhsangtrangchitiet
                intent.putExtra("thongtinsanpham", mangdt.get(i));
                startActivity(intent);
            }
        });
        lvdt.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                String Tents ="";
                int Giats =0;
                String Hinhanhts ="";
                String Motats="";
                int Idspts=0;
                if (response !=null && response.length() !=2){
                    //hienthingoacvuongnenchosaucung2phantudebiethetdulieu
                    Log.i("check","respone ok");
                    lvdt.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tents = jsonObject.getString("tensp");
                            Giats = jsonObject.getInt("giasp");
                            Hinhanhts = jsonObject.getString("hinhanhsp");
                            Motats = jsonObject.getString("motasp");
                            Idspts = jsonObject.getInt("idsanpham");
                            mangdt.add(new Sanpham(id, Tents, Giats, Hinhanhts,Motats, Idspts));
                            Log.e("mangdt",mangdt.toString());
                            traSuaAdapter.notifyDataSetChanged();
                            Log.i("check","size: "+mangdt.size());
                        }
                        traSuaAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e("json error",e.getMessage());
                        e.printStackTrace();
                    }
                }else {
                    Log.i("check","respone null");
                    limitdata = true;
                    lvdt.removeFooterView(footerview);
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
                param.put("idsanpham",String.valueOf(iddt));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbardt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbardt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void GetIdloaisp() {
        iddt = getIntent().getIntExtra("idloaisanpham", -1);
        Log.d("giatriloaisanpham",iddt+"");
    }

    private void Anhxa() {
        toolbardt = (Toolbar) findViewById(R.id.toolbartrasua);
        lvdt = (ListView) findViewById(R.id.listviewtrasua);
        mangdt = new ArrayList<>();
        traSuaAdapter = new TraSuaAdapter(getApplicationContext(), mangdt);
        lvdt.setAdapter(traSuaAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null );
        mHandler = new mHandler();
    }
    public class  mHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvdt.addFooterView(footerview);
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