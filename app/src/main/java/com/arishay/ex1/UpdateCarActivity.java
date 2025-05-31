package com.arishay.ex1;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class UpdateCarActivity extends AppCompatActivity {

    private EditText etMake, etModel, etYear, etColor;
    private ImageButton btnUpdate, btnBack;
    private TextView tvUpdate;

    private DatabaseReference carRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        etMake = findViewById(R.id.etMake);
        etModel = findViewById(R.id.etModel);
        etYear = findViewById(R.id.etYear);
        etColor = findViewById(R.id.etColor);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvUpdate = findViewById(R.id.tvUpdate);
        btnBack = findViewById(R.id.btnBack);

        carRef = FirebaseDatabase.getInstance().getReference("cars");

        btnUpdate.setOnClickListener(v -> updateCar());
        tvUpdate.setOnClickListener(v -> updateCar());
        btnBack.setOnClickListener(v -> finish());
    }

    private void updateCar() {
        String make = etMake.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();
        String color = etColor.getText().toString().trim();

        if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Year must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Search and update car by matching make + model + year
        carRef.orderByChild("make").equalTo(make)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean found = false;
                        for (DataSnapshot carSnap : snapshot.getChildren()) {
                            Car car = carSnap.getValue(Car.class);
                            if (car != null && car.model.equals(model) && car.year == year) {
                                String carId = carSnap.getKey();
                                Car updatedCar = new Car(carId, make, model, year, color);
                                carRef.child(carId).setValue(updatedCar);
                                Toast.makeText(UpdateCarActivity.this, "Car updated!", Toast.LENGTH_SHORT).show();
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            Toast.makeText(UpdateCarActivity.this, "Car not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UpdateCarActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
