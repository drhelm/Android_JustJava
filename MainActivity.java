package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends ActionBarActivity {
    int quantity = 0; //global variable to keep track of the quantity
    int pricePerCup = 5;     //global variable to keep track of the price
    int whippedCreamPrice = 1;
    int chocolatePrice = 2;
    //TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
    //TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //define and initialize CheckBox views - check if they are checked
        CheckBox whippedCreamCheck = (CheckBox) findViewById(R.id.whipped_cream_check);
        boolean hasWhippedCream = whippedCreamCheck.isChecked(); //charge $1 more
        CheckBox chocolateCheck = (CheckBox) findViewById(R.id.chocolate_check);
        boolean hasChocolate = chocolateCheck.isChecked();      //charge $2 more

        //define and initialize EditText views - set customerName to text value
        EditText NameField = (EditText) findViewById(R.id.name_field);
        String customerName = NameField.getText().toString();

        //calculates the price of the order and stores it in price
        int price = calculatePrice(hasWhippedCream,hasChocolate);

        //create and print the order summary
        String orderSummary = createOrderSummary(price,hasWhippedCream,hasChocolate,customerName);
        composeEmail(orderSummary, customerName);
    }

    /**
     * Calculates the price of the order.
     *
     * @param hasWhippedCream is whether or not the user wants whipped cream
     * @param hasChocolate is whether or not the user wants chocolate
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream,
                               boolean hasChocolate) {
        //sets basePrice to the price of basic coffee
        int basePrice = pricePerCup;

        //adds the price of whipped cream to basePrice
        if (hasWhippedCream) {
            basePrice += whippedCreamPrice;
        }

        //adds the price of chocolate to basePrice
        if (hasChocolate) {
            basePrice += chocolatePrice;
        }

        int price = quantity * basePrice;
        return price;
    }

    /**
     * Creates an order summary and puts it into an email
     *
     * @param price is the total price of the order
     * @param addWhippedCream is whether or not the user wants whipped cream
     * @param addChocolate is whether or not the user wants chocolate
     * @param customerName contains the customers name that was entered into the name field
     * @return summary of the order to be printed to the screen
     */
    private String createOrderSummary(int price,
                                      boolean addWhippedCream,
                                      boolean addChocolate,
                                      String customerName) {
        String orderSummary = "";
        orderSummary =  "Name: " + customerName + "\n";
        orderSummary += "Add whipped cream? " + addWhippedCream + "\n";
        orderSummary += "Add chocolate? " + addChocolate + "\n";
        orderSummary += "Quantity: " + quantity + "\n";
        orderSummary += "Total: $" + price;
        orderSummary += "\nThank you!";

        return orderSummary;
    }
    /**
     * This method is called when the ' + ' button is clicked.
     */
    public void increment(View view) {
        quantity = quantity + 1;

        //don't allow more than 100 cups should also display a toast message
        if (quantity > 100) {
            quantity = 100;

            //Create and show toast message
            Context toastContext = getApplicationContext();
            CharSequence toastText = "Cannot order more than 100 coffees.";
            int toastDuration = Toast.LENGTH_SHORT;
            Toast maxToast = Toast.makeText(toastContext, toastText, toastDuration);
            maxToast.show();
        }

        display(quantity);
    }

    /**
     * This method is called when the ' - ' button is clicked.
     */
    public void decrement(View view) {
        quantity = quantity - 1;

        //don't allow cups to go below 0 should also display a toast message
        if (quantity < 1) {
            quantity = 1;

            //Create and show toast message
            Context toastContext = getApplicationContext();
            CharSequence toastText = "Cannot order less than one coffee.";
            int toastDuration = Toast.LENGTH_SHORT;
            Toast negToast = Toast.makeText(toastContext, toastText, toastDuration);
            negToast.show();
        }

        display(quantity);
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price on the screen.
     */
    /*private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }*/


    /**
     * This method displays the given text on the screen.
     */
    private void composeEmail(String orderSummary,
                                String customerName) {
        String emailAddress = "JustJavaOrders@justjava.com";
        String emailSubject = "JustJava order for ";

        //TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        //orderSummaryTextView.setText(message);

        Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject + customerName);
        emailIntent.putExtra(Intent.EXTRA_TEXT, orderSummary);
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }

    }
}
