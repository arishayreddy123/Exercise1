package com.arishay.ex1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAddCar, btnDeleteCar, btnUpdateCar, btnSearchCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        btnAddCar = findViewById(R.id.btnAddCar);
        btnDeleteCar = findViewById(R.id.btnDeleteCar);
        btnUpdateCar = findViewById(R.id.btnUpdateCar);
        btnSearchCar = findViewById(R.id.btnSearchCar);

        btnAddCar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddCarActivity.class);
            startActivity(intent);
        });

        btnDeleteCar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DeleteCarActivity.class);
            startActivity(intent);
        });

        btnUpdateCar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UpdateCarActivity.class);
            startActivity(intent);
        });

        btnSearchCar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchCarActivity.class);
            startActivity(intent);
        });
    }
}
