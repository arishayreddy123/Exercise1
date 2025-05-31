package com.arishay.ex1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private final ArrayList<Car> cars;

    public CarAdapter(ArrayList<Car> cars) {
        this.cars = cars;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);
        holder.tvMake.setText(car.make);
        holder.tvModel.setText(car.model);
        holder.tvYear.setText(String.valueOf(car.year));
        holder.tvColor.setText(car.color);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView tvMake, tvModel, tvYear, tvColor;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMake = itemView.findViewById(R.id.tvMake);
            tvModel = itemView.findViewById(R.id.tvModel);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvColor = itemView.findViewById(R.id.tvColor);
        }
    }
}
