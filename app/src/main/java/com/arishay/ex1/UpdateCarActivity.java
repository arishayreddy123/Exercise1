package com.arishay.ex1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class UpdateCarActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUpdate;
    private ArrayList<Car> carList;
    private CarUpdateAdapter adapter;
    private DatabaseReference dbRef;
    private ImageButton btnBackUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        recyclerViewUpdate = findViewById(R.id.recyclerViewUpdate);
        recyclerViewUpdate.setLayoutManager(new LinearLayoutManager(this));
        carList = new ArrayList<>();
        adapter = new CarUpdateAdapter(this, carList);
        recyclerViewUpdate.setAdapter(adapter);

        btnBackUpdate = findViewById(R.id.btnBackUpdate);
        btnBackUpdate.setOnClickListener(v -> finish());

        dbRef = FirebaseDatabase.getInstance().getReference("cars");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                carList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Car car = snap.getValue(Car.class);
                    carList.add(car);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(UpdateCarActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showUpdateDialog(Car car) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_car, null);
        builder.setView(dialogView);

        EditText etMake = dialogView.findViewById(R.id.etMakeUpdate);
        EditText etModel = dialogView.findViewById(R.id.etModelUpdate);
        EditText etYear = dialogView.findViewById(R.id.etYearUpdate);
        EditText etColor = dialogView.findViewById(R.id.etColorUpdate);
        ImageButton btnUpdateCar = dialogView.findViewById(R.id.btnUpdateCar);
        ImageButton btnBackDialog = dialogView.findViewById(R.id.btnBackDialog);

        etMake.setText(car.make);
        etModel.setText(car.model);
        etYear.setText(String.valueOf(car.year));
        etColor.setText(car.color);

        AlertDialog dialog = builder.create();

        btnBackDialog.setOnClickListener(v -> dialog.dismiss());

        btnUpdateCar.setOnClickListener(v -> {
            String make = etMake.getText().toString().trim();
            String model = etModel.getText().toString().trim();
            String yearStr = etYear.getText().toString().trim();
            String color = etColor.getText().toString().trim();

            if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || color.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int year = Integer.parseInt(yearStr);
            Car updatedCar = new Car(car.id, make, model, year, color);
            dbRef.child(car.id).setValue(updatedCar);
            dialog.dismiss();
            Toast.makeText(this, "Car updated successfully", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }
}
