package io.github.edwinvanrooij.camelraceapp;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.edwinvanrooij.camelraceshared.domain.PersonalResultItem;
import io.github.edwinvanrooij.camelraceshared.domain.Player;
import io.github.edwinvanrooij.camelraceshared.events.Event;

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

