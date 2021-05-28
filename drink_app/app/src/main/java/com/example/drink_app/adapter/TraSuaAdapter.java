package com.example.drink_app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.Locale;

public class TraSuaAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arraytrasua;
//    ArrayList<Sanpham> arrayList;

    public TraSuaAdapter(Context context, ArrayList<Sanpham> arraytrasua) {
        this.context = context;
        this.arraytrasua = arraytrasua;
//        this.arrayList = new ArrayList<Sanpham>();
//        this.arrayList = new ArrayList<>();
//        this.arrayList.addAll(arraytrasua);
    }

    @Override
    public int getCount() {
        return arraytrasua.size();
    }

    @Override
    public Object getItem(int i) {
        return arraytrasua.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder{
        public TextView txttentrasua, txtgiatrasua, txtmotatrasua;
        public ImageView imgviewtrasua;
    }
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_trasua, null);
            viewHolder.txttentrasua = (TextView) view.findViewById(R.id.textviewtrasua);
            viewHolder.txtgiatrasua = (TextView) view.findViewById(R.id.textviewgiatrasua);
            viewHolder.txtmotatrasua = (TextView) view.findViewById(R.id.textviewmotatrasua);
            viewHolder.imgviewtrasua = (ImageView) view.findViewById(R.id.imagetentrasua);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();

        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txttentrasua.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiatrasua.setText("Giá : " + decimalFormat.format(sanpham.getGiasanpham()) + "VND");
        viewHolder.txtmotatrasua.setMaxLines(3);
        viewHolder.txtmotatrasua.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotatrasua.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(viewHolder.imgviewtrasua);
        return view;
    }
//    public void filter(String charText){
//        charText = charText.toLowerCase(Locale.getDefault());
//        Log.e("TAG", "filter: "+charText.length() );
//        Log.e("TAG", "filter: "+arrayList.size() );
//        arraytrasua.removeAll(arraytrasua);
//        if (charText.length()==0){
//            arraytrasua.addAll(arrayList);
//        }else {
//            Log.e("TAG", "filter: else"+charText );
//            arraytrasua.removeAll(arraytrasua);
//            for (Sanpham ts : arrayList){
//                Log.e("TAG", "filter: "+ts.Tensanpham );
//                if (ts.Tensanpham.toLowerCase().contains(charText)){
//                    arraytrasua.add(ts);
//
//                }
//            }
//        }
//        this.notifyDataSetChanged();
////        this.notify();
//    }
}
