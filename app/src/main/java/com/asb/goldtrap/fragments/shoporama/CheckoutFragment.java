package com.asb.goldtrap.fragments.shoporama;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.buyables.BuyableType;
import com.asb.goldtrap.models.states.enums.GoodiesState;

/**
 * Checkout Fragment.
 */
public class CheckoutFragment extends Fragment {
    public static final String TAG = CheckoutFragment.class.getSimpleName();
    private static final String BUYABLE = "buyable";
    private BuyableType buyableType;

    private OnFragmentInteractionListener mListener;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    public static CheckoutFragment newInstance(String buyable) {
        CheckoutFragment fragment = new CheckoutFragment();
        Bundle args = new Bundle();
        args.putString(BUYABLE, buyable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRetainInstance(true);
        if (getArguments() != null) {
            buyableType = BuyableType.valueOf(getArguments().getString(BUYABLE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        TextView buyItem = (TextView) view.findViewById(R.id.buy_item);
        buyItem.setText(getString(R.string.buy_item, getString(buyableType.getNameRes())));
        Button buyButton = (Button) view.findViewById(R.id.buy_button);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.buyItem(buyableType);
            }
        });
        Button tradeButton = (Button) view.findViewById(R.id.trade_button);
        tradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.tradePoints(buyableType);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void buyItem(BuyableType buyableType);

        void tradePoints(BuyableType buyableType);

        void exchangeGoodie(BuyableType buyableType, GoodiesState goodiesState);
    }
}
