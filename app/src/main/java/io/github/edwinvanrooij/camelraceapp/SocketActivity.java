package io.github.edwinvanrooij.camelraceapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.parceler.Parcels;

import butterknife.ButterKnife;

import static io.github.edwinvanrooij.camelraceapp.Constants.KEY_URL;

public class SocketActivity extends AppCompatActivity {

    private String url;
    private static final String TAG = "SocketActivity";
//    private final WebSocketConnection mConnection = new WebSocketConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        ButterKnife.bind(this);

        // Get the url from previous activity
        url = Parcels.unwrap(getIntent().getParcelableExtra(KEY_URL));
        Toast.makeText(this, String.format("URL is: %s", url), Toast.LENGTH_SHORT).show();

        setFragment(EnterNameFragment.class, false);

//        connectWebSocket();
    }

    private void onMessageReceived(String s) {
//        System.out.println(String.format("Received: %s", s));
//
//        Event e = Event.deserialize(s);
//
//        switch (e.getName()) {
//            case "gameStarted":
//                setFragment(WaitFragment.class, false);
//                break;
//
//            case "nextRound":
//                setFragment(CardPickFragment.class, false);
//                break;
//
//            case "allVoted":
//                setFragment(Wait2Fragment.class, false);
//                break;
//
//            default:
//                System.out.println("Could not determine name");
//        }
    }

//    public void sendMessageThroughSocket(String string) {
//        runOnUiThread(() -> {
//            mConnection.sendTextMessage(string);
//        });
//    }
//
//    /**
//     * All of the socket communication is received and finally sent in this method, callbacks are registerd here
//     * This code points to other methods based on the messages received
//     */
//    private void connectWebSocket() {
//        String wsuri = String.format("ws://%s:%s/client", BACKEND_IP, BACKEND_PORT);
//
//        try {
//            mConnection.connect(wsuri, new WebSocketHandler() {
//
//                @Override
//                public void onOpen() {
//                    Log.d(TAG, "Status: Connected to " + wsuri);
//
//                    sendMessageThroughSocket(String.format("gameId;\"%s\"", url));
//                }
//
//                @Override
//                public void onTextMessage(String payload) {
//                    onMessageReceived(payload);
//                }
//
//                @Override
//                public void onClose(int code, String reason) {
//                    Log.d(TAG, String.format("Connection lost, reason: %s", reason));
//                }
//            });
//        } catch (de.tavendo.autobahn.WebSocketException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Called when host is done with entering required information (entering user stories)
//     */
//    private void onHostIsDone() {
//        setFragment(CardPickFragment.class, false);
//    }
//
//    /**
//     * Called when all players are ready to start the game
//     */
//    private void onEveryoneReady() {
//        setFragment(WaitFragment.class, false);
//    }

    /**
     * Call this to change the fragment
     *
     * @param fragmentClass
     * @param addToStack
     */
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
            ft.replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
