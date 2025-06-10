package com.arishay.ex1;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CarUpdateAdapter extends RecyclerView.Adapter<CarUpdateAdapter.CarViewHolder> {

    private final Context context;
    private final List<Car> carList;

    public CarUpdateAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.tvLine1.setText(car.make + " - " + car.model);
        holder.tvLine2.setText("Year: " + car.year + ", Color: " + car.color);

        holder.itemView.setOnClickListener(v -> showUpdateDialog(car));
    }

    private void showUpdateDialog(Car car) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update_car, null);

        EditText etMake = view.findViewById(R.id.etMakeUpdate);
        EditText etModel = view.findViewById(R.id.etModelUpdate);
        EditText etYear = view.findViewById(R.id.etYearUpdate);
        EditText etColor = view.findViewById(R.id.etColorUpdate);
        ImageButton btnUpdate = view.findViewById(R.id.btnUpdateCar);
        ImageButton btnBack = view.findViewById(R.id.btnBackDialog);

        etMake.setText(car.make);
        etModel.setText(car.model);
        etYear.setText(String.valueOf(car.year));
        etColor.setText(car.color);

        AlertDialog dialog = builder.setView(view).create();

        btnBack.setOnClickListener(v -> dialog.dismiss());

        btnUpdate.setOnClickListener(v -> {
            String make = etMake.getText().toString().trim();
            String model = etModel.getText().toString().trim();
            String yearStr = etYear.getText().toString().trim();
            String color = etColor.getText().toString().trim();

            if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || color.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Year must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            Car updatedCar = new Car(car.id, make, model, year, color);

            FirebaseDatabase.getInstance().getReference("cars")
                    .child(car.id)
                    .setValue(updatedCar)
                    .addOnSuccessListener(unused -> {
                        int index = carList.indexOf(car);
                        if (index != -1) {
                            carList.set(index, updatedCar);
                            notifyItemChanged(index);
                        }
                        Toast.makeText(context, "Car updated successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
                    });
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView tvLine1, tvLine2;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLine1 = itemView.findViewById(android.R.id.text1);
            tvLine2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
