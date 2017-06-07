package io.github.edwinvanrooij.camelraceapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterNameFragment extends Fragment {

    @BindView(R.id.etUsername)
    EditText etUsername;

    public EnterNameFragment() {
        // Required empty public constructor
    }

    private Unbinder unbinder;

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_name, container, false);

        unbinder = ButterKnife.bind(this, view);
        client = new OkHttpClient();

        return view;
    }

    private OkHttpClient client;

    private void start() {
        Request request = new Request.Builder().url("ws://192.168.5.115:8082/client").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private void output(final String txt) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), txt, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btnConfirm)
    public void onButtonConfirmClick() {
        start();

        System.out.println("Clicked button confirm");
        String username = etUsername.getText().toString();

        Toast.makeText(getContext(), String.format("Username is: %s", username), Toast.LENGTH_SHORT).show();

        SocketActivity activity = (SocketActivity) getActivity();

//        activity.sendMessageThroughSocket(String.format("join;\"%s\"", username));

//        activity.setFragment(ReadyFragment.class, false);
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Hello, it's SSaurel !");
            webSocket.send("What's up ?");
            webSocket.send(ByteString.decodeHex("deadbeef"));
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }
}
