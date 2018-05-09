package hk.com.csci4140.culife.utility;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;

/**
 * Created by maoyuxuan(Michael Mao) on 25/04/2018.
 */

public class Utility {
    public static final String TAG = "Utility";

    private static String mDeviceToken;


    //Set the Language
    public static void setLanguage(Context mContext) {
        //读取SharedPreferences数据，默认选中第一项
        int languageSetting = SessionManager.getInt(mContext, Constant.LANGUAGE_SETTING);

        //根据读取到的数据，进行设置
        Resources resources = mContext.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();

        switch (languageSetting){
            case 0:
                Locale mSystemLanguageList[]= Locale.getAvailableLocales();
                configuration.locale = mSystemLanguageList[0];
                resources.updateConfiguration(configuration,displayMetrics);
                break;
            case 1:
                String language2 = "zh";
                String country2 = "TW";
                configuration.locale = new Locale(language2 , country2);
                resources.updateConfiguration(configuration,displayMetrics);
                break;
            case 2:
                String language = "zh";
                String country = "CN";
                configuration.locale = new Locale(language , country);
                resources.updateConfiguration(configuration,displayMetrics);
                break;
            default:
                break;
        }
    }


    //Get the language Id
    public static int getLanguageId(Context mContext){
        int id = SessionManager.getInt(mContext, Constant.LANGUAGE_SETTING);
        if(id == 0){
            Resources resources = mContext.getResources();
            Configuration configuration = resources.getConfiguration();
            String country = configuration.locale.getCountry();
            switch (country){
                case "HK":
                    id = 1;
                    break;
                case "CN":
                    id = 2;
                    break;
                default:
                    id = 1;
                    break;
            }
        }
        return id;
    }


    //Hide the keyboard
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    //Get the device token
    public static String getDeviceToken(){
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                mDeviceToken = registrationId;
            }
        });
        return mDeviceToken;
    }


    //Change "yyyy-MM-dd" String date to Date
    public static Date getLocaleDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date localeDate;
        try{
            localeDate = format.parse(date);
        }catch (Exception e){
            localeDate = new Date();
        }
        return localeDate;
    }


    //Get today date xxxx-xx-xx
    public static String getToday(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(new Date());
    }


    //Get date String
    public static String getDateString(int year, int month, int day){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        int calendarMonth = month - 1;
        Calendar c = Calendar.getInstance();
        c.set(year, calendarMonth, day);
        Date realDate = c.getTime();
        return format.format(realDate);
    }

    //Get Time String
    public static String getTimeString(int hour, int minute){
        String hourString = hour < 10 ? ("0" + hour) : Integer.toString(hour);
        String minuteString = minute < 10 ? ("0" + minute) : Integer.toString(minute);
        return hourString + ":" + minuteString;
    }


    //Calculate date after days
    public static String getDateAfterDay(String startDate, int afterDayNum){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date start = getLocaleDate(startDate);
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, afterDayNum);
        return format.format(c.getTime());
    }


    //Calculate date after months
    public static String getDateAfterMonth(String startDate, int afterMonthNum){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date start = getLocaleDate(startDate);
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.MONTH, afterMonthNum);
        return format.format(c.getTime());
    }


    //Calculate days between
    public static int getDaysBetween(String startDate, String endDate){
        Date start = getLocaleDate(startDate);
        Date end = getLocaleDate(endDate);
        long diff = end.getTime() - start.getTime();
        return (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }


    //Compare two time slot(xx:xx)
    public static boolean compareTime(String startTime, String endTime){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date start;
        Date end;
        try {
            start = format.parse(startTime);
            end = format.parse(endTime);
        }catch (Exception e){
            start = new Date();
            end = new Date();
            Log.e(TAG, e.toString());
        }
        return (start.compareTo(end) < 0);
    }


    //Compare two date slot(xxxx-xx-xx)
    public static boolean compareDate(String startDate, String endDate){
        Date start = getLocaleDate(startDate);
        Date end = getLocaleDate(endDate);
        return (start.compareTo(end) < 0);
    }


    //Change Date from xxxx-xx-xx to Chinese name
    public static String convertToChineseDate(Context context, String date){
        String [] s = date.split("-");
        int year = Integer.parseInt(s[0]);
        int month = Integer.parseInt(s[1]);
        int day = Integer.parseInt(s[2]);
        return String.format(context.getString(R.string.date_format_chinese), year, month, day);
    }


    //Load a bitmap from view
    public static Bitmap loadBitmapFromBubbleView(View v) {
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.layout_container) ;
        linearLayout.setDrawingCacheEnabled(true);
        linearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearLayout.layout(0, 0, linearLayout.getMeasuredWidth(), linearLayout.getMeasuredHeight());
        linearLayout.buildDrawingCache(true);
        return linearLayout.getDrawingCache();
    }


    //Check if string has at least one a-z or A-Z
    public static boolean judgeContainsStr(String cardNum) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

}
