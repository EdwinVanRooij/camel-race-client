package io.github.edwinvanrooij.camelraceapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.parceler.Parcels;

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

import static io.github.edwinvanrooij.camelraceapp.Config.GAME_ID;

public class SocketActivity extends AppCompatActivity {

    private OkHttpClient client;
    private String gameId;
    private WebSocket ws;

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
    }
    public void connectWebSocket() {
        Request request = new Request.Builder().url(Config.BACKEND_CONNECTION_URL).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
         ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
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
        Event e = new Event(Event.PLAYER_JOINED, playerJoinRequest);
        ws.send(Util.objectToJson(e));
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            EnterNameFragment f = (EnterNameFragment) getSupportFragmentManager().findFragmentByTag(EnterNameFragment.class.getSimpleName());
            if (f != null) {
                f.onConnected();
            }
            System.out.println("Open connection on client.");
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

            EnterNameFragment f = (EnterNameFragment) getSupportFragmentManager().findFragmentByTag(EnterNameFragment.class.getSimpleName());
            if (f != null) {
                f.onDisconnected();
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            System.out.println("Error : " + t.getMessage());
        }
    }
}
