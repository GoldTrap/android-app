package com.asb.goldtrap.fragments.launch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.conductor.impl.AiVsAi;
import com.asb.goldtrap.models.services.DataInitializationService;
import com.asb.goldtrap.models.solvers.factory.impl.BiasedTowardsMeSolversFactory;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.impl.GameOver;
import com.asb.goldtrap.views.DotBoard;
import com.asb.goldtrap.views.GameCompleteDotBoard;
import com.asb.goldtrap.views.LineType;
import com.google.android.gms.common.SignInButton;

import java.util.Random;


/**
 * Launch Fragment
 */
public class LaunchFragment extends Fragment implements GameConductor.GameStateObserver,
        View.OnClickListener,
        DotBoard.Listener {
    public static final String TAG = LaunchFragment.class.getSimpleName();
    public static final int MIN_ROWS = 4;
    public static final int MIN_COLS = 4;
    public static final int ADDITIONAL_ROWS = 3;
    public static final int ADDITIONAL_COLS = 3;
    public static final int DELAY_BETWEEN_GAMES_IN_MILLIS = 5000;
    public static final int TIME_BETWEEN_LOADING_MESSAGE_UPDATES = 1500;
    private boolean migrationComplete = false;
    private OnFragmentInteractionListener mListener;
    private SignInButton signInButton;
    private FloatingActionButton launchButton;
    private Random random = new Random();
    private FrameLayout gameLayout;
    private DotBoard dotBoard;
    private GameCompleteDotBoard gameCompleteDotBoard;
    private ProgressBar progressBar;
    private TextView loading;
    private GameConductor conductor;
    private String[] loadingMessages;
    private int msgIndex = 0;
    private int[][] themes = {
            {R.array.default_theme, R.array.default_game_complete_theme},
            {R.array.experimental_theme, R.array.experimental_game_complete_theme}
    };
    private Handler handler = new Handler();

    private Runnable mUpdateLoading = new Runnable() {
        @Override
        public void run() {
            if (null != loading) {
                loading.setText(loadingMessages[msgIndex % loadingMessages.length]);
                msgIndex += 1;
            }
            handler.postAtTime(this, SystemClock.uptimeMillis() +
                    TIME_BETWEEN_LOADING_MESSAGE_UPDATES);
        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            migrationComplete();
        }
    };

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
        setRetainInstance(true);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,
                new IntentFilter(DataInitializationService.INITIALIZATION_COMPLETE));
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        startGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launch, container, false);
        gameLayout = (FrameLayout) view.findViewById(R.id.game_layout);
        dotBoard = (DotBoard) view.findViewById(R.id.dot_board);
        signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        launchButton = (FloatingActionButton) view.findViewById(R.id.launch_button);
        launchButton.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        dotBoard.setmListener(this);
        gameCompleteDotBoard =
                (GameCompleteDotBoard) view.findViewById(R.id.game_complete_dot_board);
        gameCompleteDotBoard.setmListener(new GameCompleteDotBoard.Listener() {
            @Override
            public void animationComplete() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != mListener) {
                            startGame();
                        }
                    }
                }, DELAY_BETWEEN_GAMES_IN_MILLIS);
            }
        });
        loading = (TextView) view.findViewById(R.id.loading);
        loadingMessages = getResources().getStringArray(R.array.loading);
        handler.post(mUpdateLoading);

        if (mListener.isConnected()) {
            signInButton.setVisibility(View.GONE);
        }
        launchButton.setVisibility(View.GONE);
        DataInitializationService.startMigration(getContext());
        showLaunchButton();
        return view;
    }

    @Override
    public void stateChanged(GameState state) {

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
    public void onLineClick(int row, int col, LineType lineType) {

    }

    @Override
    public void animationComplete() {
        if (conductor.getState() instanceof GameOver) {
            gameCompleteDotBoard.setVisibility(View.VISIBLE);
            dotBoard.setVisibility(View.INVISIBLE);
            gameCompleteDotBoard.requestRedraw();
        }
        if (conductor.getState() == conductor.getFirstPlayerState()) {
            firstPlayerTurn();
        }
        else if (conductor.getState() == conductor.getSecondPlayerState()) {
            otherPlayerTurn();
        }
    }

    private void startGame() {
        int[] gameTheme = themes[random.nextInt(themes.length)];
        gameCompleteDotBoard.setVisibility(View.INVISIBLE);
        dotBoard.setVisibility(View.VISIBLE);
        int row = MIN_ROWS + random.nextInt(ADDITIONAL_ROWS);
        int col = MIN_COLS + random.nextInt(ADDITIONAL_COLS);
        conductor =
                new AiVsAi(new BiasedTowardsMeSolversFactory(), this, row, col, (row * col) / 3, 0);
        dotBoard.setGameSnapShot(conductor.getGameSnapshot());
        dotBoard.setColors(getResources().getIntArray(gameTheme[0]));
        gameCompleteDotBoard.setGameSnapShot(conductor.getGameSnapshot());
        gameCompleteDotBoard.setColors(getResources().getIntArray(gameTheme[1]));

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

    private void migrationComplete() {
        migrationComplete = true;
        if (!mListener.isSignInInProgress()) {
            showLaunchButton();
        }
        mListener.migrationComplete();
    }

    private void showLaunchButton() {
        if (migrationComplete) {
            launchButton.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                handleSignIn();
                break;
            case R.id.launch_button:
                handleLaunch();
                break;
        }
    }

    private void handleLaunch() {
        mListener.launch();
    }

    private void handleSignIn() {
        mListener.signIn();
        loading.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        launchButton.setVisibility(View.GONE);
    }

    public interface OnFragmentInteractionListener {
        void signIn();

        boolean isSignInInProgress();

        boolean isConnected();

        void launch();

        void migrationComplete();
    }
}
