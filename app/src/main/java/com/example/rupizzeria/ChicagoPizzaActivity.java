package com.example.rupizzeria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

/**
 * Activity for customizing and ordering Chicago-style pizzas.
 * @author Vy Nguyen
 */
public class ChicagoPizzaActivity extends AppCompatActivity {
    // UI Components
    private ImageView imageView;
    private Spinner spinnerPizzaType;
    private RadioGroup radioGroupPizzaSize;
    private ChipGroup chipGroupToppings;
    private TextView tvPrice;
    private ImageButton backButton;
    private Button placeOrder;
    private TextView tvCrust;

    // Data
    private final PizzaFactory chicagoPizzaFactory = new ChicagoPizza();
    private List<Pizza> pizzaList; // list of available pizza options
    private Pizza currentPizza; // tracking current selected pizza
    private static final int MAX_TOPPINGS = 7;
    private final ShareResource sharedResource = ShareResource.getInstance(); //Shared resource for data

    /**
     * Lifecycle method called when the activity is created.
     * Initializes UI components, listeners, and data.
     *
     * @param savedInstanceState Saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicago_pizza);

        findID();
        setupListeners();
        setupPizzaList();
        setupPizzaSpinner();
        populateToppingsChips();

        tvPrice.setText(getString(R.string.pizza_price, 0.00));
    }

    /**
     * Finds and initializes all UI Components ID
     */
    private void findID() {
        imageView = findViewById(R.id.imgv_pizza);
        spinnerPizzaType = findViewById(R.id.spinner_pizzaType);
        radioGroupPizzaSize = findViewById(R.id.radioGroupPizzaSize);
        chipGroupToppings = findViewById(R.id.cg_toppings);
        tvPrice = findViewById(R.id.tv_price);
        backButton = findViewById(R.id.btn_back);
        placeOrder = findViewById(R.id.btn_placeOrder);
        tvCrust = findViewById(R.id.tv_pizzaCrust);

    }

    /**
     * Sets up listeners for UI interactions
     */
    private void setupListeners() {
        // Radio Group for size
        radioGroupPizzaSize.setOnCheckedChangeListener((group, checkedId) -> {
            // Update price dynamically when size changes
            if (currentPizza != null) {
                // update the chipGroup (enable )
                updateToppingsChips();
                updatePrice();
            }
        });

        // Place Order button
        placeOrder.setOnClickListener(v -> addToCart());
        // Back button
        backButton.setOnClickListener(v -> navigateBackToHome());
    }

    /**
     * Sets up the list of available pizzas for Chicago style.
     */
    private void setupPizzaList() {
        pizzaList = Arrays.asList(
                chicagoPizzaFactory.createDeluxe(),
                chicagoPizzaFactory.createMeatzza(),
                chicagoPizzaFactory.createBBQChicken(),
                chicagoPizzaFactory.createBuildYourOwn()
        );
    }

