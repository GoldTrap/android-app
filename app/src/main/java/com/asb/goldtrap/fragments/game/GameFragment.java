package com.asb.goldtrap.fragments.game;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.asb.goldtrap.GoldTrapApplication;
import com.asb.goldtrap.R;
import com.asb.goldtrap.models.boosters.BoosterModel;
import com.asb.goldtrap.models.boosters.impl.BoosterModelImpl;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.conductor.impl.PlayerVsAi;
import com.asb.goldtrap.models.eo.Booster;
import com.asb.goldtrap.models.eo.BoosterType;
import com.asb.goldtrap.models.eo.Level;
import com.asb.goldtrap.models.file.ImageHelper;
import com.asb.goldtrap.models.file.impl.ImageHelperImpl;
import com.asb.goldtrap.models.results.computers.result.ScoreComputer;
import com.asb.goldtrap.models.results.computers.result.impl.ScoreComputerImpl;
import com.asb.goldtrap.models.sound.SoundModel;
import com.asb.goldtrap.models.sound.SoundType;
import com.asb.goldtrap.models.sound.impl.SoundModelImpl;
import com.asb.goldtrap.models.sound.strategy.SoundHelper;
import com.asb.goldtrap.models.sound.strategy.factory.SoundFactory;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.impl.GameOver;
import com.asb.goldtrap.models.states.impl.PlayerTurn;
import com.asb.goldtrap.models.states.impl.SecondaryPlayerTurn;
import com.asb.goldtrap.views.DotBoard;
import com.asb.goldtrap.views.GameCompleteDotBoard;
import com.asb.goldtrap.views.LineType;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import tyrantgit.explosionfield.ExplosionField;

/**
 * GameFragment
 */
public class GameFragment extends Fragment implements GameConductor.GameStateObserver {
    public static final String TAG = GameFragment.class.getSimpleName();
    public static final String LEVEL_RESOURCE = "LEVEL_RESOURCE";
    public static final String LEVEL_CODE = "LEVEL_CODE";
    private FrameLayout gameLayout;
    private DotBoard dotBoard;
    private GameCompleteDotBoard gameCompleteDotBoard;
    private TextView scoreBoard;
    private TextView currentTurn;
    private ImageButton flip;
    private ImageButton extraChance;
    private ImageButton skip;
    private ImageButton help;
    private SwitchCompat sound;
    private OnFragmentInteractionListener mListener;
    private GameConductor conductor;
    private Handler handler = new Handler();
    private Level level;
    private ImageHelper imageHelper;
    private ScoreComputer scoreComputer;
    private Gson gson;
    private String levelCode;
    private BoosterModel boosterModel;
    private Map<BoosterType, Booster> boosterMap;
    private SoundModel soundModel;
    private SoundFactory soundFactory;
    private SoundHelper soundHelper;
    private ExplosionField explosionField;
    private Tracker tracker;

    /**
     * Create an instance of GameFragment
     *
     * @param resourceId The resource Id of the Level
     * @return GameFragment
     */
    public static GameFragment newInstance(int resourceId, String levelCode) {
        GameFragment fragment = new GameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LEVEL_RESOURCE, resourceId);
        bundle.putString(LEVEL_CODE, levelCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_game, container, false);
        dotBoard = (DotBoard) view.findViewById(R.id.dot_board);
        gameCompleteDotBoard =
                (GameCompleteDotBoard) view.findViewById(R.id.game_complete_dot_board);
        dotBoard.setVisibility(View.VISIBLE);
        gameCompleteDotBoard.setVisibility(View.INVISIBLE);
        dotBoard.setGameSnapShot(conductor.getGameSnapshotMap().get(PlayerVsAi.DEFAULT));
        gameCompleteDotBoard
                .setGameSnapShot(conductor.getGameSnapshotMap().get(PlayerVsAi.DEFAULT));
        scoreBoard = (TextView) view.findViewById(R.id.score_board);
        currentTurn = (TextView) view.findViewById(R.id.current_turn);
        handleFlip(view);
        handleExtraChance(view);
        handleSkip(view);
        handleHelp(view);
        handleSound(view);

        gameLayout = (FrameLayout) view.findViewById(R.id.game_layout);

