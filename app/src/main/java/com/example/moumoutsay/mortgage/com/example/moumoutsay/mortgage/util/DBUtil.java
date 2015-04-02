/**
 * Author: Wei-Lin Tsai weilints@andrew.cmu.edu
 * Date: on 3/31/15.
 *
 */

package com.example.moumoutsay.mortgage.com.example.moumoutsay.mortgage.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DBUtil extends SQLiteOpenHelper{
    public static final int data_base_version = 1;

    public DBUtil(Context context) {
        super(context, SQLCmd.DATEBASE_NAME, null, data_base_version);
        Log.d("DBUtil", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLCmd.CREATE_PAYOFF_DATE);
        db.execSQL(SQLCmd.CREATE_PAYMENT);
        Log.d("DBUtil", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addPayoffDate(DBUtil dbUtil, int beginMon, int beginYear, int totalYear, String payoffDate) {
        SQLiteDatabase db = dbUtil.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("begin_mon", beginMon);
        cv.put("begin_year", beginYear);
        cv.put("total_year", totalYear);
        cv.put("payoff_date", payoffDate);
        db.insert(SQLCmd.TB_PAYOFF_DATE, null, cv);

        Log.d("DBUtil", "addPayoffDate done");
    }

    // if not found, return null
    public String getPayoffDate (DBUtil dbUtil, int beginMon, int beginYear, int totalYear) {
        String res = null;
        String[] columns = {"payoff_date"};
        SQLiteDatabase db = dbUtil.getReadableDatabase();

        onCreate(db);

        Cursor cr = db.query(SQLCmd.TB_PAYOFF_DATE, columns,
                "begin_mon =? and begin_year =? and total_year =?",
                new String[] {Integer.toString(beginMon),Integer.toString(beginYear),Integer.toString(totalYear) },
                null, null, null, null);

        if (cr.moveToFirst()) {
            res = cr.getString(cr.getColumnIndexOrThrow("payoff_date"));
        }

        Log.d("DBUtil", "getPayoffDate done");

        return res;
    }

    public void addPayment(DBUtil dbUtil, double amount, int years, double rate, int propertyTax,
                           int propertyInsurance, String monthlyPayment, String totalPayment) {
        SQLiteDatabase db = dbUtil.getWritableDatabase();
        ContentValues cv = new ContentValues();
        onCreate(db);

        cv.put(SQLCmd.AMOUNT, amount);
        cv.put(SQLCmd.YEARS, years);
        cv.put(SQLCmd.RATE, rate);
        cv.put(SQLCmd.PROPERTY_TAX, propertyTax);
        cv.put(SQLCmd.PROPERTY_INSURANCE, propertyInsurance);
        cv.put(SQLCmd.MONTHLY_PAYMENT, monthlyPayment);
        cv.put(SQLCmd.TOTAL_PAYMENT, totalPayment);

        db.insert(SQLCmd.TB_PAYMENT, null, cv);

        Log.d("DBUtil", "addPayment done");
    }

    public List<String> getPayment(DBUtil dbUtil, double amount, int years, double rate, int propertyTax,
                                   int propertyInsurance) {
        List<String> res = null;

        String[] columns = {SQLCmd.MONTHLY_PAYMENT, SQLCmd.TOTAL_PAYMENT};
        SQLiteDatabase db = dbUtil.getReadableDatabase();
        Cursor cr = db.query(SQLCmd.TB_PAYMENT, columns,
                SQLCmd.AMOUNT + " =? " + "and " +
                SQLCmd.YEARS + " =? " + "and " +
                SQLCmd.RATE + " =? " + "and " +
                SQLCmd.PROPERTY_TAX + " =? " + "and " +
                SQLCmd.PROPERTY_INSURANCE + " =? ",
                new String[] {Double.toString(amount),
                              Integer.toString(years),
                              Double.toString(rate),
                              Integer.toString(propertyTax),
                              Integer.toString(propertyInsurance),
                },
                null, null, null, null);

        if (cr.moveToFirst()) {
            res = new ArrayList<String>();
            res.add(cr.getString(cr.getColumnIndexOrThrow(SQLCmd.MONTHLY_PAYMENT)));
            res.add(cr.getString(cr.getColumnIndexOrThrow(SQLCmd.TOTAL_PAYMENT)));
        }

        Log.d("DBUtil", "getPayment done");

        return res;
    }

}
