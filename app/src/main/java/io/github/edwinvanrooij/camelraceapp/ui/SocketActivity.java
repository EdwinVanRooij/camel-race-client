package io.github.edwinvanrooij.camelraceapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.edwinvanrooij.camelraceapp.R;

import io.github.edwinvanrooij.camelraceapp.Config;
import io.github.edwinvanrooij.camelraceapp.Util;
import io.github.edwinvanrooij.camelraceshared.domain.Bid;
import io.github.edwinvanrooij.camelraceshared.domain.Player;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import io.github.edwinvanrooij.camelraceshared.events.PlayAgainRequest;
import io.github.edwinvanrooij.camelraceshared.events.PlayerAliveCheck;
import io.github.edwinvanrooij.camelraceshared.events.PlayerJoinRequest;
import io.github.edwinvanrooij.camelraceshared.events.PlayerNewBid;
import io.github.edwinvanrooij.camelraceshared.events.PlayerNotReady;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static io.github.edwinvanrooij.camelraceapp.Config.CONNECTED_CHECKER_INTERVAL;
import static io.github.edwinvanrooij.camelraceapp.Config.GAME_ID;

public class SocketActivity extends AppCompatActivity {

    @BindView(R.id.tvTitleConnect)
    TextView tvTitleConnect;

    private OkHttpClient client;
    private String gameId;
    private WebSocket ws;
    private boolean connected = false;
    private Player player;

    private Bid currentBid;
    private Bid newBid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        ButterKnife.bind(this);

        client = new OkHttpClient();

        gameId = Parcels.unwrap(getIntent().getParcelableExtra(GAME_ID));

        setFragment(EnterNameFragment.class, false);

        connectWebSocket();
//        startConnectedChecker();
    }

//    private void startConnectedChecker() {
//        final Handler h = new Handler();
//        final int delay = CONNECTED_CHECKER_INTERVAL * 1000;
//
//        h.postDelayed(new Runnable() {
//            public void run() {
//                //do something
//                if (!connected) {
//                    System.out.println("Reconnecting");
//                    reconnect();
//                } else  {
//                    System.out.println("Not reconnecting, connected is still true");
//                }
//
//                h.postDelayed(this, delay);
//            }
//        }, delay);
//    }

//    private void reconnect() {
//        connectWebSocket();
//        if (player != null) {
//            onSubmitUsername(player.getName());
//        }
//    }

    public void connectWebSocket() {
        try {
            Request request = new Request.Builder().url(Config.BACKEND_CONNECTION_URL).build();
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
        sendMessage(Event.KEY_PLAYER_JOIN, playerJoinRequest, ws);
    }

    public void onSubmitBid(Bid bid) {
        newBid = bid;
        PlayerNewBid playerNewBid = new PlayerNewBid(gameId, player, bid);
        sendMessage(Event.KEY_PLAYER_NEW_BID, playerNewBid, ws);
    }

    public void onPlayAgain() {
        PlayAgainRequest playAgainRequest = new PlayAgainRequest(gameId, player);
        sendMessage(Event.KEY_PLAY_AGAIN, playAgainRequest, ws);
    }

    public void onNotReadyClick() {
        sendMessage(Event.KEY_PLAYER_NOT_READY, new PlayerNotReady(gameId, player), ws);
    }

    public void onReady(Bid bid) {
        newBid = bid;
        PlayerNewBid playerNewBid = new PlayerNewBid(gameId, player, bid);
        sendMessage(Event.KEY_PLAYER_READY, playerNewBid, ws);
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

    public void onConnected() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        tvTitleConnect.setText(R.string.connected);
                        tvTitleConnect.setTextColor(Color.GREEN);
                        connected = true;
                    }
                }
        );
    }

    public void onDisconnected() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        tvTitleConnect.setText(R.string.disconnected);
                        tvTitleConnect.setTextColor(Color.RED);
                        connected = false;
                    }
                }
        );
    }

    public void handleMessage(String message, WebSocket socket) {
        try {
            Event event = Util.jsonToEvent(message);

            switch (event.getEventType()) {

                case Event.KEY_PLAYER_JOINED: {
                    Player player = (Player) event.getValue();
                    this.player = player;
                    System.out.println(String.format("Got %s back!", player));
                    onPlayerJoinedSuccessfully();
                    break;
                }

                case Event.KEY_PLAYER_BID_HANDED_IN: {
                    Boolean succeeded = (Boolean) event.getValue();
                    onHandedBid(succeeded);
                    break;
                }

                case Event.KEY_PLAYER_READY_SUCCESS: {
                    Boolean succeeded = (Boolean) event.getValue();
                    onHandedBid(succeeded);
                    onReadySuccess(succeeded);
                    break;
                }

                case Event.KEY_PLAYER_NOT_READY_SUCCESS: {
                    Boolean succeeded = (Boolean) event.getValue();
                    onNotReadySuccess(succeeded);
                    break;
                }

                case Event.KEY_GAME_STARTED: {
                    onGameStarted();
                    break;
                }

                case Event.KEY_PLAYER_ALIVE_CHECK_CONFIRMED: {
                    Boolean confirmed = (Boolean) event.getValue();
                    onAliveConfirmed(confirmed);
                    break;
                }

                case Event.KEY_GAME_OVER_PERSONAL_RESULTS: {
                    onGameEnded();
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onHandedBid(boolean successful) {
        if (successful) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentBid = newBid;
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.successful_bid), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.unsuccessful_bid), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void onAliveConfirmed(boolean successful) {
        if (successful) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.alive_confirmed), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
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
            setFragment(ReadyFragment.class, false);
            onHandedBid(true);
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
            setFragment(BidFragment.class, false);
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

    public Bid getBid() {
        return currentBid;
    }

    private void onGameStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setFragment(RacingFragment.class, false);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.game_started), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onGameEnded() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RacingFragment racingFragment = (RacingFragment) getSupportFragmentManager().findFragmentByTag(RacingFragment.class.getSimpleName());
                racingFragment.setBtnPlayAgain();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.game_ended), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onPlayerJoinedSuccessfully() {
        setFragment(BidFragment.class, false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.successful_join), Toast.LENGTH_SHORT).show();
            }
        });

        int interval = 2; // in seconds
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendMessage(Event.KEY_PLAYER_ALIVE_CHECK, new PlayerAliveCheck(gameId, player), ws);
            }
        };
        timer.schedule(task, 0, interval * 1000);
    }

    private void sendMessage(String eventType, Object value, WebSocket ws) {
        Event event = new Event(eventType, value);
        String message = Util.objectToJson(event);
        ws.send(message);
        System.out.println(String.format("Sending: %s", message));
    }
}
