package io.github.edwinvanrooij.camelraceapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by eddy on 6/8/17.
 */

public abstract class SocketFragment extends BaseFragment {

    protected SocketActivity activity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (SocketActivity) getActivity();
    }
}
