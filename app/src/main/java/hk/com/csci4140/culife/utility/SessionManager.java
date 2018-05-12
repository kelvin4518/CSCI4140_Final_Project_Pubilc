package hk.com.csci4140.culife.utility;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by maoyuxuan(Michael Mao) on 26/04/2018.
 */

/**
 * Session Manager is used to save some data that need to be written in app file.
 */
public class SessionManager {
    private static final String TAG = SessionManager.class.getName();

    private static SharedPreferences sharedPreferences;


    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(SessionManager.TAG, Context.MODE_PRIVATE);
    }



    //Boolean
    public static void putBoolean (Context context,String key , boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context,String key){
        //		return getSp(context).getBoolean(key, false);
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context,String key , boolean defVal){
        return sharedPreferences.getBoolean(key, defVal);
    }

    //==============================================


    //String
    public static void putString (Context context,String key , String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String getString(Context context,String key){
        return getString(context, key, "");
    }

    public static String getString(Context context,String key , String defVal){
        return sharedPreferences.getString(key, defVal);
    }

    //==============================================


    //Int
    public static void putInt (Context context,String key , int value){
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static int getInt(Context context,String key){
        return getInt(context, key , 0 );
    }

    public static int getInt(Context context,String key , int defVal){
        return sharedPreferences.getInt(key, defVal);
    }

    //==============================================

    //String Set
    public static void putStringSet (Context context,String key, Set<String> set){
        sharedPreferences.edit().putStringSet(key, set).apply();
    }

    public static Set<String> getStringSet(Context context, String key){
        return sharedPreferences.getStringSet(key,null);
    }

    public static Set<String> getStringSet(Context context,String key , Set<String> set){
        return sharedPreferences.getStringSet(key,set);
    }
    //==============================================


    //Array List
    public static void putArrayList(ArrayList<String> al, String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, al.size());
        for (int i = 0; i < al.size(); i++)
        {
            editor.putString(key+i, al.get(i));
        }
        editor.apply();
    }

    public static void putArrayList(String key,ArrayList<Integer> al){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, al.size());
        for (int i = 0; i < al.size(); i++)
        {
            editor.putString(key+i, al.get(i)+"");
        }
        editor.apply();
    }

    public static ArrayList<String> getArrayList(String key) {
        ArrayList<String> environmentList = new ArrayList<String>();
        int environNums = sharedPreferences.getInt(key, 0);
        for (int i = 0; i < environNums; i++) {
            String environItem = sharedPreferences.getString(key + i, null);
            environmentList.add(environItem);
        }
        return environmentList;
    }



    // Michael added : array list of map
//    public static void putMapArrayList(){
////        sharedPreferences.get
//    }
//
//    public static ArrayList<Map<String,String>> getMapArrayList(String key){
//        ArrayList<Map<String,String>> environmentList = new ArrayList<Map<String,String>>();
//        int environNums = sharedPreferences.getInt(key, 0);
//        for (int i = 0; i < environNums; i++) {
//            String environItem = sharedPreferences.g(key + i, null);
//            environmentList.add(environItem);
//        }
//        return environmentList;
//    }








    //==============================================
}
