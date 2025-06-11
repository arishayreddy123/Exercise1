package com.arishay.ex1;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class DeleteCarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarDeleteAdapter adapter;
    private ArrayList<Car> carList;
    private DatabaseReference dbRef;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_car);

        recyclerView = findViewById(R.id.recyclerViewDelete);
        btnBack = findViewById(R.id.btnBack);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carList = new ArrayList<>();
        adapter = new CarDeleteAdapter(this, carList, this::deleteCar);
        recyclerView.setAdapter(adapter);

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

    private void deleteCar(String carId) {
        dbRef.child(carId).removeValue();
    }
}
