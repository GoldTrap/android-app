package com.asb.goldtrap.fragments.multiplayer;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.conductor.impl.PlayerVsPlayer;
import com.asb.goldtrap.models.file.ImageHelper;
import com.asb.goldtrap.models.file.impl.ImageHelperImpl;
import com.asb.goldtrap.models.results.computers.result.ScoreComputer;
import com.asb.goldtrap.models.results.computers.result.impl.ScoreComputerImpl;
import com.asb.goldtrap.models.snapshots.GameAndLevelSnapshot;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.impl.GameOver;
import com.asb.goldtrap.models.states.impl.PlayerTurn;
import com.asb.goldtrap.models.states.impl.SecondaryPlayerTurn;
import com.asb.goldtrap.views.DotBoard;
import com.asb.goldtrap.views.GameCompleteDotBoard;
import com.asb.goldtrap.views.LineType;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.gson.Gson;

/**
 * A MultiPlayerGame {@link Fragment} subclass.
 */
public class MultiPlayerGameFragment extends Fragment implements GameConductor.GameStateObserver {
    public static final String TAG = MultiPlayerGameFragment.class.getSimpleName();
    private static final String GAME_AND_LEVEL = "gameAndLevelSnapshot";
    public static final String MY_PLAYER_ID = "MY_PLAYER_ID";
    public static final String TURN_STATUS = "TURN_STATUS";
    public static final String STATUS = "STATUS";
    private GameConductor conductor;
    private GameAndLevelSnapshot gameAndLevelSnapshot;
    private OnFragmentInteractionListener mListener;
    private Gson gson;
    private String myPlayerId;
    private FrameLayout gameLayout;
    private DotBoard dotBoard;
    private GameCompleteDotBoard gameCompleteDotBoard;
    private TextView scoreBoard;
    private Button flip;
    private Button extraChance;
    private Handler handler = new Handler();
    private ImageHelper imageHelper;
    private ScoreComputer scoreComputer;
    private int status;
    private int turnStatus;


    public MultiPlayerGameFragment() {
        // Required empty public constructor
    }

    /**
     * MultiPlayerGameFragment factory method
     *
     * @param gameAndLevel game and level.
     * @param turnStatus
     * @param status
     * @return A new instance of fragment MultiPlayerGameFragment.
     */
    public static MultiPlayerGameFragment newInstance(String gameAndLevel, String myPlayerId,
                                                      int turnStatus, int status) {
        MultiPlayerGameFragment fragment = new MultiPlayerGameFragment();
        Bundle args = new Bundle();
        args.putString(GAME_AND_LEVEL, gameAndLevel);
        args.putString(MY_PLAYER_ID, myPlayerId);
        args.putInt(TURN_STATUS, turnStatus);
        args.putInt(STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        imageHelper = new ImageHelperImpl();
        gson = new Gson();
        myPlayerId = getArguments().getString(MY_PLAYER_ID);
        gameAndLevelSnapshot = gson.fromJson(getArguments().getString(GAME_AND_LEVEL),
                GameAndLevelSnapshot.class);
        status = getArguments().getInt(STATUS);
        turnStatus = getArguments().getInt(TURN_STATUS);
        initGame();
    }

    private void initGame() {
        conductor = new PlayerVsPlayer(this, gameAndLevelSnapshot, myPlayerId);
        if (TurnBasedMatch.MATCH_STATUS_COMPLETE == status) {
            conductor.setState(conductor.getGameOverState());
        }
        else {
            if (TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN == turnStatus) {
                conductor.setState(conductor.getFirstPlayerState());
            }
            else {
                conductor.setState(conductor.getSecondPlayerState());
            }
        }
        scoreComputer = new ScoreComputerImpl(conductor.getGameSnapshotMap().get(myPlayerId));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_player_game, container, false);
        dotBoard = (DotBoard) view.findViewById(R.id.dot_board);
        gameCompleteDotBoard =
                (GameCompleteDotBoard) view.findViewById(R.id.game_complete_dot_board);
        dotBoard.setVisibility(View.VISIBLE);
        gameCompleteDotBoard.setVisibility(View.INVISIBLE);
        updateGameBoard();
        scoreBoard = (TextView) view.findViewById(R.id.score_board);
        flip = (Button) view.findViewById(R.id.flip);
        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conductor.flipBoard();
                dotBoard.requestRedraw();
            }
        });

        extraChance = (Button) view.findViewById(R.id.extra_chance);
        extraChance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conductor.setExtraChance(true);
            }
        });

        gameLayout = (FrameLayout) view.findViewById(R.id.game_layout);

        dotBoard.setmListener(new DotBoard.Listener() {
            @Override
            public void onLineClick(int row, int col, LineType lineType) {
                if (conductor.getState() instanceof PlayerTurn) {
                    if (conductor.playMyTurn(lineType, row, col)) {
                        dotBoard.requestRedraw();
                        updateScoreBoard();
                    }
                }
            }

            @Override
            public void animationComplete() {
                if (conductor.getState() instanceof GameOver) {
                    mListener.gameOver(gameAndLevelSnapshot);
                }
                conductor.doPostProcess();
                dotBoard.postInvalidate();
                if (conductor.getState() instanceof SecondaryPlayerTurn) {
                    mListener.onMyTurnComplete(gameAndLevelSnapshot);
                }
            }
        });
        gameCompleteDotBoard.setDrawingCacheEnabled(true);
        gameCompleteDotBoard.setDrawingCacheBackgroundColor(Color.LTGRAY);
        gameCompleteDotBoard.setmListener(new GameCompleteDotBoard.Listener() {
            @Override
            public void animationComplete() {
                final Uri gamePreviewUri = imageHelper
                        .writeFileToDisk(gameCompleteDotBoard.getDrawingCache(), getContext());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (status == TurnBasedMatch.MATCH_STATUS_COMPLETE) {
                            mListener.showPostGameOverOptions(
                                    gamePreviewUri);
                        }
                    }
                }, 1000);

            }
        });
        updateScoreBoard();
        selectBoardToDisplay();
        return view;
    }

    private void selectBoardToDisplay() {
        dotBoard.postInvalidate();
        if (status == TurnBasedMatch.MATCH_STATUS_COMPLETE) {
            dotBoard.setVisibility(View.INVISIBLE);
            gameCompleteDotBoard.setVisibility(View.VISIBLE);
            gameCompleteDotBoard.requestRedraw();
        }
    }

    private void updateScoreBoard() {
        scoreComputer.computeScore();
        scoreBoard.setText(
                getString(R.string.points,
                        conductor.getGameSnapshotMap().get(myPlayerId).getScore().basicScore()));
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
    public void stateChanged(GameState state) {

    }

    public void updateGame(GameAndLevelSnapshot snapshot, String myPlayerId, int turnStatus,
                           int status) {
        this.gameAndLevelSnapshot = snapshot;
        this.myPlayerId = myPlayerId;
        this.turnStatus = turnStatus;
        this.status = status;
        initGame();
        updateGameBoard();
        updateScoreBoard();
        selectBoardToDisplay();
    }

    private void updateGameBoard() {
        dotBoard.setGameSnapShot(conductor.getGameSnapshotMap().get(myPlayerId));
        gameCompleteDotBoard.setGameSnapShot(conductor.getGameSnapshotMap().get(myPlayerId));
    }

    public interface OnFragmentInteractionListener {
        void onMyTurnComplete(GameAndLevelSnapshot gameAndLevelSnapshot);

        void gameOver(GameAndLevelSnapshot gameAndLevel);

        void showPostGameOverOptions(Uri gamePreviewUri);
    }
}
