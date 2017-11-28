package com.androidevlinux.percy.UTXO.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by percy on 28/11/17.
 */

public class BlockChainExplorerBlockAdapter extends RecyclerView.Adapter<BlockChainExplorerBlockAdapter.ViewHolder> {

    private ArrayList<JsonObject> jsonObjectArrayList = null;
    private Context context;
    private int lastPosition = -1;

    public BlockChainExplorerBlockAdapter(ArrayList<JsonObject> jsonObjectArrayList, Context context) {
        this.jsonObjectArrayList = jsonObjectArrayList;
        this.context = context;
    }

    @Override
    public BlockChainExplorerBlockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.block_chain_explorer_block_adapter_row, parent, false);
        return new BlockChainExplorerBlockAdapter.ViewHolder(v);
    }

    @Override
    public void onViewDetachedFromWindow(BlockChainExplorerBlockAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(final BlockChainExplorerBlockAdapter.ViewHolder holder, int position) {
        final JsonObject jsonObject = jsonObjectArrayList.get(holder.getAdapterPosition());
        holder.txt_view1.setText(R.string.hash);
        holder.txt_value1.setText(String.valueOf(jsonObject.get("hash")));
        holder.txt_view2.setText(R.string.version);
        holder.txt_value2.setText(String.valueOf(jsonObject.get("version")));
        holder.txt_view3.setText(R.string.height);
        holder.txt_value3.setText(String.valueOf(jsonObject.get("height")));
        holder.txt_view4.setText(R.string.block_time);
        holder.txt_value4.setText(String.valueOf(jsonObject.get("block_time")));
        holder.txt_view5.setText(R.string.arrival_time);
        holder.txt_value5.setText(String.valueOf(jsonObject.get("arrival_time")));
        holder.txt_view6.setText(R.string.nonce);
        holder.txt_value6.setText(String.valueOf(jsonObject.get("nonce")));
        holder.txt_view7.setText(R.string.difficulty);
        holder.txt_value7.setText(String.valueOf(jsonObject.get("difficulty")));
        holder.txt_view8.setText(R.string.byte_size);
        holder.txt_value8.setText(String.valueOf(jsonObject.get("byte_size")));
        holder.txt_view9.setText(R.string.confirmations);
        holder.txt_value9.setText(String.valueOf(jsonObject.get("confirmations")));
        holder.txt_view10.setText(R.string.transactions);
        holder.txt_value10.setText(String.valueOf(jsonObject.get("transactions")));
        holder.txt_view11.setText(R.string.value);
        holder.txt_value11.setText(String.valueOf(jsonObject.get("value")));
        holder.txt_view12.setText(R.string.miningpool_name);
        holder.txt_value12.setText(String.valueOf(jsonObject.get("miningpool_name")));
        holder.txt_view13.setText(R.string.miningpool_url);
        holder.txt_value13.setText(String.valueOf(jsonObject.get("miningpool_url")));
        holder.txt_view14.setText(R.string.miningpool_slug);
        holder.txt_value14.setText(String.valueOf(jsonObject.get("miningpool_slug")));

        Animation animation = AnimationUtils.loadAnimation(context,
                (holder.getAdapterPosition() > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return jsonObjectArrayList.size();
    }

    /**
     * View holder class
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_view1;
        private TextView txt_value1;
        private TextView txt_view2;
        private TextView txt_value2;
        private TextView txt_view3;
        private TextView txt_value3;
        private TextView txt_view4;
        private TextView txt_value4;
        private TextView txt_view5;
        private TextView txt_value5;
        private TextView txt_view6;
        private TextView txt_value6;
        private TextView txt_view7;
        private TextView txt_value7;
        private TextView txt_view8;
        private TextView txt_value8;
        private TextView txt_view9;
        private TextView txt_value9;
        private TextView txt_view10;
        private TextView txt_value10;
        private TextView txt_view11;
        private TextView txt_value11;
        private TextView txt_view12;
        private TextView txt_value12;
        private TextView txt_view13;
        private TextView txt_value13;
        private TextView txt_view14;
        private TextView txt_value14;

        private ViewHolder(View view) {
            super(view);
            txt_view1 = view.findViewById(R.id.txt_view1);
            txt_value1 = view.findViewById(R.id.txt_value1);
            txt_view2 = view.findViewById(R.id.txt_view2);
            txt_value2 = view.findViewById(R.id.txt_value2);
            txt_view3 = view.findViewById(R.id.txt_view3);
            txt_value3 = view.findViewById(R.id.txt_value3);
            txt_view4 = view.findViewById(R.id.txt_view4);
            txt_value4 = view.findViewById(R.id.txt_value4);
            txt_view5 = view.findViewById(R.id.txt_view5);
            txt_value5 = view.findViewById(R.id.txt_value5);
            txt_view6 = view.findViewById(R.id.txt_view6);
            txt_value6 = view.findViewById(R.id.txt_value6);
            txt_view7 = view.findViewById(R.id.txt_view7);
            txt_value7 = view.findViewById(R.id.txt_value7);
            txt_view8 = view.findViewById(R.id.txt_view8);
            txt_value8 = view.findViewById(R.id.txt_value8);
            txt_view9 = view.findViewById(R.id.txt_view9);
            txt_value9 = view.findViewById(R.id.txt_value9);
            txt_view10 = view.findViewById(R.id.txt_view10);
            txt_value10 = view.findViewById(R.id.txt_value10);
            txt_view11 = view.findViewById(R.id.txt_view11);
            txt_value11 = view.findViewById(R.id.txt_value11);
            txt_view12 = view.findViewById(R.id.txt_view12);
            txt_value12 = view.findViewById(R.id.txt_value12);
            txt_view13 = view.findViewById(R.id.txt_view13);
            txt_value13 = view.findViewById(R.id.txt_value13);
            txt_view14 = view.findViewById(R.id.txt_view14);
            txt_value14 = view.findViewById(R.id.txt_value14);
        }
    }
}
