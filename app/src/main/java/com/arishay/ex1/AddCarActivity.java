package com.arishay.ex1;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCarActivity extends AppCompatActivity {

    private EditText etMake, etModel, etYear, etColor;
    private ImageButton btnAdd, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        etMake = findViewById(R.id.etMake);
        etModel = findViewById(R.id.etModel);
        etYear = findViewById(R.id.etYear);
        etColor = findViewById(R.id.etColor);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnAdd.setOnClickListener(v -> {
            String make = etMake.getText().toString().trim();
            String model = etModel.getText().toString().trim();
            String yearStr = etYear.getText().toString().trim();
            String color = etColor.getText().toString().trim();

            if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || color.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int year = Integer.parseInt(yearStr);

            String id = FirebaseDatabase.getInstance().getReference("cars").push().getKey();
            Car newCar = new Car(id, make, model, year, color);

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("cars");
            if (id != null) {
                dbRef.child(id).setValue(newCar).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Car added successfully", Toast.LENGTH_SHORT).show();
                    etMake.setText("");
                    etModel.setText("");
                    etYear.setText("");
                    etColor.setText("");
                });
            }
        });
    }
}
