package com.arishay.ex1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
        btnBackUpdate = findViewById(R.id.btnBackUpdate);

        recyclerViewUpdate.setLayoutManager(new LinearLayoutManager(this));
        carList = new ArrayList<>();
        adapter = new CarUpdateAdapter(this, carList);
        recyclerViewUpdate.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("cars");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Car car = snap.getValue(Car.class);
                    if (car != null) {
                        carList.add(car);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateCarActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnBackUpdate.setOnClickListener(v -> finish());
    }
}
