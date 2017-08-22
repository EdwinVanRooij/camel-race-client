package io.github.edwinvanrooij.camelraceapp.ui.mexican;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameModeVoteFragment extends MexicanSocketFragment {

    @BindView(R.id.tvGameModeTitle)
    TextView tvGameModeTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_mode_vote, container, false);
    }

    @OnClick(R.id.btnNormal)
    public void onNormalClick() {
        activity.onVoteNormal();
        tvGameModeTitle.setText(getResources().getString(R.string.title_game_mode_normal));
        tvGameModeTitle.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnHardcore)
    public void onHardcoreClick() {
        activity.onVoteHardcore();
        tvGameModeTitle.setText(getResources().getString(R.string.title_game_mode_hardcore));
        tvGameModeTitle.setVisibility(View.VISIBLE);
    }
}

