package com.ark.dmptest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Job> mItemList;
    Context mContext;

    public JobAdapter(Context context, List<Job> itemList) {
        mItemList = itemList;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_job, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        populateItemRows((ItemViewHolder) viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobName, tvCompanyName, tvCompanyLocation;
        ImageView ivLogo;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJobName = itemView.findViewById(R.id.tvJobName);
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvCompanyLocation = itemView.findViewById(R.id.tvCompanyLocation);
            ivLogo = itemView.findViewById(R.id.ivLogo);
        }
    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        Job item = mItemList.get(position);
        viewHolder.tvJobName.setText(item.title);
        viewHolder.tvCompanyName.setText(item.company);
        viewHolder.tvCompanyLocation.setText(item.location);
        //Glide.with(mContext).load(item.url).into(viewHolder.ivLogo);
    }

}
