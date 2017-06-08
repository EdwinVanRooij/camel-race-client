package io.github.edwinvanrooij.camelraceapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;

import static io.github.edwinvanrooij.camelraceapp.Constants.KEY_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.etGameId)
    EditText etGameId;

    private String lobby_url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @OnClick(R.id.btnJoin)
    public void onButtonJoinClick() {
        System.out.println("Clicked button join");
        lobby_url = etGameId.getText().toString();
        startSocketActivity();
    }

    private void startSocketActivity() {
        Intent intent = new Intent(getActivity(), SocketActivity.class);

        intent.putExtra(KEY_URL, Parcels.wrap(lobby_url));

        startActivity(intent);
    }
}
