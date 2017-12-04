package com.androidevlinux.percy.UTXO.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.price.PriceBean;

import java.util.ArrayList;

/**
 * Created by percy on 3/12/17.
 */

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PriceBean> priceBeanArrayList;

    public PriceAdapter(ArrayList<PriceBean> priceBeanArrayList, Context context) {
        this.priceBeanArrayList = priceBeanArrayList;
        this.context = context;
    }

    @Override
    public PriceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.price_fragment_adapter_row, parent, false);
        return new PriceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PriceAdapter.ViewHolder holder, int position) {
        final PriceBean priceBean = priceBeanArrayList.get(position);
        holder.title.setText(priceBean.getTitle());
        holder.price.setText(priceBean.getPrice());
        holder.price_low.setText(priceBean.getLow_price());
        holder.price_high.setText(priceBean.getHigh_price());
    }

    @Override
    public int getItemCount() {
        return priceBeanArrayList.size();
    }

    /**
     * View holder class
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView price;
        private TextView price_low;
        private TextView price_high;

        private ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_title);
            price = view.findViewById(R.id.txt_price);
            price_low = view.findViewById(R.id.txt_low_price);
            price_high = view.findViewById(R.id.txt_high_price);
        }
    }
}
