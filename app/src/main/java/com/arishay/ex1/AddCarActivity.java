package com.arishay.ex1;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCarActivity extends AppCompatActivity {

    private EditText etMake, etModel, etYear, etColor;
    private ImageButton btnAdd, btnBack;
    private TextView tvAdd;
    private DatabaseReference carDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        etMake = findViewById(R.id.etMake);
        etModel = findViewById(R.id.etModel);
        etYear = findViewById(R.id.etYear);
        etColor = findViewById(R.id.etColor);
        btnAdd = findViewById(R.id.btnAdd);
        tvAdd = findViewById(R.id.tvAdd);
        btnBack = findViewById(R.id.btnBack);

        carDatabase = FirebaseDatabase.getInstance().getReference("cars");

        btnAdd.setOnClickListener(view -> addCar());
        tvAdd.setOnClickListener(view -> addCar());

        btnBack.setOnClickListener(view -> finish());
    }

    private void addCar() {
        String make = etMake.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();
        String color = etColor.getText().toString().trim();

        if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Year must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = carDatabase.push().getKey();
        if (id == null) {
            Toast.makeText(this, "Failed to generate ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Car newCar = new Car(id, make, model, year, color);

        carDatabase.child(id).setValue(newCar)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Car added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to add car", Toast.LENGTH_SHORT).show()
                );
    }
}
