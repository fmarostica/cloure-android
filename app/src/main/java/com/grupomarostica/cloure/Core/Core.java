package com.grupomarostica.cloure.Core;

import android.support.v7.app.AppCompatActivity;

import com.grupomarostica.cloure.Modules.countries.Country;
import com.grupomarostica.cloure.Modules.users.User;

import org.json.JSONArray;

import java.util.ArrayList;

public class Core {
    public static String appToken = "";
    public static String userToken = "";
    public static JSONArray modulesGroupsArr;
    public static User loguedUser;
    public static String accountType = "";
    public static String primaryDomain = "";
    public static boolean savedData = false;
    public static boolean finishedByBackButton = false;
    public static boolean servicePurchaseBinded = false;
    public static String versionName = "";
    public static String lang = "";

    public static final String API_ERROR_TAG = "CLOURE_API_ERROR_TAG";
    public static final String API_INVOKED = "CLOURE_API_INVOKED";
    public static final String API_BILLING_TAG = "CLOURE_BILLING";
    public static final String API_INFORMATION_TAG = "CLOURE_INFORMATION";
}
