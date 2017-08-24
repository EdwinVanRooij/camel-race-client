package io.github.edwinvanrooij.camelraceapp.ui.mexican;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.btnThrow)
    Button btnThrow;
    @BindView(R.id.btnStop)
    Button btnStop;

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
        btnThrow.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
    }

    public void onYourTurnWithEndOption() {
        ivTurnIcon.setImageDrawable(getResources().getDrawable(R.drawable.ready));
        tvTurnDescription.setText(getResources().getString(R.string.turn_description_my_turn));
        tvTurnDescription.setTextColor(getResources().getColor(R.color.green));
        tvTurnHelp.setText(getResources().getString(R.string.turn_description_my_turn_help));
        btnThrow.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.VISIBLE);
    }

    public void onNotYourTurn() {
        ivTurnIcon.setImageDrawable(getResources().getDrawable(R.drawable.waiting));
        tvTurnDescription.setText(getResources().getString(R.string.turn_description_not_my_turn));
        tvTurnDescription.setTextColor(Color.BLACK);
        tvTurnHelp.setText(getResources().getString(R.string.turn_description_not_my_turn_help));
        btnThrow.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnThrow)
    public void onThrowClick() {
        activity.throwDices();
        Toast.makeText(activity, "Threw by using button", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnStop)
    public void onStopClick() {
        activity.onEndTurn();
    }
}

