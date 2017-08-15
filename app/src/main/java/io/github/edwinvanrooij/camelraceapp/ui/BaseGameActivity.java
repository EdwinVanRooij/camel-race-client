package io.github.edwinvanrooij.camelraceapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.parceler.Parcels;

import java.util.Timer;
import java.util.TimerTask;

import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.BidFragmentCamelRace;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.RacingFragmentCamelRace;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.ReadyFragmentCamelRace;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.ResultFragmentCamelRace;
import io.github.edwinvanrooij.camelraceshared.domain.PlayAgainRequest;
import io.github.edwinvanrooij.camelraceshared.domain.Player;
import io.github.edwinvanrooij.camelraceshared.domain.PlayerAliveCheck;
import io.github.edwinvanrooij.camelraceshared.domain.PlayerJoinRequest;
import io.github.edwinvanrooij.camelraceshared.domain.PlayerNotReady;
import io.github.edwinvanrooij.camelraceshared.domain.PlayerReady;
import io.github.edwinvanrooij.camelraceshared.events.Event;

import static io.github.edwinvanrooij.camelraceapp.Const.KEY_GAME_ID;

/**
 * Author: eddy
 * Date: 21-1-17.
 */

public abstract class BaseGameActivity extends BaseSocketActivity {

    protected String gameId;
    protected Timer timer;
    protected Player player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameId = Parcels.unwrap(getIntent().getParcelableExtra(KEY_GAME_ID));
    }

    @Override
    protected boolean handleEvent(String event, JsonObject json) throws Exception {
        switch (event) {
            case Event.KEY_PLAYER_JOINED: {
                this.player = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), Player.class);
                startTimer();
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
                onPlayAgainSuccessful();
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

    public void onSubmitUsername(String username) {
        Player player = new Player(username);
        PlayerJoinRequest playerJoinRequest = new PlayerJoinRequest(gameId, player);
        sendEvent(Event.KEY_PLAYER_JOIN, playerJoinRequest);
    }

    public void onReady() {
        PlayerReady playerReady = new PlayerReady(gameId, player);
        sendEvent(Event.KEY_PLAYER_READY, playerReady);
    }

    public void onPlayAgain() {
        PlayAgainRequest playAgainRequest = new PlayAgainRequest(gameId, player);
        sendEvent(Event.KEY_PLAY_AGAIN, playAgainRequest);
    }

    public void onNotReadyClick() {
        sendEvent(Event.KEY_PLAYER_NOT_READY, new PlayerNotReady(gameId, player));
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

    protected abstract void onPlayAgainSuccessful();

    private void onReadySuccess(boolean successful) {
        if (successful) {
            onReadySuccessful();
        } else {
            onReadyUnsuccessful();
        }
    }
    protected abstract void onReadySuccessful();
    protected void onReadyUnsuccessful() {
        Toast.makeText(this, "Ready signal incorrectly returned from server", Toast.LENGTH_SHORT).show();
    }

    private void onNotReadySuccess(boolean successful) {
        if (successful) {
            onNotReadySuccessful();
        } else {
            onNotReadyUnsuccessful();
        }
    }
    protected abstract void onNotReadySuccessful();
    protected void onNotReadyUnsuccessful() {
        Toast.makeText(this, "Not ready signal incorrectly returned from server", Toast.LENGTH_SHORT).show();
    }

    protected abstract void onGameStarted();

    @Override
    protected void onStop() {
        super.onStop();

        if (timer != null) {
            System.out.println("Timer is now canceled");
            timer.cancel();
        }
    }

    protected abstract void onGameEnded();
    protected abstract void onPlayerJoinedSuccessfully();

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        int interval = 2; // in seconds
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendEvent(Event.KEY_PLAYER_ALIVE_CHECK, new PlayerAliveCheck(gameId, player));
            }
        };
        timer.schedule(task, 0, interval * 1000);
    }
}
