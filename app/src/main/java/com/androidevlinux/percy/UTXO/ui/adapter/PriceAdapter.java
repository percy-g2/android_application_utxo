package com.androidevlinux.percy.UTXO.ui.adapter;

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

    private ArrayList<PriceBean> priceBeanArrayList;

    public PriceAdapter(ArrayList<PriceBean> priceBeanArrayList) {
        this.priceBeanArrayList = priceBeanArrayList;
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
        if (priceBean.getTitle().equalsIgnoreCase("Zebpay")) {
            holder.footer_start.setText(R.string.buy);
            holder.footer_end.setText(R.string.sell);
        } else {
            holder.footer_start.setText(R.string._24_hr_s_low);
            holder.footer_end.setText(R.string._24_hr_s_high);
        }
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
        private TextView footer_start;
        private TextView footer_end;

        private ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_title);
            price = view.findViewById(R.id.txt_price);
            price_low = view.findViewById(R.id.txt_low_price);
            price_high = view.findViewById(R.id.txt_high_price);
            footer_start = view.findViewById(R.id.footer_start);
            footer_end = view.findViewById(R.id.footer_end);
        }
    }
}
