package com.example.coursemanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemanager.R;

import java.util.ArrayList;
import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {
    @NonNull
    List<String> newsNames;
    private static onCallBack mListener;
    public NewsRecyclerViewAdapter(@NonNull List<String> newsNames,onCallBack mListener) {
        this.newsNames = newsNames;
        this.mListener = mListener;
    }

    @Override
    public NewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_one_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.tvNewsName.setText(newsNames.get(position));
    }

    @Override
    public int getItemCount() {
        return newsNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNewsName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNewsName = itemView.findViewById(R.id.tvNewsName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClickListener(getPosition());
                }
            });
        }
    }
    public interface onCallBack{
        void onItemClickListener(int position);
    }
}
