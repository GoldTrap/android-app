package com.asb.goldtrap.fragments.points;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asb.goldtrap.GoldTrapApplication;
import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.PointsRecyclerAdapter;
import com.asb.goldtrap.models.goodie.GoodieModel;
import com.asb.goldtrap.models.goodie.impl.CursorGoodieModel;
import com.asb.goldtrap.models.scores.ScoreModel;
import com.asb.goldtrap.models.scores.impl.ScoreModelImpl;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


/**
 * A placeholder fragment containing a simple view.
 */
public class PointsFragment extends Fragment implements GoodieModel.Listener {

    public static final String TAG = PointsFragment.class.getSimpleName();
    private ScoreModel scoreModel;
    private GoodieModel goodieModel;
    private PointsRecyclerAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private Tracker tracker;

    public PointsFragment() {
        // Required empty public constructor
    }

    public static PointsFragment newInstance() {
        return new PointsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRetainInstance(true);
        tracker = GoldTrapApplication.getInstance().getDefaultTracker();
        scoreModel = new ScoreModelImpl(getContext());
        goodieModel =
                new CursorGoodieModel(getContext(), getActivity().getSupportLoaderManager(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_points, container, false);
        TextView pointsValue = (TextView) view.findViewById(R.id.points_value);
        pointsValue.setText(
                getString(R.string.points_value_name, scoreModel.getCurrentScore().getValue()));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.goodies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PointsRecyclerAdapter(getContext(), goodieModel);
        recyclerView.setAdapter(adapter);
        NativeExpressAdView mAdView = (NativeExpressAdView) view.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("EAA51803F0D6A92C418E5D37FE508ACB")
                .build();
        mAdView.loadAd(adRequest);
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

    public interface OnFragmentInteractionListener {

    }

}
