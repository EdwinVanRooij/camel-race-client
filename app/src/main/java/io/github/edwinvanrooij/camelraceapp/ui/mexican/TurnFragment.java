package io.github.edwinvanrooij.camelraceapp.ui.mexican;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TurnFragment extends MexicanSocketFragment {

    @BindView(R.id.tvTurnDescription)
    TextView tvTurnDescription;
    @BindView(R.id.tvTurnHelp)
    TextView tvTurnHelp;
    @BindView(R.id.ivTurnIcon)
    ImageView ivTurnIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_turn, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onYourTurn() {
        ivTurnIcon.setImageDrawable(getResources().getDrawable(R.drawable.ready));
        tvTurnDescription.setText(getResources().getString(R.string.turn_description_my_turn));
        tvTurnDescription.setTextColor(getResources().getColor(R.color.green));
        tvTurnHelp.setText(getResources().getString(R.string.turn_description_my_turn_help));
    }

    public void onNotYourTurn() {
        ivTurnIcon.setImageDrawable(getResources().getDrawable(R.drawable.waiting));
        tvTurnDescription.setText(getResources().getString(R.string.turn_description_not_my_turn));
        tvTurnDescription.setTextColor(Color.BLACK);
        tvTurnHelp.setText(getResources().getString(R.string.turn_description_not_my_turn_help));
    }
}

