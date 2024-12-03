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
public class OrderItemDetailsAdapter extends RecyclerView.Adapter<OrderItemDetailsAdapter.PizzaViewHolder>{
    private final List<Pizza> pizzas;
    private final ShareResource shareResource = ShareResource.getInstance();

    public OrderItemDetailsAdapter(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_cardview_order_details, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = pizzas.get(position);

        // set image resource
        holder.imgPizza.setImageResource(getPizzaImageResource(pizza));

        String pizzaStyle = shareResource.getPizzaStyle(pizza);

        List<String> formattedToppings = new ArrayList<>();
        for (Topping topping : pizza.getToppings()) {
            formattedToppings.add(ShareResource.formatTopping(topping.toString()));
        }

        String toppingsList = "Toppings: " + TextUtils.join(", ", formattedToppings);

        holder.tvPizzaStyle.setText(pizzaStyle);
        holder.tvPizzaType.setText(pizza.getName());
        holder.tvPizzaSize.setText("Size: " + pizza.getSize().name());
        holder.tvPizzaToppings.setText(toppingsList);
        holder.tvPizzaPrice.setText(String.format("Price: $%.2f", pizza.price()));
    }

    @Override
    public int getItemCount() {
        return pizzas.size();
    }

    private int getPizzaImageResource(Pizza pizza) {
        String pizzaStyle = shareResource.getPizzaStyle(pizza);
        // Example: Map pizza styles to drawable resources
        if (pizzaStyle.equals("Chicago Style")) {
            return R.drawable.chicago_style_bt; // Ensure drawable exists
        }
        if (pizzaStyle.equals("New York Style")) {
            return R.drawable.ny_style_bt; // Ensure drawable exists
        }
        return R.drawable.pizzeria_logo; // Fallback for unknown styles
    }


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
