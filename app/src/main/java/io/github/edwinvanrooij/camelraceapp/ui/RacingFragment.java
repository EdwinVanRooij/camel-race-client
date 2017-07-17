package io.github.edwinvanrooij.camelraceapp.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceshared.domain.Bid;


/**
 * A simple {@link Fragment} subclass.
 */
public class RacingFragment extends SocketFragment {

//    @BindView(R.id.btnPlayAgain)
//    Button btnPlayAgain;
    @BindView(R.id.tvBid)
    TextView tvBid;
    @BindView(R.id.ivCamel)
    ImageView ivCamel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_racing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bid bid = activity.getBid();
        if (bid != null) {
            tvBid.setText(String.valueOf(bid.getValue()));
            switch (bid.getType()) {
                case CLUBS:
                    ivCamel.setImageDrawable(getResources().getDrawable(R.drawable.ace_of_clubs));
                    break;
                case SPADES:
                    ivCamel.setImageDrawable(getResources().getDrawable(R.drawable.ace_of_spades));
                    break;
                case DIAMONDS:
                    ivCamel.setImageDrawable(getResources().getDrawable(R.drawable.ace_of_diamonds));
                    break;
                case HEARTS:
                    ivCamel.setImageDrawable(getResources().getDrawable(R.drawable.ace_of_hearts));
                    break;
            }
        } else {
            tvBid.setText(getResources().getString(R.string.no_bid_set));
        }
    }

//    @OnClick(R.id.btnPlayAgain)
//    public void onPlayAgainClicked() {
//        activity.onPlayAgain();
//    }
}
