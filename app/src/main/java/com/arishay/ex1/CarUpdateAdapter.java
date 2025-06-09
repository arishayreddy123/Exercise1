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

        EditText editMake = view.findViewById(R.id.editMake);
        EditText editModel = view.findViewById(R.id.editModel);
        EditText editYear = view.findViewById(R.id.editYear);
        EditText editColor = view.findViewById(R.id.editColor);
        ImageButton btnUpdate = view.findViewById(R.id.btnDialogUpdate);
        ImageButton btnBack = view.findViewById(R.id.btnBackDialog);

        editMake.setText(car.make);
        editModel.setText(car.model);
        editYear.setText(String.valueOf(car.year));
        editColor.setText(car.color);

        AlertDialog dialog = builder.setView(view).create();

        btnBack.setOnClickListener(v -> dialog.dismiss());

        btnUpdate.setOnClickListener(v -> {
            String make = editMake.getText().toString().trim();
            String model = editModel.getText().toString().trim();
            String yearStr = editYear.getText().toString().trim();
            String color = editColor.getText().toString().trim();

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
                    .addOnSuccessListener(aVoid -> {
                        int position = carList.indexOf(car);
                        if (position != -1) {
                            carList.set(position, updatedCar);
                            notifyItemChanged(position);
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
