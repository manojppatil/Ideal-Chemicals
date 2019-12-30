package com.bitsinfotec.idealchemicals.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bitsinfotec.idealchemicals.AdminActivity;
import com.bitsinfotec.idealchemicals.R;
import com.bitsinfotec.idealchemicals.challan_receipt;
import com.bitsinfotec.idealchemicals.lorry_receipt;

import java.util.ArrayList;
import java.util.HashMap;

public class lr_adapter extends RecyclerView.Adapter<lr_adapter.ViewHolder> {

    private ArrayList items;
    private int itemLayout;
    private Context context;
    HashMap hm;

    public lr_adapter(ArrayList items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lr_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        hm = (HashMap) items.get(position);
        holder.id.setText(hm.get("id").toString());
        holder.transporter.setText(hm.get("transporter").toString());
        holder.lrfrom.setText(hm.get("lrfrom").toString());
        holder.lrto.setText(hm.get("lrto").toString());
        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, lorry_receipt.class);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView transporter;
        public TextView lrfrom;
        public TextView lrto;
        public Button print;

        public ViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.lr_id);
            transporter = itemView.findViewById(R.id.transporter);
            lrfrom = itemView.findViewById(R.id.lr_from);
            lrto = itemView.findViewById(R.id.lr_to);
            print = itemView.findViewById(R.id.print_lr);

        }
    }
}