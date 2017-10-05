package com.app.creaseart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.creaseart.R;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.models.Bundle;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterPickupBundle extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Bundle[] detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    public AdapterPickupBundle(Context context, OnCustomItemClicListener lis, Bundle[] list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_pickupbundle, parent, false);

            vh = new CustomViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof CustomViewHolder) {
            Bundle m1 = detail[i];

            ((CustomViewHolder) holder).text_name.setText(m1.getBundleName());
            ((CustomViewHolder) holder).text_date.setText("Price: " + m1.getBundlePrice());
            ((CustomViewHolder) holder).text_unit.setText("Unit: " + m1.getBundleUnit());

            if (i == m1.getSeletedPosition()) {
                ((CustomViewHolder) holder).card_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_bg));
            } else {
                ((CustomViewHolder) holder).card_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            }

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return detail.length;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_name, text_unit, text_date;
        ImageView image_viewers;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
            this.image_viewers = (ImageView) view.findViewById(R.id.image_viewers);
            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.text_unit = (TextView) view.findViewById(R.id.text_unit);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(getPosition(), 2);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Bundle m1 = detail[position];
        if (m1.getRowType() == 1) {
            return VIEW_ITEM;
        } else if (m1.getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }
}