        dotBoard.setmListener(new DotBoard.Listener() {
            @Override
            public void onLineClick(int row, int col, LineType lineType) {
                if (conductor.getState() instanceof PlayerTurn) {
                    if (conductor.playMyTurn(lineType, row, col)) {
                        soundHelper
                                .playSound(conductor.getGameSnapshotMap().get(PlayerVsAi.DEFAULT));
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
                if (conductor.getState() instanceof SecondaryPlayerTurn) {
                    if (conductor.playTheirTurn()) {
                        dotBoard.requestRedraw();
                        updateScoreBoard();
                    }
                }
            }
        });
        gameCompleteDotBoard.setDrawingCacheEnabled(true);
        gameCompleteDotBoard.setDrawingCacheBackgroundColor(Color.parseColor("#FEFEFE"));
        gameCompleteDotBoard.setmListener(new GameCompleteDotBoard.Listener() {
            @Override
            public void animationComplete() {
                final Uri gamePreviewUri = imageHelper
                        .writeFileToDisk(gameCompleteDotBoard.getDrawingCache(), getContext());
                explosionField.explode(gameCompleteDotBoard);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListener
                                .gameOver(levelCode, gson.toJson(
                                        conductor.getGameSnapshotMap()
                                                .get(PlayerVsAi.DEFAULT)),
                                        gamePreviewUri);
                    }
                }, 1000);

            }
        });
        updateScoreBoard();
        return view;
    }

    private void handleHelp(View view) {
        help = (ImageButton) view.findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Click")
                        .setLabel("Help")
                        .build());
                mListener.helpRequested();
            }
        });
    }

    private void handleSound(View view) {
        sound = (SwitchCompat) view.findViewById(R.id.sound);
        SoundType soundType = soundModel.getSoundType();
        soundHelper = soundFactory.getSoundHelper(soundType, getContext());
        sound.setChecked(SoundType.GUITAR == soundType);
        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String label;
                if (isChecked) {
                    soundModel.updateSoundType(SoundType.GUITAR);
                    soundHelper = soundFactory.getSoundHelper(SoundType.GUITAR, getContext());
                    label = "Sound Enabled";
                }
                else {
                    soundModel.updateSoundType(SoundType.MUTE);
                    soundHelper = soundFactory.getSoundHelper(SoundType.MUTE, getContext());
                    label = "Sound Disabled";
                }
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Click")
                        .setLabel(label)
                        .build());
            }
        });
    }

    private void handleSkip(View view) {
        skip = (ImageButton) view.findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label;
                if (boosterMap.get(BoosterType.SKIP).getCount() > 0) {
                    conductor.skipTurn();
                    dotBoard.requestRedraw();
                    boosterMap.put(BoosterType.SKIP,
                            boosterModel.consumeBooster(mListener.getGoogleApiClient(),
                                    BoosterType.SKIP));
                    label = "Skip Consume";
                }
                else {
                    mListener.takeMeToStore(BoosterType.SKIP);
                    label = "Skip Shop";
                }
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Click")
                        .setLabel(label)
                        .build());
            }
        });
    }

    private void handleExtraChance(View view) {
        extraChance = (ImageButton) view.findViewById(R.id.extra_chance);
        extraChance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label;
                if (boosterMap.get(BoosterType.PLUS_ONE).getCount() > 0) {
                    conductor.setExtraChance(true);
                    boosterMap.put(BoosterType.PLUS_ONE,
                            boosterModel.consumeBooster(mListener.getGoogleApiClient(),
                                    BoosterType.PLUS_ONE));
                    label = "Plus One Consume";
                }
                else {
                    mListener.takeMeToStore(BoosterType.PLUS_ONE);
                    label = "Plus One Shop";
                }

                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Click")
                        .setLabel(label)
                        .build());
            }
        });
    }

    private void handleFlip(View view) {
        flip = (ImageButton) view.findViewById(R.id.flip);
        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label;
                if (boosterMap.get(BoosterType.FLIP).getCount() > 0) {
                    conductor.flipBoard();
                    dotBoard.requestRedraw();
                    boosterMap.put(BoosterType.FLIP, boosterModel
                            .consumeBooster(mListener.getGoogleApiClient(), BoosterType.FLIP));
                    label = "Flip Consume";
                }
                else {
                    mListener.takeMeToStore(BoosterType.FLIP);
                    label = "Flip Shop";
                }
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Click")
                        .setLabel(label)
                        .build());
            }
        });
    }

    private void updateScoreBoard() {
        scoreComputer.computeScore();
        scoreBoard.setText(
                getString(R.string.points,
                        conductor.getGameSnapshotMap().get(PlayerVsAi.DEFAULT).getScore()
                                .basicScore()));
        if (conductor.getState() instanceof PlayerTurn) {
            currentTurn.setText(R.string.your_turn);
        }
        else if (conductor.getState() instanceof GameOver) {
            currentTurn.setText(R.string.game_over);
        }
        else {
            currentTurn.setText(R.string.opponents_turn);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        tracker = GoldTrapApplication.getInstance().getDefaultTracker();
        explosionField = ExplosionField.attach2Window(getActivity());
        soundModel = new SoundModelImpl(getContext());
        soundFactory = new SoundFactory();
        Bundle args = getArguments();
        imageHelper = new ImageHelperImpl();
        boosterModel = new BoosterModelImpl(getContext());
        int resourceId = args.getInt(LEVEL_RESOURCE);
        levelCode = args.getString(LEVEL_CODE);
        gson = new Gson();
        doGSONStuff(resourceId);
        startGame();
    }

    private void doGSONStuff(int resourceId) {
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
    public void onResume() {
        super.onResume();
        boosterMap = boosterModel.getBoostersState();
        Log.i(TAG, "Setting screen name: " + TAG);
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        mListener.hideAppbar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void startGame() {
        conductor = new PlayerVsAi(this, level);
        conductor.setState(conductor.getFirstPlayerState());
        scoreComputer =
                new ScoreComputerImpl(conductor.getGameSnapshotMap().get(PlayerVsAi.DEFAULT));
    }

    @Override
    public void stateChanged(GameState state) {
        if (state instanceof GameOver) {

        }
    }

    public interface OnFragmentInteractionListener {
        GoogleApiClient getGoogleApiClient();

        void gameOver(String levelCode, String snapshot, Uri gamePreviewUri);

        void takeMeToStore(BoosterType boosterType);

        void hideAppbar();

        void helpRequested();
    }

}
