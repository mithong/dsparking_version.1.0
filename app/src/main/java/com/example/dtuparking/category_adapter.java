package com.example.dtuparking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class category_adapter extends RecyclerView.Adapter<category_adapter.CategoryViewHolder> {
    private Context mcontext;
    private List<Category> mlistCatalogy;

    public category_adapter(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void setData(List<Category> list){
        this.mlistCatalogy = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category catagory = mlistCatalogy.get(position);
        if(catagory == null){
            return;
        }
        holder.txttencata.setText(catagory.getNameCatalogy());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext,RecyclerView.VERTICAL,false);
        holder.rcvLS.setLayoutManager(linearLayoutManager);

        LS_Adapter lsAdapter = new LS_Adapter(mcontext);
        lsAdapter.setData(catagory.getList());

        holder.rcvLS.setAdapter(lsAdapter);

    }

    @Override
    public int getItemCount() {
        if(mlistCatalogy != null){
            return mlistCatalogy.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView txttencata;
        private RecyclerView rcvLS;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txttencata = itemView.findViewById(R.id.txt_catalogy);
            rcvLS = itemView.findViewById(R.id.rcv_ls);
        }
    }
}
