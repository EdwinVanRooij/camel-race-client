package io.github.edwinvanrooij.camelraceapp.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterNameFragment extends SocketFragment {

    @BindView(R.id.etUsername)
    EditText etUsername;

    @BindView(R.id.tvTitleConnect)
    TextView tvTitleConnect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter_name, container, false);
    }

    @OnClick(R.id.btnConfirm)
    public void onButtonConfirmClick() {
        String username = etUsername.getText().toString();

        Toast.makeText(getContext(), String.format("Username is: %s", username), Toast.LENGTH_SHORT).show();
        activity.onSubmitUsername(username);
    }

    public void onConnected() {
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        tvTitleConnect.setText(R.string.connected);
                        tvTitleConnect.setTextColor(Color.GREEN);
                    }
                }
        );
    }

    public void onDisconnected() {
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        tvTitleConnect.setText(R.string.disconnected);
                        tvTitleConnect.setTextColor(Color.RED);
                    }
                }
        );
    }

}
