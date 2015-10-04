package com.asb.goldtrap.fragments.launch;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.conductor.impl.AiVsAi;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.impl.GameOver;
import com.asb.goldtrap.views.DotBoard;
import com.asb.goldtrap.views.GameCompleteDotBoard;
import com.asb.goldtrap.views.LineType;
import com.google.android.gms.common.SignInButton;

import java.util.Random;

/**
 * Home Fragment
 */
public class HomeFragment extends Fragment implements GameConductor.GameStateObserver {

    public static final String TAG = HomeFragment.class.getSimpleName();
    public static final int MIN_ROWS = 4;
    public static final int MIN_COLS = 4;
    public static final int ADDITIONAL_ROWS = 1;
    public static final int ADDITIONAL_COLS = 1;
    public static final int DELAY_BETWEEN_GAMES_IN_MILLIS = 5000;

    private OnFragmentInteractionListener mListener;
    private Random random = new Random();
    private FrameLayout gameLayout;
    private DotBoard dotBoard;
    private GameCompleteDotBoard gameConductorDotBoard;
    private GameConductor conductor;
    private Button quickPlay;
    private Button signOut;
    private SignInButton signInButton;
    private int[][] themes = {
            {R.array.default_theme, R.array.default_game_complete_theme},
            {R.array.experimental_theme, R.array.experimental_game_complete_theme}
    };

    private Handler handler = new Handler();

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
    }

    @Override
    public void onResume() {
        super.onResume();
        startGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        signOut = (Button) view.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInButton.setVisibility(View.VISIBLE);
                signOut.setVisibility(View.GONE);
                mListener.signOut();
            }
        });
        signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInButton.setVisibility(View.GONE);
                signOut.setVisibility(View.VISIBLE);
                mListener.signIn();
            }
        });
        if (mListener.isConnected()) {
            signInButton.setVisibility(View.GONE);
            signOut.setVisibility(View.VISIBLE);
        }
        else {
            signInButton.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.GONE);
        }
        quickPlay = (Button) view.findViewById(R.id.quick_play);
        quickPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.quickPlay();
            }
        });
        gameLayout = (FrameLayout) view.findViewById(R.id.game_layout);
        dotBoard = (DotBoard) view.findViewById(R.id.dot_board);
        dotBoard.setmListener(new DotBoard.Listener() {
            @Override
            public void onLineClick(int row, int col, LineType lineType) {

            }

            @Override
            public void animationComplete() {
                if (conductor.getState() instanceof GameOver) {
                    gameLayout.removeAllViews();
                    gameLayout.addView(gameConductorDotBoard);
                    gameConductorDotBoard.requestRedraw();
                }
                if (conductor.getState() == conductor.getFirstPlayerState()) {
                    firstPlayerTurn();
                }
                else if (conductor.getState() == conductor.getSecondPlayerState()) {
                    otherPlayerTurn();
                }
            }
        });

        gameConductorDotBoard = new GameCompleteDotBoard(getContext(), null);
        gameConductorDotBoard.setmListener(new GameCompleteDotBoard.Listener() {
            @Override
            public void animationComplete() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startGame();
                    }
                }, DELAY_BETWEEN_GAMES_IN_MILLIS);
            }
        });
        return view;
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

    private void startGame() {
        int[] gameTheme = themes[random.nextInt(themes.length)];
        gameLayout.removeAllViews();
        gameLayout.addView(dotBoard);
        int row = MIN_ROWS + random.nextInt(ADDITIONAL_ROWS);
        int col = MIN_COLS + random.nextInt(ADDITIONAL_COLS);
        conductor = new AiVsAi(this, row, col, (row * col) / 3);
        dotBoard.setGameSnapShot(conductor.getGameSnapshot());
        dotBoard.setColors(getResources().getIntArray(gameTheme[0]));
        gameConductorDotBoard.setGameSnapShot(conductor.getGameSnapshot());
        gameConductorDotBoard.setColors(getResources().getIntArray(gameTheme[1]));

        if (random.nextBoolean()) {
            conductor.setState(conductor.getFirstPlayerState());
            firstPlayerTurn();
        }
        else {
            conductor.setState(conductor.getOtherPlayerState());
            otherPlayerTurn();
        }
    }

    private void firstPlayerTurn() {
        if (conductor.playMyTurn()) {
            dotBoard.requestRedraw();
        }
    }

    private void otherPlayerTurn() {
        if (conductor.playTheirTurn()) {
            dotBoard.requestRedraw();
        }
    }

    public interface OnFragmentInteractionListener {
        boolean isConnected();

        void signOut();

        void signIn();

        void quickPlay();
    }

}
