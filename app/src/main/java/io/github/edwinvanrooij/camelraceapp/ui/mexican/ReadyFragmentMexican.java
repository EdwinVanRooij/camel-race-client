package io.github.edwinvanrooij.camelraceapp.ui.mexican;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadyFragmentMexican extends MexicanSocketFragment {

    @BindView(R.id.btnNotReady)
    Button btnNotReady;
    @BindView(R.id.title)
    TextView tvTitle;
    boolean ready = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ready, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toggleReady();
    }

    private void toggleReady() {
        if (ready) {
            ready = false;
            tvTitle.setText(getResources().getString(R.string.title_not_ready));
            tvTitle.setTextColor(getResources().getColor(R.color.red));
            btnNotReady.setText(getResources().getString(R.string.button_ready));
            activity.onNotReady();
        } else {
            ready = true;
            tvTitle.setText(getResources().getString(R.string.title_ready));
            tvTitle.setTextColor(getResources().getColor(R.color.green));
            btnNotReady.setText(getResources().getString(R.string.button_not_ready));
            System.out.println("Sending ready to activity");
            activity.onReady();
        }
    }

    @OnClick(R.id.btnNotReady)
    public void onBtnNotReadyClick() {
        toggleReady();
    }
}
