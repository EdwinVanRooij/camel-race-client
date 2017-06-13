package io.github.edwinvanrooij.camelraceapp;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.edwinvanrooij.camelraceshared.domain.Player;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import io.github.edwinvanrooij.camelraceshared.events.GameStart;
import io.github.edwinvanrooij.camelraceshared.events.PersonalResults;
import io.github.edwinvanrooij.camelraceshared.events.PlayerJoinRequest;
import io.github.edwinvanrooij.camelraceshared.events.PlayerNewBid;

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
        System.out.println(String.format("Whole: %s", wholeJson.toString()));

        String type = wholeJson.get(Event.KEY_EVENT_TYPE).getAsString();
        System.out.println(String.format("Type: %s", type));

        Event event = new Event(type);
        switch (type) {
            // region Server side
            case Event.KEY_GAME_CREATE:
                // N/A
                break;
            case Event.KEY_PLAYER_JOIN:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), PlayerJoinRequest.class)
                );
                break;
            case Event.KEY_PLAYER_NEW_BID:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), PlayerNewBid.class)
                );
                break;
            case Event.KEY_GAME_START:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), GameStart.class)
                );
                break;
            // endregion

            // region Client side
            case Event.KEY_PLAYER_JOINED:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), Player.class)
                );
                break;
            case Event.KEY_GAME_STARTED:
                // N/A
                break;
            case Event.KEY_GAME_OVER_PERSONAL_RESULTS:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), PersonalResults.class)
                );
                break;
            case Event.KEY_PLAYER_BID_HANDED_IN:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE), Boolean.class)
                );
                break;
            // endregion
            default:
                throw new Exception(String.format("No suitable event found for:\r\nType '%s'\r\nWhole json: '%s'", type, wholeJson.toString()));
        }
        return event;
    }
}

