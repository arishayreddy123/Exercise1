package com.arishay.ex1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ListCarsActivity extends AppCompatActivity {

    private ListView listViewCars;
    private Button btnAddCar;
    private DatabaseReference carsRef;

    private ArrayList<String> carList = new ArrayList<>();
    private HashMap<String, Car> carMap = new HashMap<>();
    private ArrayList<String> carKeys = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cars);

        listViewCars = findViewById(R.id.listViewCars);
        btnAddCar = findViewById(R.id.btnAddCar);
        carsRef = FirebaseDatabase.getInstance().getReference("cars");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carList);
        listViewCars.setAdapter(adapter);

        loadCarsFromFirebase();

        btnAddCar.setOnClickListener(v -> {
            Intent intent = new Intent(ListCarsActivity.this, AddCarActivity.class);
            startActivity(intent);
        });

        listViewCars.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCarId = carKeys.get(position);
            Car selectedCar = carMap.get(selectedCarId);

            if (selectedCar != null) {
                Intent intent = new Intent(ListCarsActivity.this, UpdateDeleteCarActivity.class);
                intent.putExtra("carId", selectedCarId);
                intent.putExtra("make", selectedCar.make);
                intent.putExtra("model", selectedCar.model);
                intent.putExtra("year", selectedCar.year);
                intent.putExtra("color", selectedCar.color);
                startActivity(intent);
            } else {
                Toast.makeText(ListCarsActivity.this, "Error: Car not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCarsFromFirebase() {
        carsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carList.clear();
                carMap.clear();
                carKeys.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    Car car = ds.getValue(Car.class);
                    if (car != null) {
                        carKeys.add(key);
                        carMap.put(key, car);
                        String displayText = car.make + " " + car.model + " (" + car.year + ") - " + car.color;
                        carList.add(displayText);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListCarsActivity.this, "Failed to load cars: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
