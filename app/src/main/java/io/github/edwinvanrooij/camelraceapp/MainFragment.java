package io.github.edwinvanrooij.camelraceapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static io.github.edwinvanrooij.camelraceapp.Constants.KEY_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.etManualURL)
    EditText etManualURL;

    private String lobby_url;

    private Unbinder unbinder;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.btnJoin)
    public void onButtonJoinClick() {
        System.out.println("Clicked button join");
        lobby_url = etManualURL.getText().toString();
        startEnterNameActivity();
    }

    /**
     * Start a new activity to allow the user to enter their name
     */
    private void startEnterNameActivity() {
        Intent intent = new Intent(getActivity(), SocketActivity.class);

        intent.putExtra(KEY_URL, Parcels.wrap(lobby_url));

        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
