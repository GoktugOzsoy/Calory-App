package com.example.caloryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalorieListAdapter extends RecyclerView.Adapter<CalorieListAdapter.ViewHolder> {
    private List<FoodModel> foodList;

    public CalorieListAdapter(List<FoodModel> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodModel food = foodList.get(position);
        holder.foodNameTextView.setText(food.getName());
        holder.calorieTextView.setText(String.valueOf(food.getCalorie()));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView foodNameTextView;
        public TextView calorieTextView;
        public EditText gramEditText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            calorieTextView = itemView.findViewById(R.id.calorieTextView);
            gramEditText = itemView.findViewById(R.id.gramEditText);
        }
    }
}

