package com.example.drink_app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drink_app.R;
import com.example.drink_app.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CafeAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arraycafe;

    public CafeAdapter(Context context, ArrayList<Sanpham> arraycafe) {
        this.context = context;
        this.arraycafe = arraycafe;
    }

    @Override
    public int getCount() {
        return arraycafe.size();
    }

    @Override
    public Object getItem(int i) {
        return arraycafe.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder{
        public TextView txttencafe, txtgiacafe, txtmotacafe;
        public ImageView imgviewcafe;
    }
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        CafeAdapter.ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_cafe, null);
            viewHolder.txttencafe = (TextView) view.findViewById(R.id.textviewcafe);
            viewHolder.txtgiacafe = (TextView) view.findViewById(R.id.textviewgiacafe);
            viewHolder.txtmotacafe = (TextView) view.findViewById(R.id.textviewmotacafe);
            viewHolder.imgviewcafe = (ImageView) view.findViewById(R.id.imagetencafe);
            view.setTag(viewHolder);
        }else {
            viewHolder = (CafeAdapter.ViewHolder) view.getTag();

        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txttencafe.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiacafe.setText("Giá : " + decimalFormat.format(sanpham.getGiasanpham()) + "VND");
        viewHolder.txtmotacafe.setMaxLines(3);
        viewHolder.txtmotacafe.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotacafe.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(viewHolder.imgviewcafe);
        return view;
    }
}
