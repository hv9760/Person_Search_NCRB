package com.ncrb.samapre.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Class to hold all application utility methods
 *
 * @author Ultron
 */

public class Utils {

    private static final String TAG = "Utils";


    // debug boolean enable or disable console log printing
    private static boolean DEBUG = true;

    /**
     * Constructor to prevent instantiation.
     */
    private Utils() {
        // Nothing to do
    }

    /**
     * Method to verify if network connection available and connecting.
     *
     * @param context
     * @return true if connecting successfully.
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr != null && conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        }

        return false;
    }



    /**
     * @param
     * @return
     */

    public static String getDateTime(String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String date = simpleDateFormat.format(new Date());
        printv(date);

        return date;
    }




    /**
     * @param
     * @return
     */

    public static String convertToDateFormat(Context context, String new_format_value
            , String existing_format_value, String date_value) {

        Date eDDte;
        try {
            // @Logic : convert string into date value - specially for MySQL
            SimpleDateFormat new_format = new SimpleDateFormat(new_format_value);
            SimpleDateFormat existing_format = new SimpleDateFormat(existing_format_value);
            eDDte = existing_format.parse(date_value);
            return new_format.format(eDDte);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }// end showToastMsg

    /**
     * @param
     * @return
     */

    public static Date convertStrToDate(Context context, String format, String date_value) {

        // @Logic : convert string into date value - specially for MySQL
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date_final = formatter.parse(date_value);
            return  date_final;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();

    }// end showToastMsg

    /**
     * @param
     * @return
     */

    public static void showToastMsg(Context context, String text, int toastDuration) {
        Toast.makeText(context, text, toastDuration).show();
    }// end showToastMsg


    /**
     * Get current lat and long
     */

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target.length()!=10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    /**
     * @
     */
    public static int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }// end if
        }// end for
        System.out.println("returned string " + myString);
        System.out.println("returned index " + index);
        return index;
    }// end get spinner index

    /**
     * @
     */
    public static void printv(String printable) {
        if(DEBUG) System.out.println(printable);
    }

    /**
     * @
     */

    public static boolean isNumeric(String str) {

        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();

    }// end is numeric

    /**
     * @
     */

    public static void showAlert(Context context , String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }//  end show alert

    /**
     *
     *  @ load json from asset
     */

    public static String loadJSONFromAsset(Context context, String file_name) {

        String json = null;
        try {

            InputStream is = context.getAssets().open(file_name);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }// end load json from asset


    /**
     * @ find lat and long distance between
     */

    public static double distanceBWLatLng(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}// end main utils class