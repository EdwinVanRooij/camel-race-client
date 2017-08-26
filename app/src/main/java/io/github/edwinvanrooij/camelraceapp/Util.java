package io.github.edwinvanrooij.camelraceapp;


import com.google.gson.Gson;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Util {

    private static Gson gson = new Gson();

    public static String objectToJson(Object obj) {
        return gson.toJson(obj);
    }
}

