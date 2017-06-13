package io.github.edwinvanrooij.camelraceapp.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Pattern;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.camelraceapp.R;
import io.github.edwinvanrooij.camelraceshared.domain.Bid;
import io.github.edwinvanrooij.camelraceshared.domain.CardType;

import static io.github.edwinvanrooij.camelraceapp.R.id.spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class BidFragment extends SocketFragment {

    @BindView(R.id.etBidValue)
    EditText etBidValue;
    @BindView(R.id.spinner)
    Spinner spinner;

    @BindString(R.string.diamonds)
    String diamonds;
    @BindString(R.string.spades)
    String spades;
    @BindString(R.string.hearts)
    String hearts;
    @BindString(R.string.clubs)
    String clubs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bid   , container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSpinner();
    }

    public void initSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the mAdapter to the spinner
        spinner.setAdapter(adapter);
    }

    @OnClick(R.id.btnConfirmBid)
    public void onButtonConfirmClick() {
        String bidValue = etBidValue.getText().toString();
        boolean validBid = Pattern.matches("^\\d+$", bidValue);
        if (!validBid) {
            Toast.makeText(activity, getResources().getString(R.string.invalid_bid_value), Toast.LENGTH_SHORT).show();
        }

        CardType type = null;
        String spinnerSelection = spinner.getSelectedItem().toString();
        if (Objects.equals(spinnerSelection, clubs)) {
            type = CardType.CLUBS;
        } else if (Objects.equals(spinnerSelection, hearts)) {
            type = CardType.HEARTS;
        } else if (Objects.equals(spinnerSelection, spades)) {
            type = CardType.SPADES;
        } else if (Objects.equals(spinnerSelection, diamonds)) {
            type = CardType.DIAMONDS;
        } else {
            Toast.makeText(getContext(),
                    "Could not determine spinner selection, it equals none of the accepted types"
                    , Toast.LENGTH_SHORT).show();
        }

        Bid bid = new Bid(type, Integer.valueOf(bidValue));

        activity.onSubmitBid(bid);
    }
}
