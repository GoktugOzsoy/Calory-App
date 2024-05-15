package com.example.caloryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {
    private List<HistoryModel> historyList;

    public HistoryListAdapter(List<HistoryModel> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryModel historyItem = historyList.get(position);
        holder.bind(historyItem);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView calorieTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            calorieTextView = itemView.findViewById(R.id.calorieTextView);
        }

        public void bind(HistoryModel historyItem) {
            dateTextView.setText(historyItem.getDate());
            calorieTextView.setText(String.valueOf(historyItem.getCalorie()));
        }
    }
}
