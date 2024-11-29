package com.example.rupizzeria;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

public class ChicagoPizzaActivity extends AppCompatActivity {

    private ViewPager2 vp_pizza;
    private TextView tv_price;
    private ChipGroup cg_toppings;


    private PizzaFactory chicagoPizzaFactory = new ChicagoPizza();
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicago_pizza);

        // Initialize UI elements
        vp_pizza = findViewById(R.id.vp_pizzaImage);
        tv_price = findViewById(R.id.tv_price);
        cg_toppings = findViewById(R.id.cg_toppings);

        // Create Pizza objects for the ViewPager
        List<Pizza> pizzaList = Arrays.asList(
                chicagoPizzaFactory.createDeluxe(),
                chicagoPizzaFactory.createMeatzza(),
                chicagoPizzaFactory.createBBQChicken(),
                chicagoPizzaFactory.createBuildYourOwn()
        );

        // Set up ViewPager2 adapter
        vp_pizza = findViewById(R.id.vp_pizzaImage);
        vp_pizza.setAdapter(new RecyclerView.Adapter<PizzaViewHolder>() {

            @NonNull
            @Override
            public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.pizza_image_item, parent, false);
                return new PizzaViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
                Log.d("ChicagoPizzaActivity", "Binding position: " + position);

                Pizza pizza = pizzaList.get(position);
                // Bind image resource
                holder.imageView.setImageResource(getPizzaImageResource(pizza));
                holder.textView.setText(pizza.getName());

                // Highlight the current selection
                if (currentPosition == position) {
                    holder.itemView.setScaleX(1.2f); // Make it slightly larger
                    holder.itemView.setScaleY(1.2f);
                    holder.itemView.setElevation(8f); // Add a shadow
                } else {
                    holder.itemView.setScaleX(1.0f);
                    holder.itemView.setScaleY(1.0f);
                    holder.itemView.setElevation(0f);
                }
            }

            @Override
            public int getItemCount() {
                // Total number of items
                return pizzaList.size();
            }
        }); // end adapter

        // Listen for page changes
        vp_pizza.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                //updateUIForSelectedPizza(selectedPizza);
                vp_pizza.getAdapter().notifyDataSetChanged();
            }
        });

        applyCarouselEffect(vp_pizza);
        // Initialize the UI for the first pizza
        //updateUIForSelectedPizza(pizzaList.get(0));
    } // end onCreate

    /**
     * Apply carousel effect with scaling and spacing for ViewPager2.
     *
     * @param viewPager The ViewPager2 instance.
     */
    private void applyCarouselEffect(ViewPager2 viewPager) {
        // Enable peeking and scaling
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);

        // Set padding to allow peeking
        int horizontalMargin = getResources().getDimensionPixelSize(R.dimen.viewpager_item_margin);
        viewPager.setPadding(horizontalMargin, 0, horizontalMargin, 0);

        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        recyclerView.setClipToPadding(false);

        // Add a PageTransformer for scaling effect
        viewPager.setPageTransformer((page, position) -> {
            float absPos = Math.abs(position);
            float scale = 0.85f + (1 - absPos) * 0.15f; // Scale pages
            page.setScaleY(scale);
        });

        // Add margin between items
        int itemMarginPx = getResources().getDimensionPixelSize(R.dimen.viewpager_item_margin);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = itemMarginPx / 2;
                outRect.right = itemMarginPx / 2;
            }
        });
    }

    /**
     * Update the UI based on the selected pizza.
     *
     * @param pizza The selected pizza.
     */
//    private void updateUIForSelectedPizza(Pizza pizza) {
//        // Clear toppings and add fixed toppings for the selected pizza
//        cg_toppings.removeAllViews();
//        for (Topping topping : pizza.getToppings()) {
//            Chip chip = new Chip(this);
//            chip.setText(topping.name());
//            chip.setClickable(false); // Fixed toppings are not clickable
//            cg_toppings.addView(chip);
//        }
//
//        // Update the price
//        tv_price.setText("$" + String.format("%.2f", pizza.price()));
//    }

    /**
     * Get the image resource ID for a given pizza.
     *
     * @param pizza The pizza object.
     * @return The image resource ID.
     */
    private int getPizzaImageResource(Pizza pizza) {
        if (pizza instanceof Deluxe) return R.drawable.chicago_deluxe;
        if (pizza instanceof Meatzza) return R.drawable.chicago_meatzza;
        if (pizza instanceof BBQChicken) return R.drawable.chicago_bbqchicken;
        if (pizza instanceof BuildYourOwn) return R.drawable.chicago_byo;
        return R.drawable.chicago_style_bt; // Fallback image
    }

    // ViewHolder class for the ViewPager2
    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgv_pizzaImages);
            textView = itemView.findViewById(R.id.tv_pizzaName);
        }

    }
}