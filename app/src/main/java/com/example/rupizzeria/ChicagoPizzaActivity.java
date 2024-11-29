package com.example.rupizzeria;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
    private List<Pizza> pizzaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicago_pizza);

        // Initialize UI elements
        vp_pizza = findViewById(R.id.vp_pizzaImage);
        tv_price = findViewById(R.id.tv_price);
        cg_toppings = findViewById(R.id.cg_toppings);

        // Populate pizzaList
        setupPizzaList();

        // set up view pager


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

                // Highlight current selection
                if (currentPosition == position) {
                    holder.cardView.setCardElevation(12f); // Elevated for selected
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ChicagoPizzaActivity.this, R.color.primary_orange)); // Highlighted
                } else {
                    holder.cardView.setCardElevation(8f); // Default elevation
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ChicagoPizzaActivity.this, android.R.color.white)); // Default background
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
                //reload chips
                loadToppingsToChips();
                // Reset the RadioGroup selection
                RadioGroup radioGroupSize = findViewById(R.id.radioGroupPizzaSize);
                radioGroupSize.clearCheck(); // This will clear any selected radio button
                tv_price.setText("$0.00");
            }
        });

        applyCarouselEffect(vp_pizza);
        loadToppingsToChips(); // initialize chips for the first pizza

        setupSizeSelection();
        // initialize back button
        ImageButton backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> navigateBackToHome());

    } // end onCreate

    private void setupPizzaList() {
        pizzaList = Arrays.asList(
                chicagoPizzaFactory.createDeluxe(),
                chicagoPizzaFactory.createMeatzza(),
                chicagoPizzaFactory.createBBQChicken(),
                chicagoPizzaFactory.createBuildYourOwn()
        );
    }

    private void setupSizeSelection(){
        // set of size selection
        RadioGroup radioGroupSize = findViewById(R.id.radioGroupPizzaSize);

        radioGroupSize.setOnCheckedChangeListener((group,checkedId) ->
                handleSizeSelection(checkedId));

    }

    private void handleSizeSelection(int checkedId){
        Size selectedSize; // Declare a variable to store the selected size.

        if (checkedId == R.id.rb_small) {
            selectedSize = Size.SMALL;
        } else if (checkedId == R.id.rb_medium) {
            selectedSize = Size.MEDIUM;
        } else if (checkedId == R.id.rb_large) {
            selectedSize = Size.LARGE;
        } else {
            selectedSize = null; // Default or error case, if needed.
        }

        if (selectedSize != null) {
            Pizza selectedPizza = pizzaList.get(currentPosition);
            selectedPizza.setSize(selectedSize); // Assuming your Pizza class has a setSize method
            updatePrice(selectedPizza);
        }
    }
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
        CardView cardView;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_pizzaImages);
            imageView = itemView.findViewById(R.id.imgv_pizzaImages);
            textView = itemView.findViewById(R.id.tv_pizzaName);
        }

    }

    /**
     * Formats the Topping enum name to be user-friendly.
     * Example: "GREEN_PEPPER" -> "Green Pepper".
     *
     * @param topping The Topping enum value.
     * @return A formatted string representation.
     */
    private String formatToppingName(Topping topping) {
        String name = topping.name().toLowerCase().replace('_', ' '); // Convert to lowercase and replace underscores
        String[] words = name.split(" "); // Split words by space
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            formattedName.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1)) // Capitalize the first letter of each word
                    .append(" ");
        }

        return formattedName.toString().trim();
    }

    private void loadToppingsToChips() {
        // Clear existing chips
        cg_toppings.removeAllViews();

        List<Topping> allToppings = Arrays.asList(Topping.values());
        Pizza selectedPizza = pizzaList.get(currentPosition);

        for (Topping topping : allToppings) {
            Chip chip = new Chip(this);
            chip.setText(formatToppingName(topping));
            chip.setCheckable(false);
            chip.setClickable(false);
            chip.setCloseIconVisible(false); // Hide "X" by default
            chip.setChipStrokeWidth(0f); // No border initially
            chip.setChipStrokeColor(null); // Ensure no stroke color
            chip.setChipBackgroundColorResource(R.color.chip_unchecked); // Add a color selector for checked/unchecked states


            // Highlight chips based on fixed toppings
            if (selectedPizza.getToppings().contains(topping)) {
                chip.setChecked(true);
                chip.setCheckable(false);
                chip.setClickable(false);
                chip.setChipStrokeWidth(2f); // Add border when checked
                chip.setChipBackgroundColorResource(R.color.chip_checked);
            }

            // Show "X" for removable toppings only for Build Your Own pizza
            if (selectedPizza instanceof BuildYourOwn) {
                chip.setCloseIconVisible(false);
                chip.setClickable(true);
                chip.setCheckable(true);
                chip.setChipBackgroundColorResource(chip.isChecked() ? R.color.chip_checked : R.color.chip_unchecked);

                chip.setOnCloseIconClickListener(v -> {
                    chip.setChecked(false); // Uncheck the chip
                    chip.setChipBackgroundColorResource(R.color.chip_unchecked); // Revert background color
                    chip.setChipStrokeWidth(0f);
                    chip.setCloseIconVisible(false); // Hide the "X"
                    selectedPizza.removeTopping(topping); // Remove topping from the pizza
                    Toast.makeText(this, topping.name() + " removed", Toast.LENGTH_SHORT).show(); // Debug message
                });

                // Handle topping selection/deselection
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        // Check if the topping limit has been reached
                        if (selectedPizza.getToppings().size() >= 7) {
                            Toast.makeText(this, "Cannot add more than 7 toppings!", Toast.LENGTH_SHORT).show();
                            chip.setChecked(false); // Revert the selection
                        }else {
                            selectedPizza.addTopping(topping);
                            chip.setChipBackgroundColorResource(R.color.chip_checked); // Highlight the chip
                            chip.setCloseIconVisible(true); // Show "X"
                            chip.setChipStrokeWidth(2f);
                            Toast.makeText(this, topping.name() + " added", Toast.LENGTH_SHORT).show(); // Debug message
                        }

                    } else {
                        chip.setChipBackgroundColorResource(R.color.chip_unchecked); // Revert background color
                        chip.setCloseIconVisible(false); // Hide the "X"
                        chip.setChipStrokeWidth(0f);
                        selectedPizza.removeTopping(topping); // Remove topping from the pizza
                        Toast.makeText(this, topping.name() + " removed", Toast.LENGTH_SHORT).show(); // Debug message
                    }
                    updatePrice(selectedPizza);
                });
            }
            cg_toppings.addView(chip);
        }
    }




    private void updatePrice(Pizza pizza) {
        tv_price.setText("$" + String.format("%.2f", pizza.price()));
    }

    private void handleBuildYourOwn(){

    }

    private void navigateBackToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Optional: Clears other activities
        startActivity(intent);
        finish(); // End current activity
    }

}