package hk.com.csci4140.culife.model;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.activity.LoginActivity;
import hk.com.csci4140.culife.utility.SessionManager;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by maoyuxuan(Michael Mao) on 12/05/2018.
 */

/**
 * This class is to create message instance.
 */

public class InstantMessageModel {
    private String message;
    private String author;

    public InstantMessageModel(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public InstantMessageModel() {
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
