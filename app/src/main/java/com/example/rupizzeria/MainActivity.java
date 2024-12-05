package com.example.rupizzeria;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * The MainActivity class is the entry point for RU Pizzeria applications.
 * It sets up the main layout, handles user interactions with the main menu cardview,
 * and manages navigation through the BottomNavigationView
 *
 * @author Vy Nguyen, Shahnaz Khan
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is created.
     * This method sets up the UI elements, handles navigation,
     * and configures window insets for edge-to-edge displays.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find CardView and set OnClickListener
        CardView chicagoPizzaCard = findViewById(R.id.cardview_chicagoPizza);
        chicagoPizzaCard.setOnClickListener(v -> {
            // Navigate to ChicagoPizzaActivity
            Intent intent = new Intent(MainActivity.this, ChicagoPizzaActivity.class);
            startActivity(intent);
        });

        // Find NYPizzaCard and set OnClickListener
        CardView nyPizzaCard = findViewById(R.id.cardview_NYPizza);
        nyPizzaCard.setOnClickListener(v -> {
            // Navigate to NYPizzaActivity
            Intent intent = new Intent(MainActivity.this, NewYorkPizzaActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Handle home
                return true;
            } else if (id == R.id.nav_cart) {
                // Handle cart
                Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else if (id == R.id.nav_order) {
                // Handle order
                Intent orderIntent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(orderIntent);
                return true;
            }

            return false;
        });

    }
}