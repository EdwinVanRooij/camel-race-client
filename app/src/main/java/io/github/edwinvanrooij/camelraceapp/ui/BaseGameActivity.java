package io.github.edwinvanrooij.camelraceapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.parceler.Parcels;

import java.util.Timer;
import java.util.TimerTask;

import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceapp.Util;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.BidFragmentCamelRace;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.RacingFragmentCamelRace;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.ReadyFragmentCamelRace;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.ResultFragmentCamelRace;
import io.github.edwinvanrooij.camelraceshared.domain.PlayAgainRequest;
import io.github.edwinvanrooij.camelraceshared.domain.Player;
import io.github.edwinvanrooij.camelraceshared.domain.PlayerAliveCheck;
import io.github.edwinvanrooij.camelraceshared.domain.PlayerJoinRequest;
import io.github.edwinvanrooij.camelraceshared.domain.PlayerNotReady;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static io.github.edwinvanrooij.camelraceapp.Const.KEY_GAME_ID;

/**
 * Author: eddy
 * Date: 21-1-17.
 */

public abstract class BaseGameActivity extends BaseSocketActivity {

    protected OkHttpClient client;
    protected String gameId;
    protected WebSocket ws;
    protected Timer timer;
    protected Player player;
    protected JsonParser parser;
    protected Gson gson;

    protected void initVariables() {
        gameId = Parcels.unwrap(getIntent().getParcelableExtra(KEY_GAME_ID));
        client = new OkHttpClient();
        parser = new JsonParser();
        gson = new Gson();
    }

    public void connectWebSocket(String url) {
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

    public void onSubmitUsername(String username) {
        Player player = new Player(username);
        PlayerJoinRequest playerJoinRequest = new PlayerJoinRequest(gameId, player);
        sendEvent(Event.KEY_PLAYER_JOIN, playerJoinRequest, ws);
    }

    public void onPlayAgain() {
        PlayAgainRequest playAgainRequest = new PlayAgainRequest(gameId, player);
        sendEvent(Event.KEY_PLAY_AGAIN, playAgainRequest, ws);
    }

    public void onNotReadyClick() {
        sendEvent(Event.KEY_PLAYER_NOT_READY, new PlayerNotReady(gameId, player), ws);
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("Open connection on client.");
            onConnected();
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("Receiving : " + text);
            handleMessage(text, webSocket);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            System.out.println("Closing : " + code + " / " + reason);

            onDisconnected();
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            System.out.println("Error : " + t.getMessage());
        }
    }

    public abstract void onConnected();

    public abstract void onDisconnected();

    protected boolean handleEvent(String event, JsonObject json) throws Exception {
        switch (event) {
            case Event.KEY_PLAYER_JOINED: {
                this.player = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), Player.class);
                onPlayerJoinedSuccessfully();
                return true;
            }
            case Event.KEY_PLAYER_NOT_READY_SUCCESS: {
                Boolean succeeded = gson.fromJson(json.get(Event.KEY_VALUE), Boolean.class);
                onNotReadySuccess(succeeded);
                return true;
            }
            case Event.KEY_PLAYER_READY_SUCCESS: {
                Boolean succeeded = gson.fromJson(json.get(Event.KEY_VALUE), Boolean.class);
                onReadySuccess(succeeded);
                return true;
            }
            case Event.KEY_GAME_STARTED: {
                onGameStarted();
                return true;
            }
            case Event.KEY_PLAY_AGAIN_SUCCESSFUL: {
                Boolean success = gson.fromJson(json.get(Event.KEY_VALUE), Boolean.class);
                onPlayAgain();
                return true;
            }
            case Event.KEY_PLAYER_ALIVE_CHECK_CONFIRMED: {
                Boolean confirmed = gson.fromJson(json.get(Event.KEY_VALUE), Boolean.class);
                onAliveConfirmed(confirmed);
                return true;
            }

            default:
                return false;
        }
    }

    public void handleMessage(String message, WebSocket socket) {
        try {
            JsonObject json = parser.parse(message).getAsJsonObject();
            String event = json.get(Event.KEY_TYPE).getAsString();

            handleEvent(event, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onAliveConfirmed(boolean successful) {
        if (!successful) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.alive_failed), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void onReadySuccess(boolean successful) {
        if (successful) {
            setFragment(ReadyFragmentCamelRace.class, false);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.successful_ready), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.unsuccessful_ready), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void onNotReadySuccess(boolean successful) {
        if (successful) {
            setFragment(BidFragmentCamelRace.class, false);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.successful_not_ready), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.unsuccessful_not_ready), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void onGameStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setFragment(RacingFragmentCamelRace.class, false);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.game_started), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        System.out.println("Timer is now canceled");
        if (timer != null) {
            timer.cancel();
        }
    }

    private void onGameEnded() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setFragment(ResultFragmentCamelRace.class, false);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.game_ended), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onPlayerJoinedSuccessfully() {
        setFragment(BidFragmentCamelRace.class, false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.successful_join), Toast.LENGTH_SHORT).show();
            }
        });

        if (timer != null) {
            timer.cancel();
        }
        int interval = 2; // in seconds
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendEvent(Event.KEY_PLAYER_ALIVE_CHECK, new PlayerAliveCheck(gameId, player), ws);
            }
        };
        timer.schedule(task, 0, interval * 1000);
    }

    private void sendEvent(String eventType, Object value, WebSocket ws) {
        Event event = new Event(eventType, value);
        String message = Util.objectToJson(event);
        ws.send(message);
        System.out.println(String.format("Sending: %s", message));
    }
}
