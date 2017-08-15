package io.github.edwinvanrooij.camelraceapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.channels.AlreadyConnectedException;

import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceapp.Util;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Author: eddy
 * Date: 21-1-17.
 */

public abstract class BaseSocketActivity extends AppCompatActivity {

    protected OkHttpClient client;
    protected WebSocket ws;
    protected JsonParser parser;
    protected Gson gson;
    protected String url;

    @Override
    protected void onStart() {
        super.onStart();
        setWebSocketUrl();
        connectWebSocket();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new OkHttpClient();
        parser = new JsonParser();
        gson = new Gson();
    }

    protected abstract void setWebSocketUrl();

    public void connectWebSocket() {
        try {
            Request request = new Request.Builder().url(url).build();
            EchoWebSocketListener listener = new EchoWebSocketListener();
            ws = client.newWebSocket(request, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("JavaDoc")
    public void setFragment(Class fragmentClass, boolean addToStack) {
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            // Add this transaction to the back stack
            if (addToStack) {
                ft.addToBackStack(null);
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.flContent, fragment, fragmentClass.getSimpleName()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("Open connection on client.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onConnected();
                }
            });
        }

        @Override
        public void onMessage(final WebSocket webSocket, final String text) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Receiving : " + text);
                        handleMessage(text, webSocket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, final ByteString bytes) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Receiving bytes : " +
                            bytes.hex());
                }
            });
        }

        @Override
        public void onClosing(final WebSocket webSocket, final int code, final String reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webSocket.close(NORMAL_CLOSURE_STATUS, null);
                    System.out.println("Closing : " + code + " / " + reason);

                    onDisconnected();
                }
            });
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            try {
                System.out.println("Error : " + t.getMessage());
                throw t;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public void onConnected() {
//        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        System.out.println("Connected");
    }

    public void onDisconnected(){
        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
    }

    protected abstract boolean handleEvent(String event, JsonObject json) throws Exception;

    private void handleMessage(String message, WebSocket socket) {
        try {
            JsonObject json = parser.parse(message).getAsJsonObject();
            String event = json.get(Event.KEY_TYPE).getAsString();
            handleEvent(event, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void sendEvent(final String event, final Object value) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Event e = new Event(event, value);
                        String message = Util.objectToJson(e);
                        ws.send(message);
                        System.out.println(String.format("Sending: %s", message));
                    }
                }
        );
    }
}
