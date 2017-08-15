package io.github.edwinvanrooij.camelraceapp.ui.mexican;

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
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.BidFragmentCamelRace;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.EnterNameFragmentCamelRace;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.PlayAgainPending;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.RacingFragmentCamelRace;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.ReadyFragmentCamelRace;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.ResultFragmentCamelRace;
import io.github.edwinvanrooij.camelraceshared.domain.PersonalResultItem;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.Bid;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.PlayerNewBid;
import io.github.edwinvanrooij.camelraceshared.events.Event;

public class MexicanSocketActivity extends BaseGameActivity {

    @BindView(R.id.tvTitleConnect)
    TextView tvTitleConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        ButterKnife.bind(this);

        tvTitleConnect.setText(R.string.connected);
        tvTitleConnect.setTextColor(getResources().getColor(R.color.green));
        setFragment(EnterNameFragmentMexican.class, false);
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
//            case Event.KEY_PLAYER_BID_HANDED_IN: {
//                Boolean succeeded = gson.fromJson(json.get(Event.KEY_VALUE), Boolean.class);
//                onHandedBid(succeeded);
//                return true;
//            }
//            case Event.KEY_GAME_OVER_PERSONAL_RESULTS: {
//                resultItem = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PersonalResultItem.class);
//                onGameEnded();
//                return true;
//            }

            default:
                throw new Exception("Could not determine a correct event type for client message.");
        }
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

    }

    @Override
    protected void onNotReadySuccessful() {

    }

    @Override
    protected void onGameStarted() {

    }

    @Override
    protected void onGameEnded() {

    }

    @Override
    protected void onPlayerJoinedSuccessfully() {
        Toast.makeText(this, "Player joined successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPlayAgainSuccessful() {

    }
}

