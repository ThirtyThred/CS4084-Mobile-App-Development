package com.ul.cs4084.foodapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ul.cs4084.foodapp.models.Food;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private List<Food> foods;
    private Context context;
    private int lastPosition = -1;
    private String basketButtonTitle;
    private InteractionInterface interactionInterface;

    private static final String TAG = FoodAdapter.class.getName();


    public FoodAdapter(List<Food> foods, Context context, String basketButtonTitle, InteractionInterface interactionInterface) {
        this.foods = foods;
        this.context = context;
        this.basketButtonTitle = basketButtonTitle;
        this.interactionInterface = interactionInterface;

    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false);
        return new ViewHolder(view);
    }

    // Helper method to get the drawable resource ID given the resource name
    private int getDrawableResourceId(String name) {
        Resources resources = context.getResources();
        return resources.getIdentifier(name, "drawable", context.getPackageName());
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Food food = foods.get(position);
        holder.nameView.setText(food.getName());
        holder.descriptionView.setText(food.getDescription());
        holder.priceView.setText(food.getPrice());


        Glide.with(context)
                .load(food.getImageUrl())
                .centerCrop()
                .placeholder(getDrawableResourceId("image_"+food.getImagePlaceholder()))
                .into(holder.imageView);

        holder.itemView.setOnClickListener(view -> interactionInterface.onFoodClicked(position));
        holder.addToBasketButton.setOnClickListener(view -> interactionInterface.addToBasket(position));
        holder.addToBasketButton.setText(basketButtonTitle);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView, descriptionView, priceView;
        private ImageView imageView;
        private Button addToBasketButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.foodName);
            descriptionView = itemView.findViewById(R.id.foodDescrition);
            priceView = itemView.findViewById(R.id.foodPrice);
            imageView = itemView.findViewById(R.id.foodImage);
            addToBasketButton = itemView.findViewById(R.id.addToBasketButton);

        }
    }

    public interface InteractionInterface {
        void onFoodClicked(int position);
        void addToBasket(int position);
    }
}
