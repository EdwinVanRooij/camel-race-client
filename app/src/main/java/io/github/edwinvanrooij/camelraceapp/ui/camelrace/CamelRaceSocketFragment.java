package io.github.edwinvanrooij.camelraceapp.ui.camelrace;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import io.github.edwinvanrooij.camelraceapp.ui.SocketFragment;

/**
 * Created by eddy on 6/8/17.
 */

public abstract class CamelRaceSocketFragment extends SocketFragment {

    protected CamelRaceSocketActivity activity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (CamelRaceSocketActivity) getActivity();
    }
}
