package com.androidevlinux.percy.UTXO.utils;

import android.content.Context;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by percy on 9/1/18.
 */

public class CustomMarkerView extends MarkerView {

    private TextView tvContent, tvContent1;

    public CustomMarkerView (Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = findViewById(R.id.tvContent);
        tvContent1 = findViewById(R.id.tvContent1);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("" + e.getX()); // set the entry-value as the display text
        tvContent1.setText("" + e.getY());
        super.refreshContent(e, highlight);
    }


    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }

}


