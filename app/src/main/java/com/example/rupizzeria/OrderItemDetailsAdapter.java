package com.example.rupizzeria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter for displaying pizza details in an order.
 */
public class OrderItemDetailsAdapter extends RecyclerView.Adapter<OrderItemDetailsAdapter.PizzaItemsViewHolder> {
    private final List<Pizza> pizzaList;
    private final ShareResource shareResource = ShareResource.getInstance();

    public OrderItemDetailsAdapter(List<Pizza> pizzas) {
        this.pizzaList = pizzas;
    }

    @NonNull
    @Override
    public PizzaItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_details, parent, false);
        return new PizzaItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaItemsViewHolder holder, int position) {
        Pizza pizza = pizzaList.get(position);
        String type = pizza.formatEnumName(pizza.getName());
        String crust = pizza.formatEnumName(pizza.getCrust().name());
        String size = pizza.formatEnumName(pizza.getSize().name());

        // Format toppings as a comma-separated string using StringBuilder
        StringBuilder toppingsBuilder = new StringBuilder();
        for (Topping topping : pizza.getToppings()) {
            if (toppingsBuilder.length() > 0) {
                toppingsBuilder.append(", ");
            }
            toppingsBuilder.append(ShareResource.getInstance().formatToppingName(topping));
        }
        String toppings = toppingsBuilder.length() > 0 ? toppingsBuilder.toString() : "No Toppings";


        // Access context from the itemView of the holder
        Context context = holder.itemView.getContext();

        // Set the pizza image
        int imageResource = shareResource.getInstance().getPizzaImageResource(pizza);
        holder.imgPizza.setImageResource(imageResource);

        holder.tvPizzaStyle.setText(context.getString(R.string.pizza_style, ShareResource.getInstance().getPizzaStyle(pizza)));
        holder.tvPizzaTypeAndSize.setText(context.getString(R.string.pizza_type_size, type, size));
        holder.tvPizzaCrust.setText(context.getString(R.string.pizza_crust, crust));
        holder.tvPizzaToppings.setText(context.getString(R.string.pizza_toppings, toppings));
        holder.tvPizzaPrice.setText(context.getString(R.string.pizza_price, pizza.price()));
    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }

    /**
     * ViewHolder for Pizza items in the RecyclerView.
     */
    static class PizzaItemsViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPizzaStyle, tvPizzaCrust, tvPizzaTypeAndSize, tvPizzaToppings, tvPizzaPrice;
        private final ImageView imgPizza;

        /**
         * Constructor to initialize the views in the ViewHolder.
         * @param itemView The root view for the item in the RecyclerView
         */
        public PizzaItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPizza = itemView.findViewById(R.id.img_pizza);
            tvPizzaStyle = itemView.findViewById(R.id.tv_pizza_style);
            tvPizzaCrust = itemView.findViewById(R.id.tv_pizza_crust);
            tvPizzaTypeAndSize = itemView.findViewById(R.id.tv_pizza_type_size);
            tvPizzaToppings = itemView.findViewById(R.id.tv_pizza_toppings);
            tvPizzaPrice = itemView.findViewById(R.id.tv_pizza_price);
        }
    }
}
