package com.arishay.ex1;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchCarActivity extends AppCompatActivity {

    private EditText etSearch;
    private Button btnSearch;
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
        rvSearchResults = findViewById(R.id.rvSearchResults);

        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        carList = new ArrayList<>();
        adapter = new CarAdapter(carList);
        rvSearchResults.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("cars");

        btnSearch.setOnClickListener(v -> {
            String makeToSearch = etSearch.getText().toString().trim();
            if (TextUtils.isEmpty(makeToSearch)) {
                Toast.makeText(SearchCarActivity.this, "Please enter a car make", Toast.LENGTH_SHORT).show();
                return;
            }
            searchCarsByMake(makeToSearch);
        });
    }

    private void searchCarsByMake(String make) {
        dbRef.orderByChild("make").equalTo(make)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        carList.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot carSnap : snapshot.getChildren()) {
                                Car car = carSnap.getValue(Car.class);
                                if (car != null) {
                                    carList.add(car);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(SearchCarActivity.this, "No cars found with make: " + make, Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SearchCarActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
