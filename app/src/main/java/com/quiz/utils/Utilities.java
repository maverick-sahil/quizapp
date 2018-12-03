package com.quiz.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;


public class Utilities {
    public static boolean hideKeyBoad(Context context, View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return true;
    }

    // validating email id
    public static boolean isValidEmaillId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String getRandomCharacter() {
        Random rnd = new Random();
        String[] chars = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String strChar = chars[(int) (Math.random() * 10)];
        return strChar;
    }


    public static byte[] convertDocumentToByteArray(String sourcePath) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(sourcePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;

    }

    // Converting File to Base64.encode String type using Method
    public static String convertDocumentToBase64(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;

        return lastVal;
    }


    public static String getFormatedString(String strTemp) {
        String strActual = "";

        if (strTemp.length() == 1) {
            strActual = "0" + strTemp;
        } else if (strTemp.length() == 2) {
            strActual = strTemp;
        }

        return strActual;
    }

    public static String getRoleWithFormat(String strRole) {
        String strActualRole = "";
        if (strRole.equals("User")) {
            strActualRole = "user";
        } else if (strRole.equals("Staff")) {
            strActualRole = "staff";
        } else if (strRole.equals("Admin")) {
            strActualRole = "admin";
        }

        return strActualRole;
    }


    //create bitmap from the ScrollView
    public static Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    //Getting Month Name From the Number
    public static String getMonthNameFromNumber(String strNumber) {
        if (strNumber.equals("1"))
            return "January";
        else if (strNumber.equals("2"))
            return "February";
        else if (strNumber.equals("3"))
            return "March";
        else if (strNumber.equals("4"))
            return "April";
        else if (strNumber.equals("5"))
            return "May";
        else if (strNumber.equals("6"))
            return "June";
        else if (strNumber.equals("7"))
            return "July";
        else if (strNumber.equals("8"))
            return "August";
        else if (strNumber.equals("9"))
            return "September";
        else if (strNumber.equals("10"))
            return "October";
        else if (strNumber.equals("11"))
            return "November";
        else if (strNumber.equals("12"))
            return "December";


        return null;
    }


    //Add Days in the Date and getting New Date:
    public static String getNewDateFormatWhenAddDays(String strStartDate, int numofDays) {
        String strNewDateFormat = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(strStartDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, numofDays);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMMM yyyy");
        strNewDateFormat = sdf1.format(c.getTime());


        return strNewDateFormat;
    }

    public static String gettingLongToFormatedTime(Long strDateTime) {

        String strTime = "" + strDateTime;
        strTime = strTime.replace("-", "");
        Log.e("Utilities", "======TIME IN MILLI=====" + strTime);
        //convert seconds to milliseconds
        Date date = new Date(Long.parseLong(strTime) * 1000);
        // format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("MMM dd, yyyy");
        String java_date = jdf.format(date);

        return java_date;
    }

    public static String gettingDoubleToFormatedTime(Double strDateTime) {
        String strTime = "" + strDateTime;
        strTime = strTime.replace("-", "");
        Log.e("Utilities", "======TIME IN MILLI=====" + strTime);
        double mtemp = Double.parseDouble(strTime) * 1000;

        long mLongMillis = (long) mtemp;
        //convert seconds to milliseconds
        Date date = new Date(mLongMillis);
        // format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("MMM dd, yyyy");
        String java_date = jdf.format(date);

        return java_date;
    }

    public static String gettingTimeFormat(String strDate) throws ParseException {
        String strActualFormat = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm ZZZ");
        Date d = format.parse(strDate);
        format = new SimpleDateFormat("MMM dd, yyyy");
        strActualFormat = format.format(d);
        return strActualFormat;
    }


    //Convert ByteArray to Base64:
    public static String convertByteArrayToBase64(byte[] imgByteArray) {
        return Base64.encodeToString(imgByteArray, Base64.DEFAULT);
    }


    public static String extractYoutubeId(String url) throws MalformedURLException {
        String query = new URL(url).getQuery();
        String[] param = query.split("&");
        String id = null;
        for (String row : param) {
            String[] param1 = row.split("=");
            if (param1[0].equals("v")) {
                id = param1[1];
            }
        }
        return id;
    }


    public static double getCurrentTimeStamp() {
        double currentEpachoTime = 0;
        try {
            long millis = System.currentTimeMillis();
            currentEpachoTime = millis / 1000;
            currentEpachoTime = currentEpachoTime * -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("Utitlities", "===Negative Time====" + currentEpachoTime);
        return currentEpachoTime;
    }


    public static String getCurrentTimeStampComment() {
        String formattedDate = "";
        try {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm ZZZ");
            formattedDate = df.format(c);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formattedDate;
    }


    public static long getTimeInLong(String mDate) {
        long startDate = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm ZZZ");
            Date date = sdf.parse(mDate);

            startDate = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startDate;
    }


    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static long getMilliseconds(String time) {
        String[] units = time.split(":"); //will break the string up into an array
        int hours = Integer.parseInt(units[0]); //first element
        int minutes = Integer.parseInt(units[1]); //first element
        int seconds = Integer.parseInt(units[2]); //second element
        int duration = 3600 * hours + 60 * minutes + seconds; //add up our values
        return duration;
    }

}