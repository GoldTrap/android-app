package com.asb.goldtrap.fragments.launch;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.asb.goldtrap.GoldTrapApplication;
import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.MenuRecyclerAdapter;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.menu.impl.HomePageMenu;
import com.asb.goldtrap.models.scores.ScoreModel;
import com.asb.goldtrap.models.scores.impl.ScoreModelImpl;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.spansize.MenuSpanSizeLookup;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Home Fragment
 */
public class HomeFragment extends Fragment implements GameConductor.GameStateObserver {

    public static final String TAG = HomeFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ImageView morePoints;
    private TextView points;
    private List<HomePageMenu> homePageMenus;
    private Tracker tracker;
    private ScoreModel scoreModel;

    /**
     * Get the new instance
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        tracker = GoldTrapApplication.getInstance().getDefaultTracker();
        Gson gson = new Gson();
        InputStream inputStream = getResources().openRawResource(R.raw.home_page_menu);
        homePageMenus = gson.fromJson(new JsonReader(new InputStreamReader(inputStream)),
                new TypeToken<List<HomePageMenu>>() {
                }.getType());
        scoreModel = new ScoreModelImpl(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + TAG);
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        setPointsWithAnimation();
    }

    private void setPointsWithAnimation() {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0L, scoreModel.getCurrentScore().getValue());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                points.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.setEvaluator(new TypeEvaluator<Long>() {
            public Long evaluate(float fraction, Long startValue, Long endValue) {
                return Math.round(startValue + (endValue - startValue) * (double) fraction);
            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 6);
        gridLayoutManager.setSpanSizeLookup(new MenuSpanSizeLookup(homePageMenus));
        recyclerView = (RecyclerView) view.findViewById(R.id.menus);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(new MenuRecyclerAdapter(getContext(), homePageMenus,
                new MenuRecyclerAdapter.ViewHolder.ViewHolderClicks() {
                    @Override
                    public void onClick(View v, int position) {
                        handleMenuClick(v, position);
                    }
                }));
        morePoints = (ImageButton) view.findViewById(R.id.more_points);
        morePoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Home Page Menu")
                        .setAction("Points")
                        .setLabel("More Points")
                        .build());
                mListener.showPoints();
            }
        });
        points = (TextView) view.findViewById(R.id.points);
        return view;
    }

    private void handleMenuClick(View v, int position) {
        HomePageMenu menu = homePageMenus.get(position);
        switch (menu.getType()) {
            case PLAY_GAME:
                mListener.play(v, menu.getName());
                break;
            case QUICK_PLAY_GAME:
                mListener.quickPlay();
                break;
            case MULTI_PLAYER_GAME:
                mListener.multiPlayerGame(v, menu.getName());
                break;
            case STORE:
                mListener.onStore(v, menu.getName());
                break;
            case LEADERBOARDS:
                mListener.leaderboards();
                break;
            case ACHIEVEMENTS:
                mListener.achievements();
                break;
            case SHARE:
                mListener.share();
                break;
        }
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Home Page Menu")
                .setAction(menu.getType().name())
                .setLabel(menu.getName())
                .setValue(menu.getSpanSize())
                .build());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void stateChanged(GameState state) {

    }

    public interface OnFragmentInteractionListener {
        boolean isConnected();

        void signOut();

        void play(View v, String name);

        void quickPlay();

        void multiPlayerGame(View v, String name);

        void onStore(View v, String name);

        void leaderboards();

        void achievements();

        void share();

        void showPoints();
    }

}
