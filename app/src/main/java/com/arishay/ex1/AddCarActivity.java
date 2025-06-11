package com.arishay.ex1;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.UUID;

public class AddCarActivity extends AppCompatActivity {

    private EditText etMake, etModel, etYear, etColor;
    private ImageButton btnAdd;
    private TextView tvAdd;
    private ImageButton btnBack;

    private DatabaseReference dbRef;

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

        dbRef = FirebaseDatabase.getInstance().getReference("cars");

        btnAdd.setOnClickListener(v -> addCar());
        tvAdd.setOnClickListener(v -> addCar());

        btnBack.setOnClickListener(v -> finish());
    }

    private void addCar() {
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

        String id = UUID.randomUUID().toString();
        Car car = new Car(id, make, model, year, color);

        dbRef.child(id).setValue(car)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Car added successfully", Toast.LENGTH_SHORT).show();
                    etMake.setText("");
                    etModel.setText("");
                    etYear.setText("");
                    etColor.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add car", Toast.LENGTH_SHORT).show());
    }
}
