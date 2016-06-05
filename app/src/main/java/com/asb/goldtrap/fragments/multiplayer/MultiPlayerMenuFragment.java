package com.asb.goldtrap.fragments.multiplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.GoldTrapApplication;
import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.MenuRecyclerAdapter;
import com.asb.goldtrap.models.menu.impl.MultiPlayerPageMenu;
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
 * @author arjun created on 13/01/2016
 *         A Multi Player Menu fragment containing a simple view.
 */
public class MultiPlayerMenuFragment extends Fragment {

    public static String TAG = MultiPlayerMenuFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private List<MultiPlayerPageMenu> multiPlayerPageMenus;
    private Tracker tracker;

    public MultiPlayerMenuFragment() {
    }

    public static Fragment newInstance() {
        return new MultiPlayerMenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_player, container, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 6);
        gridLayoutManager.setSpanSizeLookup(new MenuSpanSizeLookup(multiPlayerPageMenus));
        recyclerView = (RecyclerView) view.findViewById(R.id.menus);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(new MenuRecyclerAdapter(getContext(), multiPlayerPageMenus,
                new MenuRecyclerAdapter.ViewHolder.ViewHolderClicks() {
                    @Override
                    public void onClick(View v, int position) {
                        handleMenuClick(position);
                    }
                }));
        return view;
    }

    private void handleMenuClick(int position) {
        MultiPlayerPageMenu multiPlayerPageMenu = multiPlayerPageMenus.get(position);
        switch (multiPlayerPageMenu.getType()) {

            case AUTO_MATCH:
                mListener.onAutoMatch();
                break;
            case START_MATCH:
                mListener.onStartMatch();
                break;
            case IN_PROGRESS_MATCHES:
                mListener.onGamesInProgress();
                break;
        }
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Multi Player Menu")
                .setAction(multiPlayerPageMenu.getType().name())
                .setLabel(multiPlayerPageMenu.getName())
                .setValue(multiPlayerPageMenu.getSpanSize())
                .build());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        tracker = GoldTrapApplication.getInstance().getDefaultTracker();
        Gson gson = new Gson();
        InputStream inputStream = getResources().openRawResource(R.raw.multi_player_page_menu);
        multiPlayerPageMenus = gson.fromJson(new JsonReader(new InputStreamReader(inputStream)),
                new TypeToken<List<MultiPlayerPageMenu>>() {
                }.getType());
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
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + TAG);
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public interface OnFragmentInteractionListener {
        void onAutoMatch();

        void onStartMatch();

        void onGamesInProgress();
    }
}
