package com.asb.goldtrap.fragments.multiplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.MenuRecyclerAdapter;
import com.asb.goldtrap.models.menu.impl.MultiPlayerPageMenu;
import com.asb.goldtrap.spansize.MenuSpanSizeLookup;
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
                    public void onClick(int position) {
                        handleMenuClick(position);
                    }
                }));
        return view;
    }

    private void handleMenuClick(int position) {
        switch (multiPlayerPageMenus.get(position).getType()) {

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
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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

    public interface OnFragmentInteractionListener {
        void onAutoMatch();

        void onStartMatch();

        void onGamesInProgress();
    }
}
