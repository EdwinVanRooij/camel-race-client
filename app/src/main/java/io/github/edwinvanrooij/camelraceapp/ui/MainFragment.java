package io.github.edwinvanrooij.camelraceapp.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceshared.events.Event;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseSocketFragment {

    @BindView(R.id.etGameId)
    EditText etGameId;

    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @OnClick(R.id.btnJoin)
    public void onButtonJoinClick() {
        String gameId = etGameId.getText().toString();

        activity.sendEvent(Event.KEY_WHICH_GAME_TYPE, gameId);
        activity.setGameId(gameId);

        Toast.makeText(activity, getResources().getString(R.string.waiting), Toast.LENGTH_SHORT).show();
    }

}
