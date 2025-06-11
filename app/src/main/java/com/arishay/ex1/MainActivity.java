package com.arishay.ex1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAddCar, btnDeleteCar, btnUpdateCar, btnSearchCar, btnFetchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnAddCar = findViewById(R.id.btnAddCar);
        btnDeleteCar = findViewById(R.id.btnDeleteCar);
        btnUpdateCar = findViewById(R.id.btnUpdateCar);
        btnSearchCar = findViewById(R.id.btnSearchCar);
        btnFetchActivity = findViewById(R.id.btnFetchActivity);

        btnAddCar.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddCarActivity.class)));

        btnDeleteCar.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, DeleteCarActivity.class)));

        btnUpdateCar.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, UpdateCarActivity.class)));

        btnSearchCar.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SearchCarActivity.class)));

        btnFetchActivity.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, FetchPostsActivity.class)));
    }
}
