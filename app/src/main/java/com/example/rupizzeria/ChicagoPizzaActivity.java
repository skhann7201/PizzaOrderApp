package com.example.rupizzeria;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * ChicagoPizzaActivity manages the user interface for selecting different types of pizzas,
 * sizes, and toppings in the Chicago-style pizza menu. This activity utilizes a ViewPager2
 * for displaying pizzas, a ChipGroup for selecting toppings, and RadioButtons for choosing pizza sizes.
 * @author Vy Nguyen
 */
public class ChicagoPizzaActivity extends AppCompatActivity {

    private ViewPager2 vp_pizza;
    private TextView tv_price;
    private ChipGroup cg_toppings;


    private PizzaFactory chicagoPizzaFactory = new ChicagoPizza();
    private int currentPosition = 0;
    private List<Pizza> pizzaList;
    private List<Pizza> cartItems = new ArrayList<>(); // List to store pizzas added to the cart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicago_pizza);

        // Initialize UI elements
        vp_pizza = findViewById(R.id.vp_cartView);
        tv_price = findViewById(R.id.tv_price);
        cg_toppings = findViewById(R.id.cg_toppings);

        // Populate pizzaList
        setupPizzaList();

        // Set up view pager
        setupViewPager();

        // Set up the size selection logic
        setupSizeSelection();
        // Initialize back button
        ImageButton backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> navigateBackToHome());

        // Set up Add to Cart Button
        findViewById(R.id.btn_placeOrder).setOnClickListener(v -> addToCart());

    } // end onCreate

    /**
     * Initializes the list of pizzas available in the menu.
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
     * Sets up the ViewPager2 to display pizzas and handles page change logic.
     */
    private void setupViewPager() {
        vp_pizza.setAdapter(createViewPagerAdapter());
        vp_pizza.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                handlePageChange(position);
            }
        });
        applyCarouselEffect(vp_pizza);
    }

    /**
     * Creates the RecyclerView.Adapter for the ViewPager2 to display pizza details.
     *
     * @return A RecyclerView.Adapter for the pizza ViewPager2.
     */
    private RecyclerView.Adapter<PizzaViewHolder> createViewPagerAdapter() {
        return new RecyclerView.Adapter<PizzaViewHolder>() {
            @NonNull
            @Override
            public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.pizza_image_item, parent, false);
                return new PizzaViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
                // Bind image resource
                bindPizzaToView(holder, position);
            }

            @Override
            public int getItemCount() {
                // Total number of items
                return pizzaList.size();
            }
        };
    }

    /**
     * Highlights the selected pizza in the ViewPager2.
     *
     * @param holder The PizzaViewHolder for the selected pizza.
     */
    private void highlightSelectedPizza(PizzaViewHolder holder) {
        holder.cardView.setCardElevation(12f);
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary_orange));
    }

    /**
     * Resets the highlight for non-selected pizzas in the ViewPager2.
     *
     * @param holder The PizzaViewHolder for a non-selected pizza.
     */
    private void resetPizzaHighlight(PizzaViewHolder holder) {
        holder.cardView.setCardElevation(8f);
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
    }

    /**
     * Binds a pizza's data (image and name) to the ViewHolder.
     *
     * @param holder The PizzaViewHolder to bind data to.
     * @param position The position of the pizza in the list.
     */
    private void bindPizzaToView(PizzaViewHolder holder, int position) {
        Pizza pizza = pizzaList.get(position);
        holder.imageView.setImageResource(getPizzaImageResource(pizza));
        holder.textView.setText(pizza.getName());

        if (currentPosition == position) {
            highlightSelectedPizza(holder);
        } else {
            resetPizzaHighlight(holder);
        }
    }

    /**
     * Resets the size selection RadioGroup to have no selected option.
     */
    private void resetSizeSelection() {
        RadioGroup radioGroupSize = findViewById(R.id.radioGroupPizzaSize);
        radioGroupSize.clearCheck();
    }

    /**
     * Resets the price display to $0.00.
     */
    private void resetPrice() {
        tv_price.setText("$0.00");
    }

    /**
     * Handles changes in the ViewPager2's selected page.
     *
     * @param position The position of the newly selected page.
     */
    private void handlePageChange(int position) {
        currentPosition = position;
        vp_pizza.getAdapter().notifyDataSetChanged();
        loadToppingsToChips();
        resetSizeSelection();
        resetPrice();
    }

    /**
     * Sets up the RadioGroup for size selection and handles size change events.
     */
    private void setupSizeSelection(){
        // set of size selection
        RadioGroup radioGroupSize = findViewById(R.id.radioGroupPizzaSize);

        radioGroupSize.setOnCheckedChangeListener((group,checkedId) ->
                handleSizeSelection(checkedId));

    }

    /**
     * Handles the selection of a pizza size and updates the price.
     *
     * @param checkedId The ID of the selected RadioButton.
     */
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

    /**
     * Retrieves the image resource ID for a given pizza.
     *
     * @param pizza The pizza to get the image resource for.
     * @return The image resource ID.
     */
    private int getPizzaImageResource(Pizza pizza) {
        if (pizza instanceof Deluxe) return R.drawable.chicago_deluxe;
        if (pizza instanceof Meatzza) return R.drawable.chicago_meatzza;
        if (pizza instanceof BBQChicken) return R.drawable.chicago_bbqchicken;
        if (pizza instanceof BuildYourOwn) return R.drawable.chicago_byo;
        return R.drawable.chicago_style_bt; // Fallback image
    }


    /**
     * Formats the Topping enum name to be user-friendly.
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

    /**
     * Creates a new Chip with default properties for the given topping.
     *
     * @param topping The topping for the chip.
     * @param selectedPizza The currently selected pizza.
     * @return The configured Chip instance.
     */
    private Chip createChip(Topping topping, Pizza selectedPizza){
        Chip chip = new Chip(this);
        chip.setText(formatToppingName(topping));
        chip.setCheckable(false);
        chip.setClickable(false);
        chip.setCloseIconVisible(false); // Hide "X" by default
        chip.setChipStrokeWidth(0f); // No border initially
        chip.setChipStrokeColor(null); // Ensure no stroke color
        chip.setChipBackgroundColorResource(R.color.chip_unchecked); // Default background color

        if (selectedPizza.getToppings().contains(topping)) {
            highlightChip(chip);
        }
        return chip;
    }
    /**
     * Configures the Chip's behavior based on the selected pizza type and toppings.
     *
     * @param chip The chip to configure.
     * @param topping The topping associated with the chip.
     * @param selectedPizza The currently selected pizza.
     */
    private void configureChipForTopping(Chip chip, Topping topping, Pizza selectedPizza) {
        if (selectedPizza instanceof BuildYourOwn) {
            setupBuildYourOwnChip(chip, topping, (BuildYourOwn) selectedPizza);
        }
    }

    /**
     * Sets up the chip's behavior for Build Your Own pizzas.
     *
     * @param chip The chip to configure.
     * @param topping The topping associated with the chip.
     * @param selectedPizza The currently selected Build Your Own pizza.
     */
    private void setupBuildYourOwnChip(Chip chip, Topping topping, BuildYourOwn selectedPizza) {
        chip.setCloseIconVisible(false);
        chip.setClickable(true);
        chip.setCheckable(true);
        chip.setChipBackgroundColorResource(chip.isChecked() ? R.color.chip_checked : R.color.chip_unchecked);

        chip.setOnCloseIconClickListener(v -> {
            handleChipCloseAction(chip, topping, selectedPizza);
        });

        chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            handleChipSelectionChange(chip, topping, isChecked, selectedPizza);
        });
    }

    /**
     * Handles the selection/deselection of a chip.
     *
     * @param chip The chip being interacted with.
     * @param topping The topping associated with the chip.
     * @param isChecked Whether the chip is checked.
     * @param selectedPizza The currently selected Build Your Own pizza.
     */
    private void handleChipSelectionChange(Chip chip, Topping topping, boolean isChecked, BuildYourOwn selectedPizza) {
        if (isChecked) {
            if (selectedPizza.getToppings().size() >= 7) {
                Toast.makeText(this, "Cannot add more than 7 toppings!", Toast.LENGTH_SHORT).show();
                chip.setChecked(false); // Revert the selection
            } else {
                selectedPizza.addTopping(topping);
                highlightChip(chip);
                Toast.makeText(this, topping.name() + " added", Toast.LENGTH_SHORT).show();
            }
        } else {
            resetChipStyle(chip);
            selectedPizza.removeTopping(topping);
            Toast.makeText(this, topping.name() + " removed", Toast.LENGTH_SHORT).show();
        }
        updatePrice(selectedPizza);
    }

    /**
     * Handles the action when a chip's close icon is clicked.
     *
     * @param chip The chip associated with the topping.
     * @param topping The topping to remove.
     * @param selectedPizza The currently selected pizza.
     */
    private void handleChipCloseAction(Chip chip, Topping topping, BuildYourOwn selectedPizza) {
        chip.setChecked(false); // Uncheck the chip
        resetChipStyle(chip);
        selectedPizza.removeTopping(topping); // Remove topping from the pizza
        Toast.makeText(this, topping.name() + " removed", Toast.LENGTH_SHORT).show(); // Debug message
        updatePrice(selectedPizza);
    }


    /**
     * Highlights the chip when selected.
     *
     * @param chip The chip to highlight.
     */
    private void highlightChip(Chip chip) {
        chip.setChipBackgroundColorResource(R.color.chip_checked); // Highlight background color
        chip.setChipStrokeWidth(2f); // Add border
        chip.setCloseIconVisible(true); // Show the "X"
    }

    /**
     * Resets the chip style to its default state.
     *
     * @param chip The chip to reset.
     */
    private void resetChipStyle(Chip chip) {
        chip.setChipBackgroundColorResource(R.color.chip_unchecked); // Default background color
        chip.setChipStrokeWidth(0f); // No border
        chip.setCloseIconVisible(false); // Hide the "X"
    }


    /**
     * Loads the toppings for the selected pizza into the ChipGroup.
     */
    private void loadToppingsToChips() {
        // Clear existing chips
        cg_toppings.removeAllViews();

        List<Topping> allToppings = Arrays.asList(Topping.values());
        Pizza selectedPizza = pizzaList.get(currentPosition);

        for (Topping topping : allToppings) {
            Chip chip = createChip(topping, selectedPizza);
            configureChipForTopping(chip, topping, selectedPizza);
            cg_toppings.addView(chip);
        }
    }

    private void resetSelections() {
        resetSizeSelection();
        resetPrice();
        loadToppingsToChips();
    }

    /**
     * Updates the price display based on the selected pizza's price.
     *
     * @param pizza The selected pizza.
     */
    private void updatePrice(Pizza pizza) {
        tv_price.setText("$" + String.format("%.2f", pizza.price()));
    }

    /**
     * Navigates back to the MainActivity.
     */
    private void navigateBackToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Optional: Clears other activities
        startActivity(intent);
        finish(); // End current activity
    }

    private void addToCart(){
        Pizza selectedPizza = pizzaList.get(currentPosition);

        // Check if the size is set
        if (selectedPizza.getSize() == null) {
            Toast.makeText(this, "Please select a size before adding to the cart.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the current pizza to the cart
        ShareResource sharedResource = ShareResource.getInstance();
        sharedResource.addPizzaToCart(selectedPizza, "Chicago Style");

        // Show confirmation
        Toast.makeText(this, selectedPizza.getName() + " added to cart.", Toast.LENGTH_SHORT).show();

        // Optional: Reset selections after adding to the cart
        resetSelections();
    }

    /**
     * ViewHolder class for displaying pizza details in the ViewPager2.
     */
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


}