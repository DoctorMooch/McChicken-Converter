package com.mooch.austin.mcchickenconverter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    double price;
    Button convert;
    TextView result;
    EditText inPrice, taxField;
    CheckBox taxCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        convert = (Button) findViewById(R.id.convert_button);
        result = (TextView) findViewById(R.id.result_field);
        inPrice = (EditText) findViewById(R.id.dollar_ammount);
        taxCheckbox = (CheckBox) findViewById(R.id.taxCheckbox);
        taxField = (EditText) findViewById(R.id.taxField);
        Context context = getApplicationContext();
        CharSequence toastWarning = "Please make sure both the cost and tax field are populated";
        final Toast warnEmptyToast = Toast.makeText(context, toastWarning, Toast.LENGTH_SHORT);

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                //Check if price and tax field are populated
                if(TextUtils.isEmpty(inPrice.getText().toString()) || TextUtils.isEmpty(taxField.getText().toString())) {
                    //Show toast to help user correct inputs
                    warnEmptyToast.show();
                }else{
                    //Ok to proceed
                    price = Double.parseDouble(inPrice.getText().toString());
                    result.setText(convertToMcChicken(price));
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            setContentView(R.layout.activity_settings_menu);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String convertToMcChicken(double price) {
        DecimalFormat df = new DecimalFormat("#.####");         //Used for the McChicken Number
        DecimalFormat taxFormat = new DecimalFormat("#.##");    //Used for rounding the tax
        df.setRoundingMode(RoundingMode.CEILING);
        df.setRoundingMode(RoundingMode.CEILING);
        double costOfItem = price;
        double taxrate, totTax;
        double chickenAmmt;
        //Get tax rate
        taxrate = Double.parseDouble(taxField.getText().toString());
        //Calculate tax based on rate given
        totTax = Double.parseDouble(taxFormat.format(.99 * taxrate));

        //Check if box is checked
        if(taxCheckbox.isChecked()) {
            //If so, factor in tax
            chickenAmmt = costOfItem / (1+totTax);
        }else{
            //If not, exclude tax
            chickenAmmt = costOfItem / .99;
        }
        //Return McChickens
        return df.format(chickenAmmt);
    }
}
