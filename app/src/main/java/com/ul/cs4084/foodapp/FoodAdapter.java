package com.ul.cs4084.foodapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ul.cs4084.foodapp.models.Food;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {


    private ArrayList<Food> foods;
    private Context context;
    private int lastPosition = -1;
    private InteractionInterface interactionInterface;

    public FoodAdapter(ArrayList<Food> foods, Context context, InteractionInterface interactionInterface) {
        this.foods = foods;
        this.context = context;
        this.interactionInterface = interactionInterface;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Food food = foods.get(position);
        holder.nameView.setText(food.getName());
        holder.descriptionView.setText(food.getDescription());
        holder.priceView.setText(food.getPrice());
        Picasso.get()
                .load(food.getImageUrl())
                .placeholder(R.drawable.img)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interactionInterface.onFoodClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView, descriptionView, priceView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.foodName);
            descriptionView = itemView.findViewById(R.id.foodDescrition);
            priceView = itemView.findViewById(R.id.foodPrice);
            imageView = itemView.findViewById(R.id.foodImage);
        }
    }

    public interface InteractionInterface {
        void onFoodClicked(int position);
    }
}
