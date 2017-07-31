package io.github.edwinvanrooij.camelraceapp.ui.mexican;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import io.github.edwinvanrooij.camelraceapp.ui.SocketFragment;

/**
 * Created by eddy on 6/8/17.
 */

public abstract class MexicanSocketFragment extends SocketFragment {

    protected MexicanSocketActivity activity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (MexicanSocketActivity) getActivity();
    }
}
