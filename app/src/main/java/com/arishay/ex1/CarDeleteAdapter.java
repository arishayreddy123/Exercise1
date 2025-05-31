package com.arishay.ex1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarDeleteAdapter extends RecyclerView.Adapter<CarDeleteAdapter.CarViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(String carId);
    }

    private Context context;
    private List<Car> carList;
    private OnDeleteClickListener deleteClickListener;

    public CarDeleteAdapter(Context context, List<Car> carList, OnDeleteClickListener deleteClickListener) {
        this.context = context;
        this.carList = carList;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car_delete, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        String carDetails = car.make + " " + car.model + " (" + car.year + ") - " + car.color;
        holder.tvCarInfo.setText(carDetails);
        holder.btnDelete.setOnClickListener(v -> deleteClickListener.onDeleteClick(car.id));
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView tvCarInfo;
        ImageButton btnDelete;

        CarViewHolder(View itemView) {
            super(itemView);
            tvCarInfo = itemView.findViewById(R.id.tvCarInfo);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
