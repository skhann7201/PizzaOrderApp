package com.example.rupizzeria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChicagoPizzaActivity extends AppCompatActivity {

    private ImageView imageView;
    private Spinner spinnerPizzaType;
    private RadioGroup radioGroupPizzaSize;
    private ChipGroup chipGroupToppings;
    private TextView tvPrice;

    private PizzaFactory chicagoPizzaFactory = new ChicagoPizza();
    private List<Pizza> pizzaList;
    private Pizza currentPizza;
    private ShareResource sharedResource = ShareResource.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicago_pizza);

        // Initialize UI components
        imageView = findViewById(R.id.imgv_pizza);
        spinnerPizzaType = findViewById(R.id.spinner_pizzaType);
        radioGroupPizzaSize = findViewById(R.id.radioGroupPizzaSize);
        chipGroupToppings = findViewById(R.id.cg_toppings);
        tvPrice = findViewById(R.id.tv_price);

        // Back button setup
        ImageButton backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> navigateBackToHome());

        //Populate pizza list and spinner
        setupPizzaList();
        setupPizzaSpinner();
        tvPrice.setText("$0.00");

        radioGroupPizzaSize.setOnCheckedChangeListener((group, checkedId) -> {
            // Update price dynamically when size changes
            if (currentPizza != null) {
                updatePrice(currentPizza);
            }
        });

        // Add to cart button
        findViewById(R.id.btn_placeOrder).setOnClickListener(v -> addToCart());
    }

    private void setupPizzaList() {
        pizzaList = Arrays.asList(
                chicagoPizzaFactory.createDeluxe(),
                chicagoPizzaFactory.createMeatzza(),
                chicagoPizzaFactory.createBBQChicken(),
                chicagoPizzaFactory.createBuildYourOwn()
        );// select none
    }


    private void setupPizzaSpinner() {
        // Extract pizza names for spinner
        List<String> pizzaNames = new ArrayList<>();
        pizzaNames.add("Select a pizza"); // Placeholder
        for (Pizza pizza : pizzaList) {
            pizzaNames.add(pizza.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pizzaNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaType.setAdapter(adapter);

        // Initially set position to -1
        spinnerPizzaType.setSelection(0);

        spinnerPizzaType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Placeholder selected
                    currentPizza = null;
                    tvPrice.setText("$0.00");
                    chipGroupToppings.removeAllViews(); // Clear toppings
                    imageView.setImageResource(R.drawable.chicago_default); // Default image
                } else {
                    // Adjust position for actual pizzas (offset by 1 due to placeholder)
                    currentPizza = pizzaList.get(position - 1);
                    updatePizzaImage(currentPizza);
                    updateToppings(currentPizza);
                    updatePrice(currentPizza);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Reset to default
                tvPrice.setText("$0.00");
            }
        });
    }

    /**
     * Determines the selected size from the radio group.
     *
     * @return The selected Size (SMALL, MEDIUM, LARGE) or null if no size is selected.
     */
    private Size getSelectedSize() {
        int selectedSizeId = radioGroupPizzaSize.getCheckedRadioButtonId();
        if (selectedSizeId == R.id.rb_small) return Size.SMALL;
        if (selectedSizeId == R.id.rb_medium) return Size.MEDIUM;
        if (selectedSizeId == R.id.rb_large) return Size.LARGE;
        return null; // No size selected
    }


    private void updatePizzaImage(Pizza pizza) {
        int imageRes = R.drawable.chicago_default; // Default image
        if (pizza instanceof Deluxe) imageRes = R.drawable.chicago_deluxe;
        else if (pizza instanceof Meatzza) imageRes = R.drawable.chicago_meatzza;
        else if (pizza instanceof BBQChicken) imageRes = R.drawable.chicago_bbqchicken;
        else if (pizza instanceof BuildYourOwn) imageRes = R.drawable.chicago_byo;

        imageView.setImageResource(imageRes);
    }

    private void updateToppings(Pizza pizza) {
        chipGroupToppings.removeAllViews(); // Clear existing chips
        List<Topping> allToppings = Arrays.asList(Topping.values());

        for (Topping topping : allToppings) {
            Chip chip = new Chip(this);
            chip.setText(formatToppingName(topping));
            chip.setCheckable(true);
            chip.setChecked(pizza.getToppings().contains(topping));

            if (pizza instanceof BuildYourOwn) {
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        ((BuildYourOwn) pizza).addTopping(topping);
                    } else {
                        ((BuildYourOwn) pizza).removeTopping(topping);
                    }
                    updatePrice(pizza);
                });
            } else {
                chip.setEnabled(false); // Disable chips for non-BuildYourOwn pizzas
            }

            chipGroupToppings.addView(chip);
        }
    }

    private void updatePrice(Pizza pizza) {
        Size size = getSelectedSize();
        if (size == null) {
            tvPrice.setText("$0.00");
            return;
        }

        pizza.setSize(size);
        tvPrice.setText(String.format("$%.2f", pizza.price()));
    }

    private void navigateBackToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void addToCart() {
        Size size = getSelectedSize();
        if (size == null) {
            Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show();
            return;
        }

        currentPizza.setSize(size);
        sharedResource.addPizzaToCart(currentPizza, "Chicago Style");
        Toast.makeText(this, currentPizza.getName() + " added to cart.", Toast.LENGTH_SHORT).show();

        spinnerPizzaType.setSelection(0);
        radioGroupPizzaSize.clearCheck();
        tvPrice.setText("$0.00");
        updateToppings(pizzaList.get(0));


    }

    private String formatToppingName(Topping topping) {
        String name = topping.name().toLowerCase().replace('_', ' ');
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}