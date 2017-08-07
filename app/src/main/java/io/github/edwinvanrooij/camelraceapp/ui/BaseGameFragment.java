package io.github.edwinvanrooij.camelraceapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: eddy
 * Date: 21-1-17.
 */

public abstract class BaseGameFragment extends BaseSocketFragment {

    private Unbinder unbinder;
    protected BaseGameActivity activity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        activity = (BaseGameActivity) getActivity();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
