package io.github.edwinvanrooij.camelraceapp.ui.camelrace;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterNameFragmentCamelRace extends CamelRaceSocketFragment {

    @BindView(R.id.etUsername)
    EditText etUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter_name, container, false);
    }

    @OnClick(R.id.btnConfirmUsername)
    public void onButtonConfirmClick() {
        String username = etUsername.getText().toString();

        activity.onSubmitUsername(username);
    }
}
