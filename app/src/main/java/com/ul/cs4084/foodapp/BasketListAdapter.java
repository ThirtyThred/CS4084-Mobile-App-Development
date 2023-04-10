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

import java.util.List;


public class BasketListAdapter extends RecyclerView.Adapter<BasketListAdapter.ViewHolder> {

    private List<Food> foods;
    private Context context;
    private int lastPosition = -1;
    private String basketButtonTitle;
    private InteractionInterface interactionInterface;

    private static final String TAG = BasketListAdapter.class.getName();


    public BasketListAdapter(List<Food> foods, Context context, String basketButtonTitle, InteractionInterface interactionInterface) {
        this.foods = foods;
        this.context = context;
        this.basketButtonTitle = basketButtonTitle;
        this.interactionInterface = interactionInterface;
    }

    @NonNull
    @Override
    public BasketListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.basket_food_item, parent, false);
        return new ViewHolder(view);
    }

    private int getDrawableResourceId(String name) {
        Resources resources = context.getResources();
        return resources.getIdentifier(name, "drawable", context.getPackageName());
    }

    @Override
    public void onBindViewHolder(@NonNull BasketListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Food food = foods.get(position);
        holder.nameView.setText("("+food.getCount() +") "+ food.getName());
        holder.descriptionView.setText(food.getDescription());
        holder.priceView.setText(food.getPrice());

        Glide.with(context)
                .load(food.getImageUrl())
                .centerCrop()
                .placeholder(getDrawableResourceId("image_"+food.getImagePlaceholder()))
                .into(holder.imageView);


        holder.itemView.setOnClickListener(view -> interactionInterface.onFoodClicked(position));
        holder.addToBasketButton.setOnClickListener(view -> interactionInterface.removeFromBasket(food));
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

            nameView = itemView.findViewById(R.id.basketFoodName);
            descriptionView = itemView.findViewById(R.id.basketFoodDescrition);
            priceView = itemView.findViewById(R.id.basketFoodPrice);
            imageView = itemView.findViewById(R.id.basketFoodImage);
            addToBasketButton = itemView.findViewById(R.id.basketRemoveFromBasketButton);
        }
    }

    public interface InteractionInterface {
        void onFoodClicked(int position);
        void removeFromBasket(Food food);
    }
}
