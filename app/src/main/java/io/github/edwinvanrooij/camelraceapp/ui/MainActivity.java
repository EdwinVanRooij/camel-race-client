package io.github.edwinvanrooij.camelraceapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import io.github.edwinvanrooij.camelraceapp.BuildConfig;
import io.github.edwinvanrooij.camelraceapp.Const;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.CamelRaceSocketActivity;
import io.github.edwinvanrooij.camelraceapp.ui.mexican.MexicanSocketActivity;
import io.github.edwinvanrooij.camelraceshared.events.Event;

public class MainActivity extends BaseSocketActivity {

    private static final String TAG = "MainActivity";
    private String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setFragment(MainFragment.class, false);
    }

    @Override
    protected void setWebSocketUrl() {
        url = BuildConfig.BACKEND_BASE_CONNECTION_URL + Const.DEFAULT_ENDPOINT_CLIENT;
    }

    @Override
    public void onConnected() {
        Log.i(TAG, "onConnected: ws connected");
    }
    @Override
    public void onDisconnected() {
        Log.i(TAG, "onDisconnected: ws disconnected");
    }

    @Override
    protected boolean handleEvent(String event, JsonObject json) throws Exception {
        switch (event) {
            case Event.KEY_GAME_TYPE: {
                String gameType = json.get(Event.KEY_VALUE).getAsString();

                switch (gameType) {
                    case Const.KEY_GAME_TYPE_CAMEL_RACE:
                        onCamelRaceGameType();
                        break;
                    case Const.KEY_GAME_TYPE_MEXICAN:
                        onMexicanGameType();
                        break;
                    default:
                        throw new Exception("Could not determine a correct game type.");
                }
                return true;
            }

            default:
                throw new Exception("Could not determine a correct event type for client message.");
        }
    }

    private void onMexicanGameType() {
        System.out.println("On mexican gametype");
        startGame(MexicanSocketActivity.class);
    }

    private void onCamelRaceGameType() {
        System.out.println("On camel gametype");
        startGame(CamelRaceSocketActivity.class);
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    private void startGame(Class gameClass) {
        Intent intent = new Intent(this, gameClass);

        intent.putExtra(Const.KEY_GAME_ID, Parcels.wrap(gameId));

        startActivity(intent);
    }
}
