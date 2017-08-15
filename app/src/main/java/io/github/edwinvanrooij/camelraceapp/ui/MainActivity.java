package io.github.edwinvanrooij.camelraceapp.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.edwinvanrooij.camelraceapp.BuildConfig;
import io.github.edwinvanrooij.camelraceapp.Const;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceshared.events.Event;

public class MainActivity extends BaseSocketActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initVariables();
        connectWebSocket(BuildConfig.BACKEND_BASE_CONNECTION_URL + Const.DEFAULT_ENDPOINT_CLIENT);
        setFragment(MainFragment.class, false);
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
    }

    private void onCamelRaceGameType() {
        System.out.println("On camel gametype");
    }
}
