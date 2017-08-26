package io.github.edwinvanrooij.camelraceapp.ui.mexican;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceapp.ui.camelrace.CamelRaceSocketFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayAgainPendingMexican extends MexicanSocketFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_again_pending, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
