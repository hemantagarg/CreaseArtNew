package com.app.creaseart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.creaseart.R;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.models.Item;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterPickupItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Item[] detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    public AdapterPickupItem(Context context, OnCustomItemClicListener lis, Item[] list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_pickupitem, parent, false);

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

            Item m1 = detail[i];

            ((CustomViewHolder) holder).text_name.setText(m1.getItemName());
            ((CustomViewHolder) holder).text_date.setText("Price: " + m1.getItemPrice());
            ((CustomViewHolder) holder).text_count.setText(m1.getItemAdded() + "");
            ((CustomViewHolder) holder).img_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onItemClickListener(i, 11);
                }
            });
            ((CustomViewHolder) holder).img_subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onItemClickListener(i, 12);
                }
            });

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return detail.length;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_name, text_message, text_date, text_count;
        ImageView image_viewers, img_add, img_subtract;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.image_viewers = (ImageView) view.findViewById(R.id.image_viewers);
            this.img_add = (ImageView) view.findViewById(R.id.img_add);
            this.img_subtract = (ImageView) view.findViewById(R.id.img_subtract);
            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.text_message = (TextView) view.findViewById(R.id.text_message);
            this.text_count = (TextView) view.findViewById(R.id.text_count);
            this.text_date = (TextView) view.findViewById(R.id.text_date);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(getPosition(), 3);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Item m1 = detail[position];
        if (m1.getRowType() == 1) {
            return VIEW_ITEM;
        } else if (m1.getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }
}