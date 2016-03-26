package com.asb.goldtrap.fragments.launch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.MenuRecyclerAdapter;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.dao.ScoreDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.ScoreDaoImpl;
import com.asb.goldtrap.models.menu.impl.HomePageMenu;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.spansize.MenuSpanSizeLookup;
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
    private List<HomePageMenu> homePageMenus;
    private ScoreDao scoreDao;
    private TextView points;

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
        Gson gson = new Gson();
        InputStream inputStream = getResources().openRawResource(R.raw.home_page_menu);
        homePageMenus = gson.fromJson(new JsonReader(new InputStreamReader(inputStream)),
                new TypeToken<List<HomePageMenu>>() {
                }.getType());
        scoreDao = new ScoreDaoImpl(DBHelper.getInstance(getContext()).getWritableDatabase());
    }

    @Override
    public void onResume() {
        super.onResume();
        points.setText(getString(R.string.points, scoreDao.getScore(ScoreDao.CURRENT).getValue()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        points = (TextView) view.findViewById(R.id.points);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 6);
        gridLayoutManager.setSpanSizeLookup(new MenuSpanSizeLookup(homePageMenus));
        recyclerView = (RecyclerView) view.findViewById(R.id.menus);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(new MenuRecyclerAdapter(getContext(), homePageMenus,
                new MenuRecyclerAdapter.ViewHolder.ViewHolderClicks() {
                    @Override
                    public void onClick(int position) {
                        handleMenuClick(position);
                    }
                }));
        return view;
    }

    private void handleMenuClick(int position) {
        switch (homePageMenus.get(position).getType()) {
            case PLAY_GAME:
                mListener.play();
                break;
            case QUICK_PLAY_GAME:
                mListener.quickPlay();
                break;
            case MULTI_PLAYER_GAME:
                mListener.multiPlayerGame();
                break;
            case STORE:
                break;
            case RANKINGS:
                break;
            case SCORE:
                break;
            case SHARE:
                break;
        }
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

        void play();

        void quickPlay();

        void multiPlayerGame();
    }

}
