package com.example.rupizzeria;

import java.util.ArrayList;

/**
 * Abstract class representing a Pizza with crust, size, and toppings.
 * Different types of pizzas will extend this class and implement the price method.
 *
 * @author Vy Nguyen, Shahnaz Khan
 */
public abstract class Pizza {
    private ArrayList<Topping> toppings; // List of toppings for pizza
    private Crust crust; // Crust type for the pizza
    private Size size; // Size of the pizza
    private static final double NJ_SALES_TAX = 0.06625;

    /**
     * Default constructor for a pizza
     */
    public Pizza(){
        this.crust = null;
        this.size = null;
        this.toppings = new ArrayList<>();
    }

    /**
     * Constructor for a pizza
     * @param crust The crust type for the pizza
     */
    public Pizza(Crust crust) {
        this.crust = crust;
        this.size = null;
        this.toppings = new ArrayList<>();
    }

    /**
     * Constructor for Pizza
     * @param crust The crust type for the pizza
     * @param size the size of the pizza
     */
    public Pizza(Crust crust, Size size) {
        this.crust = crust;
        this.size = size;
        this.toppings = new ArrayList<>();
    }

    /**
     * Adds a topping to the pizza
     * Maximum limit of 7 toppings is not exceeded.
     * @param topping The topping to add
     */
    public void addTopping(Topping topping) {
        if(toppings.size() < 7) {
            toppings.add(topping);
        }
    }

    /**
     * Removes a topping from the pizza
     * @param topping The topping to remove.
     */
    public void removeTopping(Topping topping) {
        toppings.remove(topping);
    }

    /**
     * Gets the list of toppings on the pizza
     * @return A copy ArrayList of Toppings objects to maintain encapsulation.
     */
    public ArrayList<Topping> getToppings() {
        return new ArrayList<>(toppings);
    }

    /**
     * Gets the crust type of pizza
     * @return The crust type.
     */
    public Crust getCrust() {
        return crust;
    }

    /**
     * Sets the size of the pizza
     * @param size The desired size of the pizza
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     * Gets the size of the pizza
     * @return The size of the pizza
     */
    public Size getSize() {
        return size;
    }

    /**
     * Gets the sale tax of New Jersey
     * @return the sale tax of New Jersey.
     */
    public static double getNJSaleTax() {
        return NJ_SALES_TAX;
    }

    /**
     * Calculate the price of the pizza, implemented by subclasses.
     * @return The price of the pizza
     */
    public abstract double price();

    /**
     * Get the name of the specific type of pizza.
     * @return a string representing the name of pizza type.
     */
    public String getName() {
        // Use the simple class name to identify the type of pizza
        String className = this.getClass().getSimpleName();

        // Handle special case
        if (className.equals("BBQChicken")) {
            return "BBQ Chicken";
        }

        return className.replaceAll("([a-z])([A-Z])", "$1 $2"); // Add a space before uppercase letters
    }

    /**
     * Formats an enum name to appear in CamelCase.
     *
     * @param enumName The name of the enum constant.
     * @return The formatted name in CamelCase.
     */
    public String formatEnumName(String enumName) {
        if (enumName == null || enumName.isEmpty()) {
            return "";
        }

        // Special case for "Hand_TOSSED"
        if ("HAND_TOSSED".equals(enumName)) {
            return "Hand-tossed";
        }

        //Special case for BBQ Chicken
        if("BBQ_CHICKEN".equals(enumName)) {
            return "BBQ Chicken";
        }

        // Convert the name to lowercase and replace underscores with spaces
        String formatted = enumName.toLowerCase().replace('_', ' ');
        // Capitalize the first letter of each word
        String[] words = formatted.split(" ");
        StringBuilder camelCaseName = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                camelCaseName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        // Remove the trailing space and return the result
        return camelCaseName.toString().trim();
    }


    /**
     * A string representation of the pizza
     * @return a string representation of a pizza including crust, size and toppings.
     */
    @Override
    public String toString() {

        String pizzaType = this.getName();

        StringBuilder toppingsList = new StringBuilder("");
        for (int i = 0; i < toppings.size(); i++) {
            toppingsList.append(formatEnumName(toppings.get(i).name()));
            if (i < toppings.size() - 1) {
                toppingsList.append(", ");
            }
        }

        return pizzaType + " (" + formatEnumName(size.name()) + ")" + "\n" +
                "\tCrust: " + formatEnumName(crust.name()) + "\n" +
                (toppings.isEmpty() ? "\tNo Toppings\n" : "\tToppings: " + toppingsList + "\n") +
                "\t[$" + String.format("%.2f", this.price()) + "]\n"
                ;
    }
}
