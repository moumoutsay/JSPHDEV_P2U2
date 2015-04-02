/**
 * Author: Wei-Lin Tsai weilints@andrew.cmu.edu
 * Date: on 3/31/15.
 *
 * The main activity. Will do the calculation when user click the button
 */

package com.example.moumoutsay.mortgage.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.moumoutsay.mortgage.com.example.moumoutsay.mortgage.util.DBUtil;
import com.example.moumoutsay.mortgage.model.MortgageCal;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get data
        final EditText editPurchasePrice = (EditText) findViewById(R.id.editPurchasePrice);
        final EditText editDownPayment = (EditText) findViewById(R.id.editDownPayment);
        final EditText editMortgateTerm = (EditText) findViewById(R.id.editMortgateTerm);
        final EditText editInterestRate = (EditText) findViewById(R.id.editInterestRate);
        final EditText editPropertyTax = (EditText) findViewById(R.id.editPropertyTax);
        final EditText editPropertyInsurance = (EditText) findViewById(R.id.editPropertyInsurance);
        final EditText editZipCode = (EditText) findViewById(R.id.editZipCode);

        // Button
        Button buttonCalculate = (Button) findViewById(R.id.buttonCalculate);

        // Spinner
        final Spinner spinnerMonth = (Spinner) findViewById(R.id.spinnerMon);
        final Spinner spinnerYear = (Spinner) findViewById(R.id.spinnerYear);


        // here is output
        final EditText editMonthlyPayment = (EditText) findViewById(R.id.editMonthlyPayment);
        final EditText editTotalPayment = (EditText) findViewById(R.id.editTotalPayment);
        final EditText editPayoffDate = (EditText) findViewById(R.id.editPayoffDate);

        // Take action when click the button
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MortgageCal mortgageCal = new MortgageCal();
                // get the database
                DBUtil dbUtil = new DBUtil(ctx);

                // Parse value
                try {
                    double purchasePrice = Double.parseDouble(editPurchasePrice.getText().toString());
                    double downPayment = Double.parseDouble(editDownPayment.getText().toString());
                    int mortgateTerm = Integer.parseInt(editMortgateTerm.getText().toString());
                    double interestRate = Double.parseDouble(editInterestRate.getText().toString());
                    int propertyTax = Integer.parseInt(editPropertyTax.getText().toString());
                    int propertyInsurance = Integer.parseInt(editPropertyInsurance.getText().toString());
                    int zipCode = Integer.parseInt(editZipCode.getText().toString());
                    int selectMon = mortgageCal.converStrMonToIntMon(spinnerMonth.getSelectedItem().toString());
                    int selectYear = Integer.parseInt(spinnerYear.getSelectedItem().toString());

                    double realLoanAmount = purchasePrice * (100 - downPayment) / 100;


                    // handle payoffDate
                    String payoffDate = dbUtil.getPayoffDate(dbUtil, selectMon, selectYear, mortgateTerm);
                    if (payoffDate == null) {
                        Log.d("PayoffDate", "No existing payoffDate from DB");
                        payoffDate = mortgageCal.getPayoffDate(selectMon, selectYear, mortgateTerm);
                        dbUtil.addPayoffDate(dbUtil, selectMon, selectYear, mortgateTerm, payoffDate);
                    } else {
                        Log.d("PayoffDate", "Get payoffDate from DB" + payoffDate);
                    }

                    // Handle payment
                    String monthlyPayment;
                    String totalPayment;
                    // addPayment(DBUtil dbUtil, double amount, int years, double rate, int propertyTax, int propertyInsurance, String monthlyPayment, String totalPayment)
                    // getPayment(DBUtil dbUtil, double amount, int years, double rate, int propertyTax, int propertyInsurance) {
                    List<String> paymentList = dbUtil.getPayment(dbUtil, realLoanAmount, mortgateTerm, interestRate, propertyTax, propertyInsurance);
                    if (paymentList == null) { // not found
                        Log.d("Payment", "No existing Payment from DB");
                        monthlyPayment = mortgageCal.getMonthlyPayment(realLoanAmount, mortgateTerm, interestRate, propertyTax, propertyInsurance);
                        totalPayment = mortgageCal.getTotalPayment(realLoanAmount, mortgateTerm, interestRate, propertyTax, propertyInsurance);
                        dbUtil.addPayment(dbUtil, realLoanAmount, mortgateTerm, interestRate, propertyTax, propertyInsurance,monthlyPayment, totalPayment);
                    } else {
                        monthlyPayment = paymentList.get(0);
                        totalPayment = paymentList.get(1);
                        Log.d("Payment", "Get monthly payment from DB" + monthlyPayment);
                        Log.d("Payment", "Get total payment from DB" + totalPayment);
                    }

                    editMonthlyPayment.setText(monthlyPayment);
                    editTotalPayment.setText(totalPayment);
                    editPayoffDate.setText(payoffDate);

                } catch (Exception e) {
                    editMonthlyPayment.setText("Invalid input or blank input");
                    editTotalPayment.setText("Please check the input correctness");
                    editPayoffDate.setText("");
                    Log.e("Click Calculation","In valid or blank input");
                    Log.e("Click Calculation", e.toString());
                }
                Log.i("Click Calculation", "Calculation Done here");
            }
        });
    }

//  no need now
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
