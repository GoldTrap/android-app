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

import com.asb.goldtrap.GoldTrapApplication;
import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.BuyablesRecyclerAdapter;
import com.asb.goldtrap.models.buyables.BuyablesModel;
import com.asb.goldtrap.models.buyables.impl.BuyablesModelImpl;
import com.asb.goldtrap.models.eo.Buyable;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * ShopORamaFragment.
 */
public class ShopORamaFragment extends Fragment {
    public static final String TAG = ShopORamaFragment.class.getSimpleName();
    private BuyablesModel buyablesModel;
    private RecyclerView.Adapter adapter;
    private OnFragmentInteractionListener mListener;
    private Tracker tracker;

    public ShopORamaFragment() {
        // Required empty public constructor
    }

    public static ShopORamaFragment newInstance() {
        return new ShopORamaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        tracker = GoldTrapApplication.getInstance().getDefaultTracker();
        buyablesModel = new BuyablesModelImpl(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_orama, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.buyables);
        recyclerView.setLayoutManager(linearLayoutManager);
        NativeExpressAdView mAdView = (NativeExpressAdView) view.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("EAA51803F0D6A92C418E5D37FE508ACB")
                .build();
        mAdView.loadAd(adRequest);
        adapter = new BuyablesRecyclerAdapter(getContext(), buyablesModel,
                new BuyablesRecyclerAdapter.ViewHolder.ViewHolderClicks() {
                    @Override
                    public void onClick(int position) {
                        handleBuyableClick(position);
                    }
                });
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void handleBuyableClick(int position) {
        Buyable buyable = buyablesModel.getBuyable(position);
        mListener.onBuyableClicked(buyable);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Buyable Click")
                .setAction(buyable.getType().name())
                .setLabel(buyable.getName())
                .setValue(position)
                .build());
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
        Log.i(TAG, "Setting screen name: " + TAG);
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public interface OnFragmentInteractionListener {
        void onBuyableClicked(Buyable buyable);
    }
}
