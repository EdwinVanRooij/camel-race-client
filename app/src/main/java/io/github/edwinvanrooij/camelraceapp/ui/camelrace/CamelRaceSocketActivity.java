package io.github.edwinvanrooij.camelraceapp.ui.camelrace;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.edwinvanrooij.camelraceapp.BuildConfig;
import io.github.edwinvanrooij.camelraceapp.Const;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceapp.ui.BaseGameActivity;
import io.github.edwinvanrooij.camelraceshared.domain.PersonalResultItem;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.Bid;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.PlayerNewBid;
import io.github.edwinvanrooij.camelraceshared.events.Event;

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

        tvTitleConnect.setText(R.string.connected);
        tvTitleConnect.setTextColor(getResources().getColor(R.color.green));
        setFragment(EnterNameFragmentCamelRace.class, false);
    }

    @Override
    protected void setWebSocketUrl() {
        url = BuildConfig.BACKEND_BASE_CONNECTION_URL + Const.CAMEL_RACE_ENDPOINT_CLIENT;
    }

    public void onSubmitBid(Bid bid) {
        newBid = bid;
        PlayerNewBid playerNewBid = new PlayerNewBid(gameId, player, bid);
        sendEvent(Event.KEY_PLAYER_NEW_BID, playerNewBid);
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
            case Event.KEY_GAME_OVER_PERSONAL_RESULTS: {
                resultItem = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PersonalResultItem.class);
                onGameEnded();
                return true;
            }

            default:
                throw new Exception("Could not determine a correct event type for client message.");
        }
    }

    @Override
    protected void onPlayAgainSuccessful() {
        setFragment(PlayAgainPending.class, false);
    }

    @Override
    protected void onReadySuccessful() {
        setFragment(ReadyFragmentCamelRace.class, false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.successful_ready), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onNotReadySuccessful() {
        setFragment(BidFragmentCamelRace.class, false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.successful_not_ready), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onGameStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setFragment(RacingFragmentCamelRace.class, false);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.game_started), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onGameEnded() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setFragment(ResultFragmentCamelRace.class, false);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.game_ended), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPlayerJoinedSuccessfully() {
        setFragment(BidFragmentCamelRace.class, false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.successful_join), Toast.LENGTH_SHORT).show();
            }
        });
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

    public Bid getBid() {
        return currentBid;
    }

    public PersonalResultItem getResultItem() {
        return resultItem;
    }
}

