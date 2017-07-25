package io.github.edwinvanrooij.camelraceapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.BuildConfig;
import io.github.edwinvanrooij.camelraceapp.Const;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceapp.Util;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.CamelRaceSocketActivity;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.etGameId)
    EditText etGameId;

    private OkHttpClient client;
    private String gameId;
    private WebSocket ws;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = new OkHttpClient();

        connectWebSocket();
    }

    public void connectWebSocket() {
        try {
            String url = BuildConfig.BACKEND_BASE_CONNECTION_URL + Const.DEFAULT_ENDPOINT_CLIENT;
            Request request = new Request.Builder().url(url).build();
            System.out.println(String.format("Connecting to url: %s", url));
            EchoWebSocketListener listener = new EchoWebSocketListener();
            ws = client.newWebSocket(request, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        System.out.println("Connected");
    }

    public void onDisconnected() {
        System.out.println("Disconnected");
    }

    public void handleMessage(String message, WebSocket socket) {
        try {
            Event event = Util.jsonToEvent(message);

            switch (event.getEventType()) {
                case Event.KEY_GAME_TYPE: {
//                    resultItem = (PersonalResultItem) event.getValue();
                    String gameType = (String) event.getValue();
                    switch (gameType) {
                        case Const.KEY_GAME_TYPE_CAMEL_RACE:
                            startGame(CamelRaceSocketActivity.class);
                            break;
                        case Const.KEY_GAME_TYPE_MEXICAN:
//                            startGame(CamelRaceSocketActivity.class);
                            Toast.makeText(activity, "Starting mexican game!", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            throw new Exception(String.format("Could not determine the correct game type from '%s'", gameType));
                    }
//                    onGameEnded();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEvent(String eventType, Object value, WebSocket ws) {
        Event event = new Event(eventType, value);
        String message = Util.objectToJson(event);
        ws.send(message);
        System.out.println(String.format("Sending: %s", message));
    }

    @OnClick(R.id.btnJoin)
    public void onButtonJoinClick() {
        gameId = etGameId.getText().toString();
        sendEvent(Event.KEY_WHICH_GAME_TYPE, gameId, ws);
        Toast.makeText(activity, getResources().getString(R.string.waiting), Toast.LENGTH_SHORT).show();
    }

    private void startGame(Class gameClass) {
        Intent intent = new Intent(getActivity(), gameClass);

        intent.putExtra(Const.KEY_GAME_ID, Parcels.wrap(gameId));

        startActivity(intent);
    }
}
