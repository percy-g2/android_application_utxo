package com.androidevlinux.percy.UTXO.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.blocktrail.TransactionBean;

import java.util.ArrayList;

/**
 * Created by percy on 28/11/17.
 */

public class BlockChainExplorerTransactionAdapter extends RecyclerView.Adapter<BlockChainExplorerTransactionAdapter.ViewHolder> {

    private ArrayList<TransactionBean> transactionBeanArrayList;
    private Context context;
    private int lastPosition = -1;

    public BlockChainExplorerTransactionAdapter(ArrayList<TransactionBean> transactionBeanArrayList, Context context) {
        this.transactionBeanArrayList = transactionBeanArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BlockChainExplorerTransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.block_chain_explorer_transaction_adapter_row, parent, false);
        return new BlockChainExplorerTransactionAdapter.ViewHolder(v);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BlockChainExplorerTransactionAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(@NonNull final BlockChainExplorerTransactionAdapter.ViewHolder holder, int position) {
        final TransactionBean transactionBean = transactionBeanArrayList.get(holder.getAdapterPosition());
        holder.txt_view1.setText(R.string.block_hash);
        holder.txt_value1.setText(transactionBean.getBlockHash());
        holder.txt_view2.setText(R.string.first_seen_at);
        holder.txt_value2.setText(transactionBean.getFirstSeenAt());
        holder.txt_view3.setText(R.string.last_seen_at);
        holder.txt_value3.setText(String.valueOf(transactionBean.getLastSeenAt()));
        holder.txt_view4.setText(R.string.block_height);
        holder.txt_value4.setText(String.valueOf(transactionBean.getBlockHeight()));
        holder.txt_view5.setText(R.string.block_time);
        holder.txt_value5.setText(String.valueOf(transactionBean.getBlockTime()));
        holder.txt_view6.setText(R.string.confirmations);
        holder.txt_value6.setText(String.valueOf(transactionBean.getConfirmations()));
        holder.txt_view7.setText(R.string.is_coinbase);
        holder.txt_value7.setText(String.valueOf(transactionBean.getIsCoinbase()));
        holder.txt_view8.setText(R.string.estimated_value);
        holder.txt_value8.setText(String.valueOf(transactionBean.getEstimatedValue()));
        holder.txt_view9.setText(R.string.total_input_value);
        holder.txt_value9.setText(String.valueOf(transactionBean.getTotalInputValue()));
        holder.txt_view10.setText(R.string.total_output_value);
        holder.txt_value10.setText(String.valueOf(transactionBean.getTotalOutputValue()));
        holder.txt_view11.setText(R.string.total_fee);
        holder.txt_value11.setText(String.valueOf(transactionBean.getTotalFee()));
        holder.txt_view12.setText(R.string.estimated_change);
        holder.txt_value12.setText(String.valueOf(transactionBean.getEstimatedChange()));
        holder.txt_view13.setText(R.string.estimated_change_address);
        holder.txt_value13.setText(String.valueOf(transactionBean.getEstimatedChangeAddress()));
        holder.txt_view14.setText(R.string.high_priority);
        holder.txt_value14.setText(String.valueOf(transactionBean.getHighPriority()));
        holder.txt_view15.setText(R.string.enough_fee);
        holder.txt_value15.setText(String.valueOf(transactionBean.getEnoughFee()));
        holder.txt_view16.setText(R.string.expand_input_list);
        holder.txt_view17.setText(R.string.expand_output_list);
        if (transactionBean.getInputs().size() != 0) {
            holder.input_list_txt_view1.setText(R.string.index);
            holder.input_list_txt_value1.setText(String.valueOf(transactionBean.getInputs().get(0).getIndex()));
            holder.input_list_txt_view2.setText(R.string.output_hash);
            holder.input_list_txt_value2.setText(String.valueOf(transactionBean.getInputs().get(0).getOutputHash()));
            holder.input_list_txt_view3.setText(R.string.output_index);
            holder.input_list_txt_value3.setText(String.valueOf(transactionBean.getInputs().get(0).getOutputIndex()));
            holder.input_list_txt_view4.setText(R.string.value);
            holder.input_list_txt_value4.setText(String.valueOf(transactionBean.getInputs().get(0).getValue()));
            holder.input_list_txt_view5.setText(R.string.address);
            holder.input_list_txt_value5.setText(String.valueOf(transactionBean.getInputs().get(0).getAddress()));
            holder.input_list_txt_view6.setText(R.string.type);
            holder.input_list_txt_value6.setText(String.valueOf(transactionBean.getInputs().get(0).getType()));
            holder.input_list_txt_view7.setText(R.string.multisig);
            holder.input_list_txt_value7.setText(String.valueOf(transactionBean.getInputs().get(0).getMultisig()));
            holder.input_list_txt_view8.setText(R.string.script_signature);
            holder.input_list_txt_value8.setText(String.valueOf(transactionBean.getInputs().get(0).getScriptSignature()));
        }
        holder.txt_view16.setOnClickListener(view -> {
            if (transactionBean.getInputs().size() != 0) {
                if (holder.input_list.getVisibility() == View.GONE) {
                    holder.input_list.setVisibility(View.VISIBLE);
                } else {
                    holder.input_list.setVisibility(View.GONE);
                }
            }
        });

        if (transactionBean.getOutputs().size() != 0) {
            holder.output_list_txt_view1.setText(R.string.index);
            holder.output_list_txt_value1.setText(String.valueOf(transactionBean.getOutputs().get(0).getIndex()));
            holder.output_list_txt_view2.setText(R.string.value);
            holder.output_list_txt_value2.setText(String.valueOf(transactionBean.getOutputs().get(0).getValue()));
            holder.output_list_txt_view3.setText(R.string.address);
            holder.output_list_txt_value3.setText(String.valueOf(transactionBean.getOutputs().get(0).getAddress()));
            holder.output_list_txt_view4.setText(R.string.type);
            holder.output_list_txt_value4.setText(String.valueOf(transactionBean.getOutputs().get(0).getType()));
            holder.output_list_txt_view5.setText(R.string.multisig);
            holder.output_list_txt_value5.setText(String.valueOf(transactionBean.getOutputs().get(0).getMultisig()));
            holder.output_list_txt_view6.setText(R.string.script);
            holder.output_list_txt_value6.setText(String.valueOf(transactionBean.getOutputs().get(0).getScript()));
            holder.output_list_txt_view7.setText(R.string.script_hex);
            holder.output_list_txt_value7.setText(String.valueOf(transactionBean.getOutputs().get(0).getScriptHex()));
            holder.output_list_txt_view8.setText(R.string.spent_hash);
            holder.output_list_txt_value8.setText(String.valueOf(transactionBean.getOutputs().get(0).getSpentHash()));
            holder.output_list_txt_view9.setText(R.string.spent_index);
            holder.output_list_txt_value9.setText(String.valueOf(transactionBean.getOutputs().get(0).getSpentIndex()));
        }
        holder.txt_view17.setOnClickListener(view -> {
            if (transactionBean.getOutputs().size() != 0) {
                if (holder.output_list.getVisibility() == View.GONE) {
                    holder.output_list.setVisibility(View.VISIBLE);
                } else {
                    holder.output_list.setVisibility(View.GONE);
                }
            }
        });

        Animation animation = AnimationUtils.loadAnimation(context,
                (holder.getAdapterPosition() > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return transactionBeanArrayList.size();
    }

    /**
     * View holder class
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView output_list_txt_view1;
        private TextView output_list_txt_value1;
        private TextView output_list_txt_view2;
        private TextView output_list_txt_value2;
        private TextView output_list_txt_view3;
        private TextView output_list_txt_value3;
        private TextView output_list_txt_view4;
        private TextView output_list_txt_value4;
        private TextView output_list_txt_view5;
        private TextView output_list_txt_value5;
        private TextView output_list_txt_view6;
        private TextView output_list_txt_value6;
        private TextView output_list_txt_view7;
        private TextView output_list_txt_value7;
        private TextView output_list_txt_view8;
        private TextView output_list_txt_value8;
        private TextView output_list_txt_view9;
        private TextView output_list_txt_value9;
        private TextView input_list_txt_view1;
        private TextView input_list_txt_value1;
        private TextView input_list_txt_view2;
        private TextView input_list_txt_value2;
        private TextView input_list_txt_view3;
        private TextView input_list_txt_value3;
        private TextView input_list_txt_view4;
        private TextView input_list_txt_value4;
        private TextView input_list_txt_view5;
        private TextView input_list_txt_value5;
        private TextView input_list_txt_view6;
        private TextView input_list_txt_value6;
        private TextView input_list_txt_view7;
        private TextView input_list_txt_value7;
        private TextView input_list_txt_view8;
        private TextView input_list_txt_value8;
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
        private TextView txt_view15;
        private TextView txt_value15;
        private TextView txt_view16;
        private TextView txt_view17;
        private LinearLayout input_list;
        private LinearLayout output_list;

        private ViewHolder(View view) {
            super(view);

            output_list = view.findViewById(R.id.output_list);
            output_list_txt_view1 = view.findViewById(R.id.output_list_txt_view1);
            output_list_txt_value1 = view.findViewById(R.id.output_list_txt_value1);
            output_list_txt_view2 = view.findViewById(R.id.output_list_txt_view2);
            output_list_txt_value2 = view.findViewById(R.id.output_list_txt_value2);
            output_list_txt_view3 = view.findViewById(R.id.output_list_txt_view3);
            output_list_txt_value3 = view.findViewById(R.id.output_list_txt_value3);
            output_list_txt_view4 = view.findViewById(R.id.output_list_txt_view4);
            output_list_txt_value4 = view.findViewById(R.id.output_list_txt_value4);
            output_list_txt_view5 = view.findViewById(R.id.output_list_txt_view5);
            output_list_txt_value5 = view.findViewById(R.id.output_list_txt_value5);
            output_list_txt_view6 = view.findViewById(R.id.output_list_txt_view6);
            output_list_txt_value6 = view.findViewById(R.id.output_list_txt_value6);
            output_list_txt_view7 = view.findViewById(R.id.output_list_txt_view7);
            output_list_txt_value7 = view.findViewById(R.id.output_list_txt_value7);
            output_list_txt_view8 = view.findViewById(R.id.output_list_txt_view8);
            output_list_txt_value8 = view.findViewById(R.id.output_list_txt_value8);
            output_list_txt_view9 = view.findViewById(R.id.output_list_txt_view9);
            output_list_txt_value9 = view.findViewById(R.id.output_list_txt_value9);
            input_list = view.findViewById(R.id.input_list);
            input_list_txt_view1 = view.findViewById(R.id.input_list_txt_view1);
            input_list_txt_value1 = view.findViewById(R.id.input_list_txt_value1);
            input_list_txt_view2 = view.findViewById(R.id.input_list_txt_view2);
            input_list_txt_value2 = view.findViewById(R.id.input_list_txt_value2);
            input_list_txt_view3 = view.findViewById(R.id.input_list_txt_view3);
            input_list_txt_value3 = view.findViewById(R.id.input_list_txt_value3);
            input_list_txt_view4 = view.findViewById(R.id.input_list_txt_view4);
            input_list_txt_value4 = view.findViewById(R.id.input_list_txt_value4);
            input_list_txt_view5 = view.findViewById(R.id.input_list_txt_view5);
            input_list_txt_value5 = view.findViewById(R.id.input_list_txt_value5);
            input_list_txt_view6 = view.findViewById(R.id.input_list_txt_view6);
            input_list_txt_value6 = view.findViewById(R.id.input_list_txt_value6);
            input_list_txt_view7 = view.findViewById(R.id.input_list_txt_view7);
            input_list_txt_value7 = view.findViewById(R.id.input_list_txt_value7);
            input_list_txt_view8 = view.findViewById(R.id.input_list_txt_view8);
            input_list_txt_value8 = view.findViewById(R.id.input_list_txt_value8);
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
            txt_view15 = view.findViewById(R.id.txt_view15);
            txt_value15 = view.findViewById(R.id.txt_value15);
            txt_view16 = view.findViewById(R.id.txt_view16);
            txt_view17 = view.findViewById(R.id.txt_view17);
        }
    }
}
