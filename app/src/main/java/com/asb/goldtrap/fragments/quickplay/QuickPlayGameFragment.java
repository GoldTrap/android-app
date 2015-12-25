package com.asb.goldtrap.fragments.quickplay;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.conductor.impl.PlayerVsAi;
import com.asb.goldtrap.models.eo.Level;
import com.asb.goldtrap.models.file.ImageHelper;
import com.asb.goldtrap.models.file.impl.ImageHelperImpl;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.impl.AITurn;
import com.asb.goldtrap.models.states.impl.GameOver;
import com.asb.goldtrap.models.states.impl.PlayerTurn;
import com.asb.goldtrap.views.DotBoard;
import com.asb.goldtrap.views.GameCompleteDotBoard;
import com.asb.goldtrap.views.LineType;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * QuickPlayGameFragment
 */
public class QuickPlayGameFragment extends Fragment implements GameConductor.GameStateObserver {
    private static final int MIN_ROWS = 4;
    private static final int MIN_COLS = 4;
    private static final int ADDITIONAL_ROWS = 3;
    private static final int ADDITIONAL_COLS = 3;
    public static final String TAG = QuickPlayGameFragment.class.getSimpleName();
    public static final String LEVEL_RESOURCE = "LEVEL_RESOURCE";
    private FrameLayout gameLayout;
    private DotBoard dotBoard;
    private GameCompleteDotBoard gameCompleteDotBoard;
    private TextView scoreBoard;
    private Button flip;
    private Button extraChance;
    private OnFragmentInteractionListener mListener;
    private Random random = new Random();
    private GameConductor conductor;
    private Handler handler = new Handler();
    private Level level;
    private ImageHelper imageHelper;

    /**
     * Create an instance of QuickPlayGameFragment
     *
     * @param resourceId The resource Id of the Level
     * @return QuickPlayGameFragment
     */
    public static QuickPlayGameFragment newInstance(int resourceId) {
        QuickPlayGameFragment fragment = new QuickPlayGameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LEVEL_RESOURCE, resourceId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public QuickPlayGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_play_game, container, false);
        dotBoard = (DotBoard) view.findViewById(R.id.dot_board);
        gameCompleteDotBoard =
                (GameCompleteDotBoard) view.findViewById(R.id.game_complete_dot_board);
        dotBoard.setVisibility(View.VISIBLE);
        gameCompleteDotBoard.setVisibility(View.INVISIBLE);
        dotBoard.setGameSnapShot(conductor.getGameSnapshot());
        gameCompleteDotBoard.setGameSnapShot(conductor.getGameSnapshot());
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
                    dotBoard.setVisibility(View.INVISIBLE);
                    gameCompleteDotBoard.setVisibility(View.VISIBLE);
                    gameCompleteDotBoard.requestRedraw();
                }
                conductor.doPostProcess();
                dotBoard.postInvalidate();
                if (conductor.getState() instanceof AITurn) {
                    if (conductor.playTheirTurn()) {
                        dotBoard.requestRedraw();
                        updateScoreBoard();
                    }
                }
            }
        });
        gameCompleteDotBoard.setDrawingCacheEnabled(true);
        gameCompleteDotBoard.setDrawingCacheBackgroundColor(Color.LTGRAY);
        gameCompleteDotBoard.setmListener(new GameCompleteDotBoard.Listener() {
            @Override
            public void animationComplete() {
                Uri uri = imageHelper
                        .writeFileToDisk(gameCompleteDotBoard.getDrawingCache(), getContext());
                conductor.getGameSnapshot().setImageUri(uri);
                mListener.gameOver(conductor.getGameSnapshot());
            }
        });
        updateScoreBoard();
        return view;
    }

    private void updateScoreBoard() {
        scoreBoard.setText(
                getString(R.string.points, conductor.getGameSnapshot().getScore().basicScore()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle args = getArguments();
        imageHelper = new ImageHelperImpl();
        int resourceId = args.getInt(LEVEL_RESOURCE);
        doGSONStuff(resourceId);
        startGame();
    }

    private void doGSONStuff(int resourceId) {
        Gson gson = new Gson();
        InputStream inputStream = getResources().openRawResource(resourceId);
        level = gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), Level.class);
        Log.d(TAG, gson.toJson(level));
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

    private void startGame() {
        conductor = new PlayerVsAi(this, level);
        conductor.setState(conductor.getFirstPlayerState());
    }

    @Override
    public void stateChanged(GameState state) {
        if (state instanceof GameOver) {

        }
    }

    public interface OnFragmentInteractionListener {
        void gameOver(DotsGameSnapshot dotsGameSnapshot);
    }

}
