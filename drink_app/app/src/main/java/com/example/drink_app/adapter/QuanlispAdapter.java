package com.example.drink_app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.drink_app.R;
import com.example.drink_app.activity.Quanlisp;
import com.example.drink_app.activity.UpdateSanphamActivity;
import com.example.drink_app.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class QuanlispAdapter extends BaseAdapter {

    private Quanlisp context;
    private ArrayList<Sanpham> arraysp;

    public QuanlispAdapter(Quanlisp context, int dong_tatcasp, ArrayList<Sanpham> arraysp) {
        this.context = context;
        this.arraysp = arraysp;
    }

    @Override
    public int getCount() {
        return arraysp.size();
    }

    @Override
    public Object getItem(int i) {
        return arraysp.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder{
        public TextView txttensp, txtgiasp, txtmotasp;
        public ImageView imgviewsp,imgviewdelete, imgviewupdate;
    }
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_tatcasp, null);
            viewHolder.txttensp = (TextView) view.findViewById(R.id.textviewsp);
            viewHolder.txtgiasp = (TextView) view.findViewById(R.id.textviewgiasp);
            viewHolder.txtmotasp = (TextView) view.findViewById(R.id.textviewmotasp);
            viewHolder.imgviewsp = (ImageView) view.findViewById(R.id.imagetensp);
            viewHolder.imgviewdelete = (ImageView) view.findViewById(R.id.imageviewdelete);
            viewHolder.imgviewupdate = (ImageView) view.findViewById(R.id.imageviewupdate);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();

        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txttensp.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiasp.setText("Giá : " + decimalFormat.format(sanpham.getGiasanpham()) + "VND");
        viewHolder.txtmotasp.setMaxLines(3);
        viewHolder.txtmotasp.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotasp.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(viewHolder.imgviewsp);

        viewHolder.imgviewupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateSanphamActivity.class);
                intent.putExtra("dataSanpham", sanpham);
                context.startActivity(intent);
            }
        });

        viewHolder.imgviewdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Xacnhanxoa(sanpham.getTensanpham(), sanpham.getID());
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
                context.DeleteSanpham(id);
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
