package com.arishay.ex1;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class DeleteCarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarDeleteAdapter adapter;
    private ArrayList<Car> carList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_car);

        recyclerView = findViewById(R.id.recyclerViewDelete);  // ✅ This ID must exist in your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carList = new ArrayList<>();

        adapter = new CarDeleteAdapter(this, carList, this::deleteCar);
        recyclerView.setAdapter(adapter);

        loadCars();
    }

    private void loadCars() {
        FirebaseDatabase.getInstance().getReference("cars")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        carList.clear();
                        for (DataSnapshot carSnap : snapshot.getChildren()) {
                            Car car = carSnap.getValue(Car.class);
                            if (car != null) {
                                car.id = carSnap.getKey();
                                carList.add(car);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DeleteCarActivity.this, "Failed to load cars.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteCar(String carId) {
        FirebaseDatabase.getInstance().getReference("cars").child(carId)
                .removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Car deleted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete car", Toast.LENGTH_SHORT).show());
    }
}
