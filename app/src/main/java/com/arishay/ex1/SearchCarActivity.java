package com.arishay.ex1;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

public class SearchCarActivity extends AppCompatActivity {

    private EditText etSearchMake;
    private Button btnSearch, btnBackSearch;
    private TextView tvSearchResult;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car);

        etSearchMake = findViewById(R.id.etSearchMake);
        btnSearch = findViewById(R.id.btnSearch);
        btnBackSearch = findViewById(R.id.btnBackSearch);
        tvSearchResult = findViewById(R.id.tvSearchResult);

        dbRef = FirebaseDatabase.getInstance().getReference("cars");

        btnSearch.setOnClickListener(v -> {
            String makeInput = etSearchMake.getText().toString().trim();
            if (TextUtils.isEmpty(makeInput)) {
                etSearchMake.setError("Enter a car make");
            } else {
                searchCar(makeInput);
            }
        });

        btnBackSearch.setOnClickListener(v -> finish());
    }

    private void searchCar(String makeInput) {
        String makeLower = makeInput.toLowerCase();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder resultBuilder = new StringBuilder();
                boolean found = false;

                for (DataSnapshot carSnap : snapshot.getChildren()) {
                    String make = carSnap.child("make").getValue(String.class);
                    String model = carSnap.child("model").getValue(String.class);
                    String year = carSnap.child("year").getValue(String.class);

                    if (make != null && make.toLowerCase().equals(makeLower)) {
                        found = true;
                        resultBuilder.append("Make: ").append(make).append("\n");
                        resultBuilder.append("Model: ").append(model != null ? model : "N/A").append("\n");
                        resultBuilder.append("Year: ").append(year != null ? year : "N/A").append("\n\n");
                    }
                }

                if (found) {
                    tvSearchResult.setText(resultBuilder.toString());
                } else {
                    tvSearchResult.setText("No cars found for make: " + makeInput);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchCarActivity.this, "Failed to read data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
