package com.example.dtuparking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LS_Adapter extends RecyclerView.Adapter<LS_Adapter.lsViewHolder>{

    private Context mcontext;
    private List<LSGIAODICH> mls;

    public LS_Adapter(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void setData(List<LSGIAODICH> list){
        this.mls = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public lsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_gd,parent,false);
        return new lsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull lsViewHolder holder, int position) {
        final LSGIAODICH lsgiaodich = mls.get(position);
        if(lsgiaodich == null){
            return;
        }
        holder.imgHinh.setImageResource(lsgiaodich.getResourceID());
        holder.txttb.setText(lsgiaodich.getThongbao());
        SimpleDateFormat sdf2 = new SimpleDateFormat("E, dd/MM");
        holder.txtngay.setText(sdf2.format(lsgiaodich.getNgay()));
        holder.txttien.setText(lsgiaodich.getTien());
        holder.txttien.setTextColor(Color.parseColor(lsgiaodich.getMau()));
        holder.lnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ChiTietLSGD.class);
                intent.putExtra("MaGD",lsgiaodich.getMagd());
                intent.putExtra("id",lsgiaodich.getId());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mls != null){
            return mls.size();
        }
        return 0;
    }

    public class lsViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgHinh;
        private TextView txttb,txtngay,txttien;
        private LinearLayout lnClick;

        public lsViewHolder(@NonNull View itemView) {
            super(itemView);

            lnClick = itemView.findViewById(R.id.Linerlayout_click);
            imgHinh = itemView.findViewById(R.id.imghinh);
            txttb = itemView.findViewById(R.id.txt_thongbao);
            txtngay = itemView.findViewById(R.id.txt_ngay);
            txttien = itemView.findViewById(R.id.txt_tien);

        }
    }
}
