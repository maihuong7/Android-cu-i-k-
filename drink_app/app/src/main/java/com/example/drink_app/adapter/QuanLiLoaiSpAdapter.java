package com.example.drink_app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.drink_app.R;
import com.example.drink_app.activity.Quanliloaisp;
import com.example.drink_app.activity.UpdateLoaiSpActivity;
import com.example.drink_app.activity.UpdateSanphamActivity;
import com.example.drink_app.model.Loaisp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QuanLiLoaiSpAdapter extends BaseAdapter {
    ArrayList<Loaisp> arraylistloaisp;
    Quanliloaisp context;

    public QuanLiLoaiSpAdapter(ArrayList<Loaisp> arraylistloaisp, Quanliloaisp context, int dong_loai_sanpham) {
        this.arraylistloaisp = arraylistloaisp;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arraylistloaisp.size();
    }

    @Override
    public Object getItem(int i) {
        return arraylistloaisp.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder{
        TextView txttenloaisp;
        ImageView imgloaisp, imgdeleteloai, imgupdateloai;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//get layout ra
            view = layoutInflater.inflate(R.layout.dong_loai_sanpham,null);
            viewHolder.txttenloaisp= (TextView) view.findViewById(R.id.textviewloaisp);
            viewHolder.imgloaisp= (ImageView) view.findViewById(R.id.imageloaisp);
            viewHolder.imgdeleteloai = (ImageView) view.findViewById(R.id.imageviewdeleteloaisp);
            viewHolder.imgupdateloai = (ImageView) view.findViewById(R.id.imageviewupdateloaisp);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Loaisp loaisp = (Loaisp) getItem(i);
        viewHolder.txttenloaisp.setText(loaisp.getTenloaisp());
        Picasso.with(context).load(loaisp.getHinhanhloaisp())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(viewHolder.imgloaisp);

        viewHolder.imgupdateloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateLoaiSpActivity.class);
                intent.putExtra("dataLoaiSp",loaisp);
                context.startActivity(intent);
            }
        });

        viewHolder.imgdeleteloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Xacnhanxoa(loaisp.getTenloaisp(), loaisp.getId());
            }
        });

        return view;
    }

    private void Xacnhanxoa(String ten, final int id){
        AlertDialog.Builder dialogxoa = new AlertDialog.Builder(context);
        dialogxoa.setMessage("Bạn có muốn xóa sản phẩm "+ten+" không?");
        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.DeleteLoaiSanpham(id);
            }
        });
        dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogxoa.show();
    }
}
