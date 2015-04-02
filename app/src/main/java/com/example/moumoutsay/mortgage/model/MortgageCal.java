/**
 * Author: Wei-Lin Tsai weilints@andrew.cmu.edu
 * Date: on 3/31/15.
 *
 * The core calculation part
 * Note, most return types are String
 */
package com.example.moumoutsay.mortgage.model;

public class MortgageCal {

    // convert from Jan ~ Dec to 1 ~ 12
    public int converStrMonToIntMon(String strMon) {
        if (strMon.equals("Jan")) {
            return 1;
        }
        if (strMon.equals("Feb")) {
            return 2;
        }
        if (strMon.equals("Mar")) {
            return 3;
        }
        if (strMon.equals("Apr")) {
            return 4;
        }
        if (strMon.equals("May")) {
            return 5;
        }
        if (strMon.equals("Jun")) {
            return 6;
        }
        if (strMon.equals("Jul")) {
            return 7;
        }
        if (strMon.equals("Aug")) {
            return 8;
        }
        if (strMon.equals("Sep")) {
            return 9;
        }
        if (strMon.equals("Oct")) {
            return 10;
        }
        if (strMon.equals("Nov")) {
            return 11;
        }
        if (strMon.equals("Dec")) {
            return 12;
        }

        return 13;   // error condition
    }

    // convert from 1 ~ 12 to Jan ~ Dec
    public String convertIntMonToStrMon(int intMon) {

        switch (intMon) {
            case 1:   return "Jan";
            case 2:   return "Feb";
            case 3:   return "Mar";
            case 4:   return "Apr";
            case 5:   return "May";
            case 6:   return "Jue";
            case 7:   return "Jul";
            case 8:   return "Aug";
            case 9:   return "Sep";
            case 10:  return "Oct";
            case 11:  return "Nov";
            case 12:  return "Dec";
        }

        return "Err";  // error condition
    }

    public String getPayoffDate(int beginMon, int beginYear, int totalYear) {
        StringBuilder sb = new StringBuilder();

        int endYear = beginYear + totalYear;
        int endMon = beginMon;
        if (beginMon == 1) {
            endMon = 12;
            --endYear;
        } else {
            --endMon;
        }

        sb.append(convertIntMonToStrMon(endMon));
        sb.append(", ");
        sb.append(endYear);

        return sb.toString();
    }

    public String getMonthlyPayment (double amount, int years, double rate, int propertyTax, int propertyInsurance) {
        /*
        c = rP / ( 1 - ( 1 + r ) ^ -N )
            r is month rate, N is 12 * years
         */
        double res = 0;
        double monthlyRate = rate / 12 / 100;

        res = (monthlyRate * amount ) / ( 1 - Math.pow(1 + monthlyRate, (years * -12) ));

        res += (double)propertyTax / 12;
        res += (double)propertyInsurance / 12;

        return String.format("%.3f", res);
    }

    public String getTotalPayment(double amount, int years, double rate, int propertyTax, int propertyInsurance) {
        /*
            Total Payment = PV  /  [(1- (1 / ((1 + i)^n) )) / i]
            Where PV = amt of loan (present value)
            i=periodic interest rate
            n=# of periods
        */
        double res = 0;
        double monthlyRate = rate / 12 / 100;

        res = amount  /  (( 1 - ( 1 / (Math.pow(1 + monthlyRate, years * 12)))) / monthlyRate) * years * 12;

        res += propertyTax * years;
        res += propertyInsurance * years;

        return String.format("%.3f", res);
    }
}
