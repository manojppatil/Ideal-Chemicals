package com.bitsinfotec.idealchemicals.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsinfotec.idealchemicals.AdminActivity;
import com.bitsinfotec.idealchemicals.R;
import com.bitsinfotec.idealchemicals.do_receipt;

import java.util.ArrayList;
import java.util.HashMap;

public class do_adapter extends RecyclerView.Adapter<do_adapter.ViewHolder> {
    @NonNull
    private ArrayList items;
    private int itemLayout;
    private Context context;
    HashMap hm;

    public do_adapter(ArrayList items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.do_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        hm = (HashMap) items.get(position);
        holder.id.setText(hm.get("id").toString());
        holder.do_to.setText(hm.get("do_to").toString());
        holder.do_date.setText(hm.get("date_do").toString());
        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, do_receipt.class);
                intent.putExtra("inserted_id", Integer.parseInt(holder.id.getText().toString()));
                context.startActivity(intent);

            }
        });
        holder.itemView.setTag(hm);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView do_to;
        public TextView do_date;
        public Button print;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.do_id);
            do_to= itemView.findViewById(R.id.do_to);
            do_date = itemView.findViewById(R.id.do_date);
            print = itemView.findViewById(R.id.print_do);

        }
    }
}