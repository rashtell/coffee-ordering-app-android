package com.rashtell.coffeeorderingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView quantityTextView, orderSummary ;
    private CheckBox WhippedCream, Chocolate;

    private int numberOfCoffees;

    /**
     * This app displays an order form to order coffee
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quantityTextView = findViewById(R.id.quantityTextView);
        orderSummary = findViewById(R.id.orderSummaryTextView);

        WhippedCream = findViewById(R.id.whippedCreamCheckBox);
        Chocolate = findViewById(R.id.chocolateCheckBox);

        numberOfCoffees = Integer.parseInt((String) quantityTextView.getText());


    }

    /**
     * This method is called when the order button is clicked
     * @param view
     */
    public void submitOrder(View view) {
        orderSummaryDisplay();

        String message ;
        if (numberOfCoffees <= 1) {message = getString(R.string.coffee);
        }else{message = getString(R.string.coffees);
        }

        Toast orderMessage = Toast.makeText(this,
                numberOfCoffees + " " + message + getString(R.string.ordered),
                Toast.LENGTH_LONG);
        orderMessage.show();
    }

    /**
     * This method is called when the plus button is clicked
     * @param view
     */
    public void increment(View view) {
        ++numberOfCoffees;

        displayQuantity(numberOfCoffees);
        displayPrice();
    }

    /**
     * This method is called when the minus button is clicked
     * @param view
     */
    public void decrement(View view) {
        --numberOfCoffees;

        if (numberOfCoffees<0){
            numberOfCoffees=0;
            Toast toast = Toast.makeText(this
                    , R.string.below_zero_cups
                    , Toast.LENGTH_LONG);
            toast.show();
        }
        displayQuantity(numberOfCoffees);
        displayPrice();
    }

    /**
     * Calculates the price per coffee and total cost
     * @return
     */
    private int calcPrice(){
        int basePrice = 5;
        int priceOfCoffee = basePrice;

        if (Chocolate.isChecked()){
            priceOfCoffee = basePrice + 1;}

        if(WhippedCream.isChecked()){
            priceOfCoffee = basePrice + 2;}

        int totalAmount = priceOfCoffee * numberOfCoffees;
        return totalAmount;
    }

    /**
     * This method is responsible for displaying total quantity of coffees
     * @param quantity
     */
    private void displayQuantity(int quantity){
        TextView quantityTextView = findViewById(
                R.id.quantityTextView);
        quantityTextView.setText("" + quantity);
    }

    /**
     * This method is responsible for the displaying the total price
     * @param
     */
    private void displayPrice(){
        TextView priceTextView = (TextView) findViewById(
                R.id.displayPriceTextView);
        priceTextView.setText(getString(R.string.price) + NumberFormat
                .getCurrencyInstance(Locale.US)
                .format(calcPrice()));
    }

    /**
     * This method is responsible for displaying the order summary
     * @param
     */
    private void orderSummaryDisplay(){
        EditText nameEditText = findViewById(R.id.userNameEditText);
        String Name = nameEditText.getText().toString();

        boolean isWhippedCream = WhippedCream.isChecked();

        boolean isChocolate = Chocolate.isChecked();

        int Quantity = numberOfCoffees;

        int totalPrice = calcPrice();

        String message = "Name: "+ Name +
                "\nAdd whipped cream? " + isWhippedCream +
                "\nAdd chocolate ? " + isChocolate +
                "\nQuantity: " + Quantity +
                "\nTotal: " + NumberFormat.getCurrencyInstance(Locale.US)
                .format(totalPrice) +
                "\nThank you!";
        orderSummary.setText(message);

        composeEmail(Name, message);
    }

    /**
     * updates the display price corresponding to the checkboxes state
     * @param view
     */
    public void updateView(View view) {
        displayPrice();
    }

    /**
     * Composes email
     * @param name
     * @param message
     */
    private void composeEmail(String name, String message){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, new StringBuilder()
                .append(getString(R.string.coffee_order_for))
                .append(name).toString());
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

}
