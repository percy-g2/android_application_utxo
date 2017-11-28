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
import com.androidevlinux.percy.UTXO.data.models.blocktrail.AddressBean;

import java.util.ArrayList;

/**
 * Created by percy on 28/11/17.
 */

public class BlockChainExplorerAddressAdapter extends RecyclerView.Adapter<BlockChainExplorerAddressAdapter.ViewHolder> {

    private ArrayList<AddressBean> addressBeanArrayList = null;
    private Context context;
    private int lastPosition = -1;

    public BlockChainExplorerAddressAdapter(ArrayList<AddressBean> addressBeanArrayList, Context context) {
        this.addressBeanArrayList = addressBeanArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.block_chain_explorer_adapter_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onViewDetachedFromWindow(BlockChainExplorerAddressAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AddressBean addressBean = addressBeanArrayList.get(holder.getAdapterPosition());
        holder.txt_view1.setText(R.string.address);
        holder.txt_value1.setText(addressBean.getAddress());
        holder.txt_view2.setText(R.string.hash160);
        holder.txt_value2.setText(addressBean.getHash160());
        holder.txt_view3.setText(R.string.balance);
        holder.txt_value3.setText(String.valueOf(addressBean.getBalance()));
        holder.txt_view4.setText(R.string.received);
        holder.txt_value4.setText(String.valueOf(addressBean.getReceived()));
        holder.txt_view5.setText(R.string.sent);
        holder.txt_value5.setText(String.valueOf(addressBean.getSent()));
        holder.txt_view6.setText(R.string.transactions);
        holder.txt_value6.setText(String.valueOf(addressBean.getTransactions()));
        holder.txt_view7.setText(R.string.utxos);
        holder.txt_value7.setText(String.valueOf(addressBean.getUtxos()));
        holder.txt_view8.setText(R.string.unconfirmed_received);
        holder.txt_value8.setText(String.valueOf(addressBean.getUnconfirmedReceived()));
        holder.txt_view9.setText(R.string.unconfirmed_sent);
        holder.txt_value9.setText(String.valueOf(addressBean.getUnconfirmedSent()));
        holder.txt_view10.setText(R.string.unconfirmed_transactions);
        holder.txt_value10.setText(String.valueOf(addressBean.getUnconfirmedTransactions()));
        holder.txt_view11.setText(R.string.unconfirmed_utxos);
        holder.txt_value11.setText(String.valueOf(addressBean.getUnconfirmedUtxos()));
        holder.txt_view12.setText(R.string.total_transactions_in);
        holder.txt_value12.setText(String.valueOf(addressBean.getTotalTransactionsIn()));
        holder.txt_view13.setText(R.string.total_transactions_out);
        holder.txt_value13.setText(String.valueOf(addressBean.getTotalTransactionsOut()));

        Animation animation = AnimationUtils.loadAnimation(context,
                (holder.getAdapterPosition() > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return addressBeanArrayList.size();
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
        }
    }
}
