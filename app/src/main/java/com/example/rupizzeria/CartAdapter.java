package com.example.rupizzeria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter for displaying cart items in the RecyclerView.
 * This adapter binds data for each pizza in the cart and allows removal of pizzas from the cart.
 * @author Shahnaz Khan, Vy Nguyen
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Pizza> cartItems; // List of pizzas in the cart
    private final OnPizzaRemoveListener onPizzaRemoveListener; // Listener to handle pizza removal

    /**
     * Constructor to initialize the adapter with the list of cart items and the remove listener.
     * @param cartItems List of pizza items in the cart
     * @param onPizzaRemoveListener Listener for removing pizzas from the cart
     */
    public CartAdapter(List<Pizza> cartItems, OnPizzaRemoveListener onPizzaRemoveListener) {
        this.cartItems = cartItems;
        this.onPizzaRemoveListener = onPizzaRemoveListener;
    }

    /**
     * Creates a new ViewHolder to display each cart item.
     * @param parent The parent ViewGroup where the new view will be added
     * @param viewType The type of the view (not used in this case)
     * @return A new instance of CartViewHolder
     */
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the card view layout for each cart item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_cart_item, parent, false);
        return new CartViewHolder(view); // Return a new CartViewHolder
    }

    /**
     * Binds the pizza data to the views in the ViewHolder.
     * @param holder The ViewHolder to bind data to
     * @param position The position of the pizza item in the list
     */
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Pizza pizza = cartItems.get(position);

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

        // Using string resources for formatting
        holder.tvPizzaStyle.setText(context.getString(R.string.pizza_style, ShareResource.getInstance().getPizzaStyle(pizza)));
        holder.tvPizzaTypeAndSize.setText(context.getString(R.string.pizza_type_size, type, size));
        holder.tvPizzaCrust.setText(context.getString(R.string.pizza_crust,crust));
        holder.tvPizzaToppings.setText(context.getString(R.string.pizza_toppings, toppings));
        holder.tvPizzaPrice.setText(context.getString(R.string.pizza_price, pizza.price()));

        // Set the pizza image
        int imageResource = ShareResource.getInstance().getPizzaImageResource(pizza);
        holder.imgPizza.setImageResource(imageResource);

        holder.btnRemovePizza.setOnClickListener(v -> {
            if (onPizzaRemoveListener != null) {
                onPizzaRemoveListener.onPizzaRemove(pizza);
            }
        });
    }

    /**
     * Returns the total number of items in the cart.
     * @return The size of the cartItems list
     */
    @Override
    public int getItemCount() {
        return cartItems.size(); // Return the total number of items in the cart
    }

    /**
     * ViewHolder for each pizza item in the RecyclerView.
     * It holds references to the views that display the pizza details.
     */
    public static class CartViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvPizzaStyle, tvPizzaCrust, tvPizzaTypeAndSize, tvPizzaToppings, tvPizzaPrice;
        private final ImageView imgPizza;
        private final ImageButton btnRemovePizza; // Button to remove pizza from the cart
        private final CardView cardView;

        /**
         * Constructor to initialize the views in the ViewHolder.
         * @param itemView The root view for the item in the RecyclerView
         */
        public CartViewHolder(View itemView) {
            super(itemView);

            // Initialize views by finding them in the itemView
            imgPizza = itemView.findViewById(R.id.img_pizza);
            tvPizzaStyle = itemView.findViewById(R.id.tv_pizza_style);
            tvPizzaCrust = itemView.findViewById(R.id.pizza_crust);
            tvPizzaTypeAndSize = itemView.findViewById(R.id.pizza_type_size);
            tvPizzaToppings = itemView.findViewById(R.id.tv_pizza_toppings);
            tvPizzaPrice = itemView.findViewById(R.id.tv_pizza_price);
            btnRemovePizza = itemView.findViewById(R.id.btn_remove);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    /**
     * Interface to handle pizza removal event from the cart.
     * The activity implementing this interface will define the behavior for removing a pizza.
     */
    public interface OnPizzaRemoveListener {
        void onPizzaRemove(Pizza pizza); // Method to remove pizza from the cart
    }
}