package com.arishay.ex1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateCarActivity extends AppCompatActivity {
    private EditText etCarId, etMake, etModel, etYear, etColor;
    private Button btnUpdate;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        etCarId = findViewById(R.id.etCarId);
        etMake = findViewById(R.id.etMake);
        etModel = findViewById(R.id.etModel);
        etYear = findViewById(R.id.etYear);
        etColor = findViewById(R.id.etColor);
        btnUpdate = findViewById(R.id.btnUpdate);

        dbRef = FirebaseDatabase.getInstance().getReference("cars");

        btnUpdate.setOnClickListener(v -> updateCar());
    }

    private void updateCar() {
        String id = etCarId.getText().toString().trim();
        String make = etMake.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String color = etColor.getText().toString().trim();

        if (id.isEmpty()) {
            etCarId.setError("ID required");
            return;
        }
        if (make.isEmpty() || model.isEmpty() || etYear.getText().toString().trim().isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int year;
        try {
            year = Integer.parseInt(etYear.getText().toString().trim());
        } catch (NumberFormatException e) {
            etYear.setError("Enter a valid year");
            return;
        }

        Car car = new Car(make, model, year, color);
        dbRef.child(id).setValue(car).addOnSuccessListener(aVoid -> {
            Toast.makeText(UpdateCarActivity.this, "Car updated successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        }).addOnFailureListener(e -> Toast.makeText(UpdateCarActivity.this, "Failed to update car: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void clearFields() {
        etCarId.setText("");
        etMake.setText("");
        etModel.setText("");
        etYear.setText("");
        etColor.setText("");
    }
}
