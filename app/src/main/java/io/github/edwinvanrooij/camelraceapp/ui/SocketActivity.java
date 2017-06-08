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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.edwinvanrooij.camelraceapp.R;

import io.github.edwinvanrooij.camelraceapp.Config;
import io.github.edwinvanrooij.camelraceapp.domain.Event;
import io.github.edwinvanrooij.camelraceapp.domain.Player;
import io.github.edwinvanrooij.camelraceapp.domain.PlayerJoinRequest;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        ButterKnife.bind(this);

        client = new OkHttpClient();

        gameId = Parcels.unwrap(getIntent().getParcelableExtra(GAME_ID));
        Toast.makeText(this, String.format("Game ID: %s", gameId), Toast.LENGTH_SHORT).show();

        setFragment(EnterNameFragment.class, false);

        connectWebSocket();
        startConnectedChecker();
    }

    private void startConnectedChecker() {
        final Handler h = new Handler();
        final int delay = CONNECTED_CHECKER_INTERVAL * 1000;

        h.postDelayed(new Runnable() {
            public void run() {
                //do something
                if (!connected) {
                    System.out.println("Reconnecting");
                    reconnect();
                } else  {
                    System.out.println("Not reconnecting, connected is still true");
                }

                h.postDelayed(this, delay);
            }
        }, delay);
    }

    private void reconnect() {
        connectWebSocket();
        if (player != null) {
            onSubmitUsername(player.getName());
        }
    }

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
        this.player = player;
        PlayerJoinRequest playerJoinRequest = new PlayerJoinRequest(gameId, player);
        Event e = new Event(Event.PLAYER_JOINED, playerJoinRequest);
        ws.send(Util.objectToJson(e));
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

}
