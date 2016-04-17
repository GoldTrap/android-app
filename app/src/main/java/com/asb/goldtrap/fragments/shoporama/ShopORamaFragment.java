package com.asb.goldtrap.fragments.shoporama;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.BuyablesRecyclerAdapter;
import com.asb.goldtrap.models.buyables.BuyablesModel;
import com.asb.goldtrap.models.buyables.impl.BuyablesModelImpl;
import com.asb.goldtrap.models.eo.Buyable;

/**
 * ShopORamaFragment.
 */
public class ShopORamaFragment extends Fragment {
    public static final String TAG = ShopORamaFragment.class.getSimpleName();
    private BuyablesModel buyablesModel;
    private RecyclerView.Adapter adapter;
    private OnFragmentInteractionListener mListener;

    public ShopORamaFragment() {
        // Required empty public constructor
    }

    public static ShopORamaFragment newInstance() {
        return new ShopORamaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buyablesModel = new BuyablesModelImpl(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_orama, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.buyables);
        recyclerView.setLayoutManager(linearLayoutManager);

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
        mListener.onBuyableClicked(buyablesModel.getBuyable(position));
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
        void onBuyableClicked(Buyable buyable);
    }
}
