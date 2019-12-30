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
import com.bitsinfotec.idealchemicals.do_receipt;

import java.util.ArrayList;
import java.util.HashMap;

public class challan_adapter extends RecyclerView.Adapter<challan_adapter.ViewHolder> {

    private ArrayList items;
    private int itemLayout;
    private Context context;
    HashMap hm;

    public challan_adapter(ArrayList items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.challan_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        hm = (HashMap) items.get(position);
        holder.id.setText(hm.get("id").toString());
        holder.ch_to.setText(hm.get("chto").toString());
        holder.receiver.setText(hm.get("receiver").toString());
        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, challan_receipt.class);
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
        public TextView ch_to;
        public TextView receiver;
        public Button print;

        public ViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.ch_id);
            ch_to = itemView.findViewById(R.id.ch_to);
            receiver = itemView.findViewById(R.id.receiver);
            print = itemView.findViewById(R.id.printchallan);

        }
    }
}
