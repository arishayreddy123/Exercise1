package com.arishay.ex1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class SearchCarActivity extends AppCompatActivity {

    private EditText etSearch;
    private ImageButton btnSearch, btnBackSearch;
    private TextView tvNoResults;
    private RecyclerView rvSearchResults;

    private DatabaseReference dbRef;
    private ArrayList<Car> carList;
    private CarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car);

        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnBackSearch = findViewById(R.id.btnBackSearch);
        tvNoResults = findViewById(R.id.tvNoResults);
        rvSearchResults = findViewById(R.id.rvSearchResults);

        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        carList = new ArrayList<>();
        adapter = new CarAdapter(carList);
        rvSearchResults.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("cars");

        btnBackSearch.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            String makeToSearch = etSearch.getText().toString().trim().toLowerCase();
            if (TextUtils.isEmpty(makeToSearch)) {
                Toast.makeText(this, "Please enter a car make", Toast.LENGTH_SHORT).show();
                return;
            }
            searchCarsByMake(makeToSearch);
        });
    }

    private void searchCarsByMake(String make) {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carList.clear();
                for (DataSnapshot carSnap : snapshot.getChildren()) {
                    Car car = carSnap.getValue(Car.class);
                    if (car != null && car.make != null &&
                            car.make.toLowerCase().contains(make)) {
                        carList.add(car);
                    }
                }

                if (carList.isEmpty()) {
                    tvNoResults.setVisibility(View.VISIBLE);
                } else {
                    tvNoResults.setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchCarActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
