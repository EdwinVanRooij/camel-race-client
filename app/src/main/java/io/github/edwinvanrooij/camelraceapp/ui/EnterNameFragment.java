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
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.edwinvanrooij.camelraceapp.Config;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceapp.ui.SocketActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterNameFragment extends SocketFragment {

    @BindView(R.id.etUsername)
    EditText etUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter_name, container, false);
    }

    @OnClick(R.id.btnConfirm)
    public void onButtonConfirmClick() {
        String username = etUsername.getText().toString();

        Toast.makeText(getContext(), String.format("Username is: %s", username), Toast.LENGTH_SHORT).show();
    }

}
