package io.github.edwinvanrooij.camelraceapp.ui.mexican;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import io.github.edwinvanrooij.camelraceapp.ui.BaseSocketFragment;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.CamelRaceSocketActivity;

/**
 * Created by eddy on 6/8/17.
 */

public abstract class MexicanSocketFragment extends BaseSocketFragment {

    protected MexicanSocketActivity activity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (MexicanSocketActivity) getActivity();
    }
}
