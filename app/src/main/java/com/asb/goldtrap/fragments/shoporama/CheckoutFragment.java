package com.asb.goldtrap.fragments.shoporama;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asb.goldtrap.GoldTrapApplication;
import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.CheckoutRecyclerAdapter;
import com.asb.goldtrap.models.boosters.BoosterModel;
import com.asb.goldtrap.models.boosters.impl.BoosterModelImpl;
import com.asb.goldtrap.models.buyables.BuyableType;
import com.asb.goldtrap.models.eo.BoosterExchangeRate;
import com.asb.goldtrap.models.eo.BoosterType;
import com.asb.goldtrap.models.eo.Goodie;
import com.asb.goldtrap.models.eo.Score;
import com.asb.goldtrap.models.goodie.GoodieModel;
import com.asb.goldtrap.models.goodie.impl.CursorGoodieModel;
import com.asb.goldtrap.models.scores.ScoreModel;
import com.asb.goldtrap.models.scores.impl.ScoreModelImpl;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


/**
 * Checkout Fragment.
 */
public class CheckoutFragment extends Fragment implements GoodieModel.Listener,
        CheckoutRecyclerAdapter.ViewHolder.ViewHolderClicks {
    public static final String TAG = CheckoutFragment.class.getSimpleName();
    private static final String BUYABLE = "buyable";
    private BuyableType buyableType;
    private OnFragmentInteractionListener mListener;
    private ScoreModel scoreModel;
    private BoosterModel boosterModel;
    private GoodieModel goodieModel;
    private RecyclerView.Adapter adapter;
    private Tracker tracker;

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
        tracker = GoldTrapApplication.getInstance().getDefaultTracker();
        scoreModel = new ScoreModelImpl(getContext());
        boosterModel = new BoosterModelImpl(getContext());
        goodieModel =
                new CursorGoodieModel(getContext(), getActivity().getSupportLoaderManager(), this);
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
        TextView buyItemDesc = (TextView) view.findViewById(R.id.buy_item_desc);
        buyItemDesc.setText(getString(R.string.buy_item_desc, getString(buyableType.getNameRes())));
        Button buyButton = (Button) view.findViewById(R.id.buy_button);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.buyItem(buyableType);
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Buy")
                        .setLabel(buyableType.name())
                        .build());
            }
        });

        TextView tradeItemDesc = (TextView) view.findViewById(R.id.trade_points_desc);
        tradeItemDesc.setText(
                getString(R.string.trade_points_desc, getBoosterExchangeRate().getPoints()));
        final Button tradeButton = (Button) view.findViewById(R.id.trade_button);
        Score score = scoreModel.getCurrentScore();
        handleScoreEnablement(score, tradeButton);
        tradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Score score =
                        scoreModel.tradeBoosterForScore(BoosterType.valueOf(buyableType.name()),
                                getBoosterExchangeRate());
                handleScoreEnablement(score, tradeButton);
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Trade Score")
                        .setLabel(buyableType.name())
                        .build());
                mListener.onConsumptionComplete();
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.goodies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CheckoutRecyclerAdapter(getContext(), goodieModel, this,
                getBoosterExchangeRate());
        recyclerView.setAdapter(adapter);
        NativeExpressAdView mAdView = (NativeExpressAdView) view.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("EAA51803F0D6A92C418E5D37FE508ACB")
                .build();
        mAdView.loadAd(adRequest);
        return view;
    }

    private void handleScoreEnablement(Score score, Button tradeButton) {
        if (getBoosterExchangeRate().getPoints() <= score.getValue()) {
            tradeButton.setEnabled(true);
        }
        else {
            tradeButton.setEnabled(false);
        }
    }

    private BoosterExchangeRate getBoosterExchangeRate() {
        return boosterModel.getBoosterExchangeRates()
                .get(BoosterType.valueOf(buyableType.name()));
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

    @Override
    public void onResume() {
        super.onResume();
        goodieModel.loadGoodies();
        Log.i(TAG, "Setting screen name: " + TAG);
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    public void dataChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(int position) {
        Goodie goodie = goodieModel.getGoodie(position);
        goodieModel.tradeWithGoodie(goodie, BoosterType.valueOf(buyableType.name()),
                getBoosterExchangeRate());
        goodieModel.loadGoodies();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Button")
                .setAction("Goodie " + goodie.getType())
                .setLabel(buyableType.name())
                .setValue(position)
                .build());
        mListener.onConsumptionComplete();
    }

    public interface OnFragmentInteractionListener {
        void buyItem(BuyableType buyableType);

        void onConsumptionComplete();
    }
}
