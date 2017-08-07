package io.github.edwinvanrooij.camelraceapp.ui.camelrace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.parceler.Parcels;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.edwinvanrooij.camelraceapp.BuildConfig;
import io.github.edwinvanrooij.camelraceapp.Config;
import io.github.edwinvanrooij.camelraceapp.Const;
import io.github.edwinvanrooij.camelraceapp.R;

import io.github.edwinvanrooij.camelraceapp.Util;
import io.github.edwinvanrooij.camelraceapp.ui.BaseGameActivity;
import io.github.edwinvanrooij.camelraceapp.ui.BaseSocketActivity;
import io.github.edwinvanrooij.camelraceshared.domain.PersonalResultItem;
import io.github.edwinvanrooij.camelraceshared.domain.PlayAgainRequest;
import io.github.edwinvanrooij.camelraceshared.domain.Player;
import io.github.edwinvanrooij.camelraceshared.domain.PlayerAliveCheck;
import io.github.edwinvanrooij.camelraceshared.domain.PlayerJoinRequest;
import io.github.edwinvanrooij.camelraceshared.domain.PlayerNotReady;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.Bid;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.PlayerNewBid;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static io.github.edwinvanrooij.camelraceapp.Const.KEY_GAME_ID;

public class CamelRaceSocketActivity extends BaseGameActivity {

    @BindView(R.id.tvTitleConnect)
    TextView tvTitleConnect;

    private Bid currentBid;
    private Bid newBid;
    private PersonalResultItem resultItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        ButterKnife.bind(this);

        initVariables();
        connectWebSocket(BuildConfig.BACKEND_BASE_CONNECTION_URL + Const.CAMEL_RACE_ENDPOINT_CLIENT);
        setFragment(EnterNameFragmentCamelRace.class, false);
    }

    public void onSubmitBid(Bid bid) {
        newBid = bid;
        PlayerNewBid playerNewBid = new PlayerNewBid(gameId, player, bid);
        sendEvent(Event.KEY_PLAYER_NEW_BID, playerNewBid);
    }

    public void onReady(Bid bid) {
        newBid = bid;
        PlayerNewBid playerNewBid = new PlayerNewBid(gameId, player, bid);
        sendEvent(Event.KEY_PLAYER_READY, playerNewBid);
    }

    @Override
    public void onConnected() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        tvTitleConnect.setText(R.string.connected);
                        tvTitleConnect.setTextColor(getResources().getColor(R.color.green));
                    }
                }
        );
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
    protected boolean handleEvent(String event, JsonObject json) throws Exception {
        // Let base handlers handle this event, if possible.
        if (super.handleEvent(event, json)) {
            return true;
        }

        switch (event) {
            case Event.KEY_PLAYER_BID_HANDED_IN: {
                Boolean succeeded = gson.fromJson(json.get(Event.KEY_VALUE), Boolean.class);
                onHandedBid(succeeded);
                return true;
            }
            case Event.KEY_PLAYER_READY_SUCCESS: {
                Boolean succeeded = gson.fromJson(json.get(Event.KEY_VALUE), Boolean.class);
                onHandedBid(succeeded);
                onReadySuccess(succeeded);
                return true;
            }
            case Event.KEY_GAME_OVER_PERSONAL_RESULTS: {
                resultItem = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PersonalResultItem.class);
                onGameEnded();
                return true;
            }

            default:
                throw new Exception("Could not determine a correct event type for client message.");
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
    private void onReadySuccess(boolean successful) {
        if (successful) {
            setFragment(ReadyFragmentCamelRace.class, false);
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

    public Bid getBid() {
        return currentBid;
    }

    public PersonalResultItem getResultItem() {
        return resultItem;
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

    private void onGameEnded() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setFragment(ResultFragmentCamelRace.class, false);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.game_ended), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

