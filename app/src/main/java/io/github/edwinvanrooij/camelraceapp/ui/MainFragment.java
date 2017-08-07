package io.github.edwinvanrooij.camelraceapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.Const;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceshared.events.Event;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseSocketFragment {

    @BindView(R.id.etGameId)
    EditText etGameId;

    private String gameId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @OnClick(R.id.btnJoin)
    public void onButtonJoinClick() {
        gameId = etGameId.getText().toString();
        activity.sendEvent(Event.KEY_WHICH_GAME_TYPE, gameId);
        Toast.makeText(activity, getResources().getString(R.string.waiting), Toast.LENGTH_SHORT).show();
    }

    private void startGame(Class gameClass) {
        Intent intent = new Intent(getActivity(), gameClass);

        intent.putExtra(Const.KEY_GAME_ID, Parcels.wrap(gameId));

        startActivity(intent);
    }
}
