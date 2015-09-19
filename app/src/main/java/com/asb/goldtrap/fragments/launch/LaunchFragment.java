package com.asb.goldtrap.fragments.launch;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.conductor.impl.AiVsAi;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.impl.GameOver;
import com.asb.goldtrap.views.DotBoard;
import com.asb.goldtrap.views.LineType;

import java.util.Random;

/**
 * Launch Fragment
 */
public class LaunchFragment extends Fragment implements GameConductor.GameStateObserver,
        DotBoard.Listener {
    public static final String TAG = LaunchFragment.class.getSimpleName();
    public static final int MIN_ROWS = 4;
    public static final int MIN_COLS = 4;
    public static final int ADDITIONAL_ROWS = 3;
    public static final int ADDITIONAL_COLS = 3;
    public static final int DELAY_BETWEEN_GAMES_IN_MILLIS = 2000;
    private Random random = new Random();
    private DotBoard dotBoard;
    private TextView loading;
    private GameConductor conductor;

    private Handler handler = new Handler();

    public LaunchFragment() {
        // Required empty public constructor
    }

    /**
     * New Instance
     *
     * @return A new instance of fragment LaunchFragment.
     */
    public static LaunchFragment newInstance() {
        return new LaunchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        startGame();
    }

    private void startGame() {
        int row = MIN_ROWS + random.nextInt(ADDITIONAL_ROWS);
        int col = MIN_COLS + random.nextInt(ADDITIONAL_COLS);
        conductor = new AiVsAi(this, row, col, (row * col) / 3);
        dotBoard.setGameSnapShot(conductor.getGameSnapshot());
        if (random.nextBoolean()) {
            conductor.setState(conductor.getFirstPlayerState());
            firstPlayerTurn();
        }
        else {
            conductor.setState(conductor.getOtherPlayerState());
            otherPlayerTurn();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launch, container, false);
        dotBoard = (DotBoard) view.findViewById(R.id.dot_board);
        dotBoard.setmListener(this);
        loading = (TextView) view.findViewById(R.id.loading);
        return view;
    }

    @Override
    public void stateChanged(GameState state) {
        if (state instanceof GameOver) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startGame();
                }
            }, DELAY_BETWEEN_GAMES_IN_MILLIS);
        }
    }

    @Override
    public void onLineClick(int row, int col, LineType lineType) {

    }

    @Override
    public void animationComplete() {
        if (conductor.getState() == conductor.getFirstPlayerState()) {
            firstPlayerTurn();
        }
        else if (conductor.getState() == conductor.getSecondPlayerState()) {
            otherPlayerTurn();
        }
    }

    private void firstPlayerTurn() {
        if (conductor.playFirstPlayerTurn()) {
            dotBoard.requestRedraw();
        }
    }

    private void otherPlayerTurn() {
        if (conductor.playSecondPlayerTurn()) {
            dotBoard.requestRedraw();
        }
    }
}
