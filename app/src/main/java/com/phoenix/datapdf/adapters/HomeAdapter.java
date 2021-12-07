package com.phoenix.datapdf.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.phoenix.datapdf.DataActivity;
import com.phoenix.datapdf.R;
import com.phoenix.datapdf.modals.DisplayData;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ItemHolder> {

    List<DisplayData> list;
    Context context;

    public HomeAdapter(List<DisplayData> arrayList, Context activity)
    {
        list = arrayList;
        this.context = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agri, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        DisplayData data = list.get(position);
        holder.regno.setText(data.getRegNo());
        holder.name.setText(data.getName());
        holder.id = data.getId();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MaterialTextView name, regno;
        String id;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            regno = itemView.findViewById(R.id.tv_regno);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, DataActivity.class);
            intent.putExtra("ID", id);
            context.startActivity(intent);
        }
    }
}
