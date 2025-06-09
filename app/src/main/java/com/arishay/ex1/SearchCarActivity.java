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
    private RecyclerView rvSearchResults;
    private TextView tvNoResults;
    private ArrayList<Car> carList;
    private CarAdapter adapter;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car);

        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnBackSearch = findViewById(R.id.btnBackSearch);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        tvNoResults = findViewById(R.id.tvNoResults);

        carList = new ArrayList<>();
        adapter = new CarAdapter(carList);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("cars");

        btnSearch.setOnClickListener(v -> {
            String make = etSearch.getText().toString().trim();
            if (TextUtils.isEmpty(make)) {
                Toast.makeText(this, "Please enter a car make", Toast.LENGTH_SHORT).show();
            } else {
                searchCars(make);
            }
        });

        btnBackSearch.setOnClickListener(v -> finish());
    }

    private void searchCars(String makeQuery) {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Car car = snap.getValue(Car.class);
                    if (car != null && car.make != null &&
                            car.make.equalsIgnoreCase(makeQuery)) {
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
                Toast.makeText(SearchCarActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
