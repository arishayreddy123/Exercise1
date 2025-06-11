package com.arishay.ex1;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class UpdateCarActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUpdate;
    private CarUpdateAdapter adapter;
    private ArrayList<Car> carList;
    private DatabaseReference dbRef;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        recyclerViewUpdate = findViewById(R.id.recyclerViewUpdate);
        btnBack = findViewById(R.id.btnBack);

        recyclerViewUpdate.setLayoutManager(new LinearLayoutManager(this));
        carList = new ArrayList<>();
        adapter = new CarUpdateAdapter(this, carList);
        recyclerViewUpdate.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("cars");
        loadCars();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadCars() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                carList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Car car = snap.getValue(Car.class);
                    if (car != null) carList.add(car);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}
