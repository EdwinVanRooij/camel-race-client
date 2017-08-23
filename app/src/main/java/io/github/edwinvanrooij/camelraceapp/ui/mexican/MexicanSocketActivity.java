package io.github.edwinvanrooij.camelraceapp.ui.mexican;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.seismic.ShakeDetector;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.edwinvanrooij.camelraceapp.BuildConfig;
import io.github.edwinvanrooij.camelraceapp.Const;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceapp.ui.BaseGameActivity;
import io.github.edwinvanrooij.camelraceshared.domain.mexican.MexicanGame;
import io.github.edwinvanrooij.camelraceshared.domain.mexican.NewPlayerThrow;
import io.github.edwinvanrooij.camelraceshared.domain.mexican.PlayerGameModeVote;
import io.github.edwinvanrooij.camelraceshared.events.Event;

public class MexicanSocketActivity extends BaseGameActivity implements ShakeDetector.Listener {


    @BindView(R.id.tvTitleConnect)
    TextView tvTitleConnect;

    private boolean yourTurn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        ButterKnife.bind(this);

        tvTitleConnect.setText(R.string.connected);
        tvTitleConnect.setTextColor(getResources().getColor(R.color.green));
        setFragment(EnterNameFragmentMexican.class, false);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);
    }

    @Override
    protected void setWebSocketUrl() {
        url = BuildConfig.BACKEND_BASE_CONNECTION_URL + Const.MEXICAN_ENDPOINT_CLIENT;
    }

    @Override
    protected boolean handleEvent(String event, JsonObject json) throws Exception {
        // Let base handlers handle this event, if possible.
        if (super.handleEvent(event, json)) {
            return true;
        }

        switch (event) {
            case Event.KEY_PLAYER_GAME_MODE_VOTE_HANDED_IN: {
                Boolean succeeded = gson.fromJson(json.get(Event.KEY_VALUE), Boolean.class);
                onVoteHandedIn(succeeded);
                return true;
            }

            case Event.KEY_EVERYONE_VOTED: {
                onEveryoneVoted();
                return true;
            }

            case Event.KEY_YOUR_TURN: {
                onYourTurn();
                return true;
            }

            default:
                throw new Exception("Could not determine a correct event type for client message.");
        }
    }

    private void onYourTurn() {
        TurnFragment turnFragment = (TurnFragment) getSupportFragmentManager().findFragmentByTag(TurnFragment.class.getSimpleName());
        if (turnFragment != null && turnFragment.isVisible()) {
            turnFragment.onYourTurn();
            yourTurn = true;
        }
    }

    private void onEndTurn() {
        TurnFragment turnFragment = (TurnFragment) getSupportFragmentManager().findFragmentByTag(TurnFragment.class.getSimpleName());
        if (turnFragment != null && turnFragment.isVisible()) {
            turnFragment.onNotYourTurn();
            yourTurn = false;
        }
    }

    private void onVoteHandedIn(Boolean succeeded) {
        if (succeeded) {
            Toast.makeText(this, "Vote was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Vote was unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }

    public void onVoteNormal() {
        PlayerGameModeVote vote = new PlayerGameModeVote(gameId, player, MexicanGame.GameMode.NORMAL.ordinal());
        sendEvent(Event.KEY_PLAYER_GAME_MODE_VOTE, vote);
    }

    public void onVoteHardcore() {
        PlayerGameModeVote vote = new PlayerGameModeVote(gameId, player, MexicanGame.GameMode.HARDCORE.ordinal());
        sendEvent(Event.KEY_PLAYER_GAME_MODE_VOTE, vote);
    }

    public void onEveryoneVoted() {
        Toast.makeText(this, "Everyone voted", Toast.LENGTH_SHORT).show();
        setFragment(TurnFragment.class, false);
    }

    @Override
    public void onDisconnected() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        tvTitleConnect.setText(R.string.disconnected);
                        tvTitleConnect.setTextColor(getResources().getColor(R.color.red));
                    }
                }
        );
    }

    @Override
    protected void onReadySuccessful() {
        Toast.makeText(this, "Ready now", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNotReadySuccessful() {
        Toast.makeText(this, "Not ready now", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onGameStarted() {
        setFragment(GameModeVoteFragment.class, false);
    }

    @Override
    protected void onGameEnded() {

    }

    @Override
    protected void onPlayerJoinedSuccessfully() {
        setFragment(ReadyFragmentMexican.class, false);
    }

    @Override
    protected void onPlayAgainSuccessful() {

    }

    @Override
    public void hearShake() {
        if (yourTurn) {
            Toast.makeText(this, "Shakers gonna shake!", Toast.LENGTH_SHORT).show();
            NewPlayerThrow newPlayerThrow = new NewPlayerThrow(gameId, player);
            sendEvent(Event.KEY_NEW_THROW, newPlayerThrow);
        }
    }
}

