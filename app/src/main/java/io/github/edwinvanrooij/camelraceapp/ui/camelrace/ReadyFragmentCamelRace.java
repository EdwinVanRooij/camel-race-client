package io.github.edwinvanrooij.camelraceapp.ui.camelrace;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadyFragmentCamelRace extends CamelRaceSocketFragment {

    @BindView(R.id.btnNotReady)
    Button btnNotReady;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ready, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btnNotReady)
    public void onBtnNotReadyClick() {
        activity.onNotReady();
    }
}
