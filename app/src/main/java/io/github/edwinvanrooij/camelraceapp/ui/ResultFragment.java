package io.github.edwinvanrooij.camelraceapp.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceshared.domain.Bid;
import io.github.edwinvanrooij.camelraceshared.domain.PersonalResultItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends SocketFragment {

//    @BindView(R.id.btnPlayAgain)
//    Button btnPlayAgain;
@BindView(R.id.tvTitle)
TextView tvTitle;
    @BindView(R.id.tvBid)
    TextView tvBid;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PersonalResultItem item = activity.getResultItem();
        if (item.isWon()) {
            tvTitle.setText(getResources().getString(R.string.result_item_won));
            tvTitle.setTextColor(getResources().getColor(R.color.green));
            tvBid.setVisibility(View.VISIBLE);
            tvBid.setText(String.valueOf(item.getBid().getValue()));
        } else {
            tvTitle.setText(getResources().getString(R.string.result_item_lost));
            tvTitle.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @OnClick(R.id.btnPlayAgain)
    public void onPlayAgainClicked() {
        activity.onPlayAgain();
    }
}
