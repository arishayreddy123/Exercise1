package com.arishay.ex1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDeleteCarActivity extends AppCompatActivity {

    private EditText etMake, etModel, etYear, etColor;
    private Button btnUpdate, btnDelete;

    private String carId;
    private DatabaseReference carRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_car);

        etMake = findViewById(R.id.etMake);
        etModel = findViewById(R.id.etModel);
        etYear = findViewById(R.id.etYear);
        etColor = findViewById(R.id.etColor);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Receive data from intent
        carId = getIntent().getStringExtra("carId");
        String make = getIntent().getStringExtra("make");
        String model = getIntent().getStringExtra("model");
        int year = getIntent().getIntExtra("year", 0);
        String color = getIntent().getStringExtra("color");

        if (carId == null) {
            Toast.makeText(this, "Error: no car ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etMake.setText(make);
        etModel.setText(model);
        etYear.setText(String.valueOf(year));
        etColor.setText(color);

        carRef = FirebaseDatabase.getInstance().getReference("cars").child(carId);

        btnUpdate.setOnClickListener(v -> updateCar());
        btnDelete.setOnClickListener(v -> confirmDeleteCar());
    }

    private void updateCar() {
        String make = etMake.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();
        String color = etColor.getText().toString().trim();

        if (TextUtils.isEmpty(make) || TextUtils.isEmpty(model) || TextUtils.isEmpty(yearStr) || TextUtils.isEmpty(color)) {
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

        Car updatedCar = new Car(make, model, year, color);
        carRef.setValue(updatedCar).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UpdateDeleteCarActivity.this, "Car updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(UpdateDeleteCarActivity.this, "Failed to update car", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDeleteCar() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Car")
                .setMessage("Are you sure you want to delete this car?")
                .setPositiveButton("Yes", (dialog, which) -> deleteCar())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteCar() {
        carRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UpdateDeleteCarActivity.this, "Car deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(UpdateDeleteCarActivity.this, "Failed to delete car", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
