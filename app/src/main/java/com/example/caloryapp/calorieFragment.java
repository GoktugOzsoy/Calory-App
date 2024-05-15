package com.example.caloryapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class calorieFragment extends Fragment {
    private RecyclerView recyclerView;
    private CalorieListAdapter adapter;
    private List<FoodModel> foodList;
    private Button calculateButton;
    private Button saveButton;
    private TextView resultTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calorie_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        foodList = new ArrayList<>();
        adapter = new CalorieListAdapter(foodList);
        recyclerView.setAdapter(adapter);

        calculateButton = view.findViewById(R.id.calculateButton);
        saveButton = view.findViewById(R.id.saveButton);
        resultTextView = view.findViewById(R.id.resultTextView);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCaloriesToFirestore();
            }
        });

        loadFoodListFromFirebase();

        return view;
    }

    private void loadFoodListFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Foods")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            foodList.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("Name");
                                double calorie = document.getDouble("Calorie");

                                FoodModel food = new FoodModel(name, calorie);
                                foodList.add(food);
                            }

                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void calculateCalories() {
        double totalCalories = 0;

        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View itemView = recyclerView.getChildAt(i);
            EditText gramEditText = itemView.findViewById(R.id.gramEditText);
            String gramText = gramEditText.getText().toString().trim();

            if (!gramText.isEmpty()) {
                int gramAmount = Integer.parseInt(gramText);
                FoodModel food = foodList.get(i);
                totalCalories += (food.getCalorie() * gramAmount);
            }
        }

        resultTextView.setText("Total Calories: " + totalCalories);
    }

    private void saveCaloriesToFirestore() {
        double totalCalories = Double.parseDouble(resultTextView.getText().toString().substring(15)); // Total Calories: 'den sonrasını al

        Date date = new Date();
        String dateString = DateFormat.getDateInstance().format(date);

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String timeString = timeFormat.format(date);

        dateString += " " + timeString;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> history = new HashMap<>();
        history.put("Date", dateString);
        history.put("Calorie", totalCalories);

        db.collection("History")
                .add(history)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Calories saved successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to save calories. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