    /**
     * Loads the pizza type spinner with pizza names.
     */
    private void setupPizzaSpinner() {
        // Extract pizza names for spinner
        List<String> pizzaNames = new ArrayList<>();
        pizzaNames.add("Select a pizza"); // Placeholder
        pizzaList.forEach(pizza -> pizzaNames.add(pizza.getName()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, pizzaNames);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerPizzaType.setAdapter(adapter);

        spinnerPizzaType.setSelection(0); // Default option for spinner
        spinnerPizzaType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handlePizzaSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Reset to default
                resetUIState();
            }
        });
    }

    /**
     * Handles the logic for selecting a pizza from the spinner.
     *
     * @param position The index of the selected pizza.
     */
    private void handlePizzaSelection(int position) {
        if (position == 0) {
            resetUIState();
            return;
        }
        radioGroupPizzaSize.clearCheck(); // Clear size selection
        resetChips();

        // Create a fresh instance of the selected pizza
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
                currentPizza = null; // Fallback
                resetUIState();
                return;
        }
        updatePizzaImage();
        updateCrustInfo();
        updateToppingsChips();
        updatePrice();
    }

    /**
     * Updates the crust information displayed in the UI.
     */
    private void updateCrustInfo() {
        if (currentPizza == null) {
            tvCrust.setText("");
            return;
        }
        String crustName = currentPizza.formatEnumName(currentPizza.getCrust().name());
        tvCrust.setText(getString(R.string.crust_label, crustName));
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

    /**
     * Updates the image view to reflect the selected pizza type.
     */
    private void updatePizzaImage() {
        int imageRes = R.drawable.chicago_default;
        if (currentPizza != null) {
            if (currentPizza instanceof Deluxe) imageRes = R.drawable.chicago_deluxe;
            else if (currentPizza instanceof Meatzza) imageRes = R.drawable.chicago_meatzza;
            else if (currentPizza instanceof BBQChicken) imageRes = R.drawable.chicago_bbqchicken;
            else if (currentPizza instanceof BuildYourOwn) imageRes = R.drawable.chicago_byo;
        }
        // Default image
        imageView.setImageResource(imageRes);
    }

    /**
     * Create and populates the ChipGroup with chips for all available toppings.
     */
    private void populateToppingsChips() {
        List<Topping> allToppings = Arrays.asList(Topping.values());
        //Create chips
        for (Topping topping : allToppings) {
            Chip chip = new Chip(this);
            chip.setText(sharedResource.formatToppingName(topping));
            chip.setTag(topping); // Store the topping as a tag for easy identification
            chip.setEnabled(false); // Initially disabled
            chipGroupToppings.addView(chip);
        }
    }

    /**
     * Highlights a chip by changing its background color and border.
     *
     * @param chip The chip to highlight
     */
    private void highlightChip(Chip chip) {
        chip.setChipBackgroundColorResource(R.color.chip_checked); // Highlight background color
        chip.setChipStrokeWidth(2f); // Add border
    }

    /**
     * Resets a chip to its default style.
     *
     * @param chip The chip to reset
     */
    private void resetChipStyle(Chip chip) {
        chip.setChipBackgroundColorResource(R.color.chip_unchecked); // Default background color
        chip.setChipStrokeWidth(0f); // No border
    }

    /**
     * Update the chips based on the selected pizza and size
     */
    private void updateToppingsChips() {
        if (currentPizza == null || getSelectedSize() == null) {
            resetChips();
            return;
        }

        for (int i = 0; i < chipGroupToppings.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupToppings.getChildAt(i);
            Topping topping = (Topping) chip.getTag();

            if (currentPizza instanceof BuildYourOwn) {
                chip.setEnabled(true);
                chip.setCheckable(true);
                // Remove previous listeners to avoid duplicate calls
                chip.setOnCheckedChangeListener(null);

                // Set listener for adding/removing toppings
                chip.setOnCheckedChangeListener((buttonView, isChecked) ->
                        handleBuildYourOwnTopping(chip, topping, isChecked));
            } else {
                if (currentPizza.getToppings().contains(topping)) {
                    // Highlight chips for toppings in the selected pizza
                    highlightChip(chip);
                    chip.setEnabled(true);
                    chip.setCheckable(false); // Non-checkable for predefined toppings
                } else {
                    // Reset chips for toppings not in the pizza
                    resetChipStyle(chip);
                }
            }

        }
    }

    /**
     * Handles adding or removing toppings for "Build Your Own" pizzas.
     *
     * @param chip      The chip representing the topping.
     * @param topping   The topping associated with the chip.
     * @param isChecked Whether the chip is checked (selected).
     */
    private void handleBuildYourOwnTopping(Chip chip, Topping topping, boolean isChecked) {
        if (!(currentPizza instanceof BuildYourOwn)) return; // Safety check

        if (isChecked) {
            if (currentPizza.getToppings().size() < MAX_TOPPINGS) {
                currentPizza.addTopping(topping);
                highlightChip(chip);
                chip.setCloseIconVisible(true); // Show close icon
            } else {
                chip.setChecked(false); // Revert check
                sharedResource.getInstance().showAlertDialog(this, "Topping Limit", "Maximum 7 toppings allowed.");
            }
        } else {
            currentPizza.removeTopping(topping);
            resetChipStyle(chip);
            chip.setCloseIconVisible(false); // Hide close icon
        }
        updatePrice();
    }

    /**
     * Updates the price based on the selected pizza and size.
     */
    private void updatePrice() {
        Size size = getSelectedSize();
        if (size == null) {
            tvPrice.setText(getString(R.string.pizza_price, 0.00));
            return;
        }
        currentPizza.setSize(size);
        tvPrice.setText(getString(R.string.pizza_price, currentPizza.price()));
    }

    /**
     * Validates the current pizza selection before proceeding.
     *
     * @return true if both a pizza type and size have been selected;
     *         false otherwise.
     */
    private boolean validatePizzaSelection() {
        if(currentPizza == null) {
            sharedResource.showAlertDialog(this,"Pizza Selection", "Please select a pizza type.");
            return false;
        }

        // if size is not selected
        if (getSelectedSize() == null) {
            sharedResource.showAlertDialog(this,"Size Selection", "Please select a pizza size.");
            return false;
        }
        return true;
    }

    /**
     * Adds the selected pizza to the cart and resets the UI.
     */
    private void addToCart() {
        // if spinner pizza type is not selected
        if (!validatePizzaSelection()) return;

        // Validation pass, add to cart
        Size size = getSelectedSize();
        currentPizza.setSize(size);
        sharedResource.addPizzaToCart(currentPizza, "Chicago Style");
        Toast.makeText(this, currentPizza.getName() + " added to cart.", Toast.LENGTH_SHORT).show();

        // Reset UI states and data
        resetUIState();
    }

    /**
     *  Reset all chips in the ChipGroup to their default state.
     */
    private void resetChips() {
        for (int i = 0; i < chipGroupToppings.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupToppings.getChildAt(i);
            chip.setOnCheckedChangeListener(null); // Remove existing listeners
            resetChipStyle(chip);
            chip.setChecked(false);
            chip.setCloseIconVisible(false);
            chip.setEnabled(false); // Disable chips after resetting
        }
    }

    /**
     * Resets the UI to its default state.
     */
    private void resetUIState() {
        spinnerPizzaType.setSelection(0);
        radioGroupPizzaSize.clearCheck();
        tvPrice.setText(getString(R.string.pizza_price, 0.00));
        imageView.setImageResource(R.drawable.chicago_default);
        resetChips();
        tvCrust.setText("");
        currentPizza = null;
    }

    /**
     * Navigate back to main screen
     */
    private void navigateBackToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}