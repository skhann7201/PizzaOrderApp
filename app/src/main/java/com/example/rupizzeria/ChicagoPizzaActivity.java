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
                // update the chipgroup (enable )
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
        );
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

        // Initially set position to 0
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
                    // Create a new instance of the selected pizza
                    switch (position) {
                        case 1:
                            currentPizza = chicagoPizzaFactory.createDeluxe();
                            break;
                        case 2:
                            currentPizza = chicagoPizzaFactory.createMeatzza();
                            break;
                        case 3:
                            currentPizza = chicagoPizzaFactory.createBBQChicken();
                            break;
                        case 4:
                            currentPizza = chicagoPizzaFactory.createBuildYourOwn();
                            break;
                        default:
                            currentPizza = null; // Safety fallback
                    }

                    if (currentPizza != null) {
                        updatePizzaImage(currentPizza);
                        updateToppings(currentPizza);
                        updatePrice(currentPizza);
                    }
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
    public void clearToppings() {
        currentPizza.getToppings().clear();
    }

    private void highlightChip(Chip chip) {
        chip.setChipBackgroundColorResource(R.color.chip_checked); // Highlight background color
        chip.setChipStrokeWidth(2f); // Add border
    }

    private void resetChipStyle(Chip chip) {
        chip.setChipBackgroundColorResource(R.color.chip_unchecked); // Default background color
        chip.setChipStrokeWidth(0f); // No border
    }


    private void updateToppings(Pizza pizza) {
        chipGroupToppings.removeAllViews(); // Clear existing chips
        List<Topping> allToppings = Arrays.asList(Topping.values());

        // Clear toppings if it's a fresh "Build Your Own" pizza
        if (pizza instanceof BuildYourOwn) {
            clearToppings(); // Ensure toppings are cleared
        }

        for (Topping topping : allToppings) {
            Chip chip = new Chip(this);
            chip.setText(formatToppingName(topping));

            if(pizza.getToppings().contains(topping)){
                highlightChip(chip);
                chip.setEnabled(true);
                chip.setCheckable(false);
            }else {
                resetChipStyle(chip);
                chip.setEnabled(false);
            }

            if (pizza instanceof BuildYourOwn) {
                chip.setEnabled(true); // Enable chips for BYO
                chip.setCheckable(true); // Allow selection for BYO
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        if (pizza.getToppings().size() < 7) {
                            ((BuildYourOwn) pizza).addTopping(topping);
                            highlightChip(chip);
                            chip.setCloseIconVisible(true);
                        } else {
                            chip.setChecked(false); // Revert check
                            Toast.makeText(this, "Maximum 7 toppings allowed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ((BuildYourOwn) pizza).removeTopping(topping);
                        resetChipStyle(chip);
                        chip.setCloseIconVisible(false); // Hide the "X"
                    }
                    updatePrice(pizza);
                });
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
        currentPizza = null;


    }

    private String formatToppingName(Topping topping) {
        String name = topping.name().toLowerCase().replace('_', ' ');
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}