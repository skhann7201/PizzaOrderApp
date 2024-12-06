package com.example.rupizzeria;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying pizza details in an order.
 */
public class OrderItemDetailsAdapter extends RecyclerView.Adapter<OrderItemDetailsAdapter.PizzaViewHolder> {
    private final List<Pizza> pizzas;
    private final ShareResource shareResource = ShareResource.getInstance();

    public OrderItemDetailsAdapter(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_details, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = pizzas.get(position);

        // Set pizza image resource based on style and type
        holder.imgPizza.setImageResource(getPizzaImageResource(pizza));

        // Get the formatted pizza style
        String pizzaStyle = shareResource.getPizzaStyle(pizza);

        // Format the toppings
        List<String> formattedToppings = new ArrayList<>();
        for (Topping topping : pizza.getToppings()) {
            formattedToppings.add(shareResource.formatToppingName(topping)); // Use the instance method
        }
        String toppingsList = "Toppings: " + (formattedToppings.isEmpty() ? "No Toppings" : TextUtils.join(", ", formattedToppings));

        // Set values to the views
        holder.tvPizzaStyle.setText(pizzaStyle);
        holder.tvPizzaType.setText("Type: " + pizza.getName());
        holder.tvPizzaSize.setText("Size: " + pizza.getSize().name());
        holder.tvPizzaToppings.setText(toppingsList);
        holder.tvPizzaPrice.setText(String.format("Price: $%.2f", pizza.price()));
    }

    @Override
    public int getItemCount() {
        return pizzas.size();
    }

    /**
     * Maps the pizza style and type to the corresponding image resource.
     *
     * @param pizza The pizza object.
     * @return The drawable resource ID for the pizza image.
     */
    private int getPizzaImageResource(Pizza pizza) {
        String pizzaStyle = shareResource.getPizzaStyle(pizza);
        String pizzaType = pizza.getName();

        // Map Chicago Style pizza types to images
        if (pizzaStyle.equals("Chicago Style")) {
            switch (pizzaType) {
                case "Deluxe":
                    return R.drawable.chicago_deluxe;
                case "Meatzza":
                    return R.drawable.chicago_meatzza;
                case "BBQ Chicken":
                    return R.drawable.chicago_bbqchicken;
                case "Build Your Own":
                    return R.drawable.chicago_byo;
                default:
                    return R.drawable.chicago_default;
            }
        }

        // Map New York Style pizza types to images
        if (pizzaStyle.equals("New York Style")) {
            switch (pizzaType) {
                case "Deluxe":
                    return R.drawable.ny_deluxe;
                case "Meatzza":
                    return R.drawable.ny_meatzza;
                case "BBQ Chicken":
                    return R.drawable.ny_bbqchicken;
                case "Build Your Own":
                    return R.drawable.ny_byo;
                default:
                    return R.drawable.ny_default;
            }
        }

        // Fallback to a default image
        return R.drawable.pizzeria_logo;
    }

    /**
     * ViewHolder for Pizza items in the RecyclerView.
     */
    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        TextView tvPizzaStyle, tvPizzaType, tvPizzaSize, tvPizzaToppings, tvPizzaPrice;
        ImageView imgPizza;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPizza = itemView.findViewById(R.id.img_pizza);
            tvPizzaStyle = itemView.findViewById(R.id.tv_pizza_style);
            tvPizzaType = itemView.findViewById(R.id.tv_pizza_type);
            tvPizzaSize = itemView.findViewById(R.id.tv_pizza_size);
            tvPizzaToppings = itemView.findViewById(R.id.tv_pizza_toppings);
            tvPizzaPrice = itemView.findViewById(R.id.tv_pizza_price);
        }
    }
}
