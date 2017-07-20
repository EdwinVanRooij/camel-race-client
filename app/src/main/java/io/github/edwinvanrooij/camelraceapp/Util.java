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
    private static JsonParser parser = new JsonParser();

    public static String objectToJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Event jsonToEvent(String json) throws Exception {
        JsonObject wholeJson = parser.parse(json).getAsJsonObject();

        String type = wholeJson.get(Event.KEY_TYPE).getAsString();

        Event event = new Event(type);
        switch (type) {

            case Event.KEY_PLAYER_ALIVE_CHECK_CONFIRMED:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_VALUE), Boolean.class)
                );
                break;
            case Event.KEY_PLAYER_JOINED:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_VALUE).getAsJsonObject().toString(), Player.class)
                );
                break;
            case Event.KEY_PLAY_AGAIN_SUCCESSFUL:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_VALUE), Boolean.class)
                );
                break;
            case Event.KEY_PLAYER_READY_SUCCESS:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_VALUE), Boolean.class)
                );
                break;
            case Event.KEY_PLAYER_NOT_READY_SUCCESS:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_VALUE), Boolean.class)
                );
                break;
            case Event.KEY_GAME_STARTED:
                // N/A
                break;
            case Event.KEY_GAME_OVER_PERSONAL_RESULTS:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_VALUE).getAsJsonObject().toString(), PersonalResultItem.class)
                );
                break;
            case Event.KEY_PLAYER_BID_HANDED_IN:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_VALUE), Boolean.class)
                );
                break;

            default:
                throw new Exception(String.format("No suitable event found for:\r\nType '%s'\r\nWhole json: '%s'", type, wholeJson.toString()));
        }
        return event;
    }
}

