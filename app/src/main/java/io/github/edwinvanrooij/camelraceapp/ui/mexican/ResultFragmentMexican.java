package io.github.edwinvanrooij.camelraceapp.ui.mexican;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceshared.domain.mexican.PersonalResultItemMexican;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragmentMexican extends MexicanSocketFragment {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvStake)
    TextView tvStake;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PersonalResultItemMexican item = activity.getResultItem();
        if (item.isLoser()) {
            tvTitle.setText(getResources().getString(R.string.result_item_lost));
            tvTitle.setTextColor(getResources().getColor(R.color.red));
        } else {
            tvTitle.setText(getResources().getString(R.string.result_item_someone_else_lost));
            tvTitle.setTextColor(getResources().getColor(R.color.green));
        }
        String stakeString = getResources().getString(R.string.stake, item.getStake());
        tvStake.setText(stakeString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result_mexican, container, false);
    }

    @OnClick(R.id.btnPlayAgain)
    public void onPlayAgainClicked() {
        activity.onPlayAgain();
    }
}
