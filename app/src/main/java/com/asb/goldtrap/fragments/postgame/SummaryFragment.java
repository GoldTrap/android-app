package com.asb.goldtrap.fragments.postgame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.asb.goldtrap.GoldTrapApplication;
import com.asb.goldtrap.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.games.stats.PlayerStats;

/**
 * Summary Fragment
 * Created on 29/11/2015
 */
public class SummaryFragment extends Fragment implements View.OnClickListener {
    private static final String IMAGE_URI = "imageUri";
    public static final String TAG = SummaryFragment.class.getSimpleName();

    private Button next;
    private Button share;
    private Button invite;
    private Button replay;
    private ImageView image;
    private Uri uri;

    private OnFragmentInteractionListener mListener;
    private Tracker tracker;
    private NativeExpressAdView mAdView;

    public SummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Factory to create new instance
     *
     * @param uri Image URI.
     * @return A new instance of fragment SummaryFragment.
     */
    public static SummaryFragment newInstance(Uri uri) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putParcelable(IMAGE_URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker = GoldTrapApplication.getInstance().getDefaultTracker();
        if (getArguments() != null) {
            uri = getArguments().getParcelable(IMAGE_URI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        next = (Button) view.findViewById(R.id.next);
        replay = (Button) view.findViewById(R.id.replay);
        share = (Button) view.findViewById(R.id.share);
        invite = (Button) view.findViewById(R.id.invite);
        next.setOnClickListener(this);
        replay.setOnClickListener(this);
        share.setOnClickListener(this);
        invite.setOnClickListener(this);
        image = (ImageView) view.findViewById(R.id.image);
        image.setImageURI(uri);
        mAdView = (NativeExpressAdView) view.findViewById(R.id.ad_view);
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
        Log.i(TAG, "Setting screen name: " + TAG);
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        mListener.hideAppbar();
        PlayerStats stats = mListener.getPlayerStats();
        if (null != stats &&
                (stats.getChurnProbability() == 1 || stats.getSpendProbability() > 0.5)) {
            mAdView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        String label = null;
        switch (v.getId()) {
            case R.id.share:
                label = "Share";
                mListener.shareGame();
                break;
            case R.id.invite:
                label = "Invite";
                mListener.invite();
                break;
            case R.id.replay:
                label = "Replay";
                mListener.replayGame();
                break;
            case R.id.next:
                label = "Next";
                mListener.next();
                break;
        }
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Button")
                .setAction("Click")
                .setLabel(label)
                .build());
    }

    public interface OnFragmentInteractionListener {
        void replayGame();

        void shareGame();

        void invite();

        void next();

        void hideAppbar();

        PlayerStats getPlayerStats();
    }
}
