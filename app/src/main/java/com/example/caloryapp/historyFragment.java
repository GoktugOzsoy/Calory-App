package com.example.caloryapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class historyFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryListAdapter adapter;
    private List<HistoryModel> historyList;

    public historyFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);

        recyclerView = view.findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        historyList = new ArrayList<>();
        adapter = new HistoryListAdapter(historyList);
        recyclerView.setAdapter(adapter);

        loadHistoryFromFirestore();

        return view;
    }

    private void loadHistoryFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("History")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            historyList.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String date = document.getString("Date");
                                double calorie = document.getDouble("Calorie");

                                HistoryModel historyItem = new HistoryModel("Date: "+date+"\nCalorie:", calorie);
                                historyList.add(historyItem);
                            }

                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("HistoryFragment", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
