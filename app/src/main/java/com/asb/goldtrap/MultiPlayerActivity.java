package com.asb.goldtrap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.fragments.multiplayer.MultiPlayerGameFragment;
import com.asb.goldtrap.fragments.multiplayer.MultiPlayerMenuFragment;
import com.asb.goldtrap.fragments.postgame.SummaryFragment;
import com.asb.goldtrap.models.achievements.AchievementsModel;
import com.asb.goldtrap.models.achievements.impl.MultiplayerAchievementsModel;
import com.asb.goldtrap.models.eo.BoosterType;
import com.asb.goldtrap.models.eo.Level;
import com.asb.goldtrap.models.factory.GameSnapshotCreator;
import com.asb.goldtrap.models.gameplay.GameTypes;
import com.asb.goldtrap.models.leaderboards.LeaderboardsModel;
import com.asb.goldtrap.models.leaderboards.impl.LeaderboardsModelImpl;
import com.asb.goldtrap.models.results.computers.result.ScoreComputer;
import com.asb.goldtrap.models.results.computers.result.impl.ScoreComputerImpl;
import com.asb.goldtrap.models.scores.ScoreModel;
import com.asb.goldtrap.models.scores.impl.MultiplayerScoreModelImpl;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.snapshots.GameAndLevelSnapshot;
import com.asb.goldtrap.models.tutorial.TutorialModel;
import com.asb.goldtrap.models.tutorial.impl.TutorialModelImpl;
import com.asb.goldtrap.models.utils.sharer.Sharer;
import com.asb.goldtrap.models.utils.sharer.impl.SharerImpl;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.android.gms.games.stats.PlayerStats;
import com.google.android.gms.games.stats.Stats;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class MultiPlayerActivity extends AppCompatActivity
        implements MultiPlayerMenuFragment.OnFragmentInteractionListener,
        MultiPlayerGameFragment.OnFragmentInteractionListener,
        SummaryFragment.OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnTurnBasedMatchUpdateReceivedListener {

    public static final String TAG = MultiPlayerActivity.class.getSimpleName();
    public static final String CHARSET_NAME = "UTF-8";
    public static final String TBM = "tbm";
    private static final int REQUEST_CODE = 21000;
    private final ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>
            initiateMatchResultResultCallback =
            new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
                @Override
                public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
                    processResult(result);
                }
            };
    private final ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>
            updateMatchResultResultCallback =
            new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
                @Override
                public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                    processResult(result, true);
                }
            };

    private GoogleApiClient mGoogleApiClient;

    private boolean mResolvingConnectionFailure = false;

    private boolean mSignInClicked = false;

    private boolean mAutoStartSignInFlow = true;

    // For our intents
    private static final int RC_SIGN_IN = 9001;
    private final static int RC_SELECT_PLAYERS = 10000;
    private final static int RC_LOOK_AT_MATCHES = 10001;
    private static final int REQUEST_INVITE = 15001;

    public boolean isDoingTurn = false;

    private TurnBasedMatch tbm;
    private Gson gson;
    private GameAndLevelSnapshot gameAndLevelSnapshot;
    private Sharer sharer;
    private ScoreModel scoreModel;
    private LeaderboardsModel leaderboardsModel;
    private AchievementsModel achievementsModel;
    private PlayerStats playerStats;
    private TutorialModel tutorialModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            tbm = savedInstanceState.getParcelable(TBM);
        }
        setContentView(R.layout.activity_multi_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        MobileAds.initialize(getApplicationContext(), getString(R.string.application_id));
        gson = new Gson();
        sharer = new SharerImpl();
        scoreModel = new MultiplayerScoreModelImpl(getApplicationContext());
        leaderboardsModel = new LeaderboardsModelImpl(getApplicationContext());
        achievementsModel = new MultiplayerAchievementsModel(getApplicationContext());
        tutorialModel = new TutorialModelImpl(getApplicationContext(), GameTypes.MULTI_PLAYER);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
        if (null == getSupportFragmentManager().findFragmentByTag(
                MultiPlayerMenuFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(
                        MultiPlayerGameFragment.TAG)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, MultiPlayerMenuFragment.newInstance(),
                            MultiPlayerMenuFragment.TAG)
                    .commit();
        }
        retrieveMatchFromHint(getIntent().getExtras());
    }

    private void checkPlayerStats() {
        PendingResult<Stats.LoadPlayerStatsResult> result =
                Games.Stats.loadPlayerStats(mGoogleApiClient, false);
        result.setResultCallback(new ResultCallback<Stats.LoadPlayerStatsResult>() {
            public void onResult(Stats.LoadPlayerStatsResult result) {
                Status status = result.getStatus();
                if (status.isSuccess()) {
                    playerStats = result.getPlayerStats();
                    if (playerStats != null) {
                        Log.d(TAG, "Player stats loaded");
                        if (playerStats.getDaysSinceLastPlayed() > 7) {
                            Log.d(TAG,
                                    "It's been longer than a week");
                        }
                        if (playerStats.getNumberOfSessions() > 1000) {
                            Log.d(TAG, "Veteran player");
                        }
                        if (playerStats.getChurnProbability() == 1) {
                            Log.d(TAG,
                                    "Player is at high risk of churn");
                        }
                    }
                }
                else {
                    Log.d(TAG,
                            "Failed to fetch Stats Data status: "
                                    + status.getStatusMessage());
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TBM, tbm);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (hasFocus) {
                int uiVisibilityCode =
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    uiVisibilityCode |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                }
                getWindow().getDecorView().setSystemUiVisibility(uiVisibilityCode);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart(): Connecting to Google APIs");
        mGoogleApiClient.connect();
        showSpinner();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissSpinner();
        Log.d(TAG, "onStop(): Disconnecting from Google APIs");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        switch (request) {
            case RC_SIGN_IN:
                handleSignin(request, response);
                break;
            case RC_LOOK_AT_MATCHES:
                handleBrowseMatches(response, data);
                break;
            case RC_SELECT_PLAYERS:
                handleCreateMatch(response, data);
                break;
        }
    }

    @Override
    public void onAutoMatch() {
        showSpinner();
        startMatch(TurnBasedMatchConfig.builder()
                .setAutoMatchCriteria(RoomConfig.createAutoMatchCriteria(
                        1, 1, 0)).build());
    }

    private void startMatch(TurnBasedMatchConfig tbmc) {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            Games.TurnBasedMultiplayer.createMatch(mGoogleApiClient, tbmc)
                    .setResultCallback(initiateMatchResultResultCallback);
        }
        else {
            showMessage(getString(R.string.connect_to_google_play_games));
        }
    }

    @Override
    public void onMyTurnComplete(GameAndLevelSnapshot gameAndLevelSnapshot) {
        showSpinner();
        takeTurn(gameAndLevelSnapshot, getNextParticipantId(), false);
    }

    @Override
    public void gameOver(final GameAndLevelSnapshot gameAndLevel) {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            String playerId = Games.Players.getCurrentPlayerId(mGoogleApiClient);
            final String myParticipantId = tbm.getParticipantId(playerId);
            gameAndLevelSnapshot = gameAndLevel;
            gameAndLevel.setLastPlayerId(myParticipantId);
            Games.TurnBasedMultiplayer.finishMatch(mGoogleApiClient, tbm.getMatchId(),
                    gson.toJson(gameAndLevel).getBytes(Charset.forName(CHARSET_NAME)))
                    .setResultCallback(updateMatchResultResultCallback);
        }
        else {
            showMessage(getString(R.string.connect_to_google_play_games));
        }
    }

    @Override
    public void showPostGameOverOptions(Uri gamePreviewUri) {
        GoldTrapApplication.getInstance().setGamePreviewUri(gamePreviewUri);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container,
                        SummaryFragment.newInstance(gamePreviewUri),
                        SummaryFragment.TAG)
                .addToBackStack(SummaryFragment.TAG)
                .commit();
    }

    @Override
    public void takeMeToStore(BoosterType boosterType) {
        Intent intent = new Intent(getApplicationContext(), ShopORamaActivity.class);
        intent.putExtra(ShopORamaActivity.BOOSTER_TYPE, boosterType.name());
        startActivity(intent);
    }

    @Override
    public void onStartMatch() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(mGoogleApiClient,
                    1, 1, true);
            startActivityForResult(intent, RC_SELECT_PLAYERS);
        }
        else {
            showMessage(getString(R.string.connect_to_google_play_games));
        }
    }

    @Override
    public void onGamesInProgress() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            Intent intent = Games.TurnBasedMultiplayer.getInboxIntent(mGoogleApiClient);
            startActivityForResult(intent, RC_LOOK_AT_MATCHES);
        }
        else {
            showMessage(getString(R.string.connect_to_google_play_games));
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        dismissSpinner();
        Log.d(TAG, "onConnected(): Connection successful");

        // Retrieve the TurnBasedMatch from the connectionHint
        retrieveMatchFromHint(connectionHint);
        if (null != tbm) {
            updateMatch(tbm);
        }
        checkPlayerStats();
        Games.TurnBasedMultiplayer.registerMatchUpdateListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended():  Trying to reconnect.");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        dismissSpinner();
        Log.d(TAG, "onConnectionFailed(): attempting to resolve");
        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed(): ignoring connection failure, already resolving.");
        }
        else {
            if (mSignInClicked || mAutoStartSignInFlow) {
                mAutoStartSignInFlow = false;
                mSignInClicked = false;

                mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this,
                        mGoogleApiClient, connectionResult, RC_SIGN_IN,
                        getString(R.string.signin_other_error));
            }
        }

    }

    @Override
    public void onTurnBasedMatchReceived(TurnBasedMatch turnBasedMatch) {
        updateMatch(turnBasedMatch);
    }

    @Override
    public void onTurnBasedMatchRemoved(String s) {

    }

    @Override
    public void replayGame() {
        onGamesInProgress();
    }

    @Override
    public void shareGame() {
        sharer.shareGameImage(this);
    }

    @Override
    public void invite() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .setCustomImage(GoldTrapApplication.getInstance().getGamePreviewUri())
                .setDeepLink(Uri.parse(getString(R.string.multiplayer_deeplink)))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    public void next() {
        finish();
    }

    private void processResult(TurnBasedMultiplayer.CancelMatchResult result) {
        dismissSpinner();

        if (!checkStatusCode(result.getStatus().getStatusCode())) {
            return;
        }

        isDoingTurn = false;

        showMessage("This match is canceled.  All other players will have their game ended.");
    }

    private void processResult(TurnBasedMultiplayer.InitiateMatchResult result) {
        TurnBasedMatch match = result.getMatch();
        dismissSpinner();
        if (checkStatusCode(result.getStatus().getStatusCode())) {
            startMatch(match);
        }
    }

    public void processResult(TurnBasedMultiplayer.UpdateMatchResult result,
                              boolean updateFragment) {
        TurnBasedMatch match = result.getMatch();
        dismissSpinner();
        if (checkStatusCode(result.getStatus().getStatusCode())) {
            if (updateFragment) {
                updateMatch(match);
            }
            else {
                tbm = match;
            }
        }
    }

    private Level getMyLevel(int resourceId) {
        InputStream inputStream = getResources().openRawResource(resourceId);
        return gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), Level.class);
    }

    public void startMatch(TurnBasedMatch match) {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            tbm = match;
            Level level = getMyLevel(R.raw.another_level);
            String playerId = Games.Players.getCurrentPlayerId(mGoogleApiClient);
            String myParticipantId = tbm.getParticipantId(playerId);
            if (null != match.getData()) {
                loadGame(match);
            }
            if (null != gameAndLevelSnapshot) {
                initMatchWithMyCopy(myParticipantId);
            }
            else {
                initializeMatch(level, myParticipantId);
            }
            takeTurn(gameAndLevelSnapshot, myParticipantId, true);
        }
        else {
            showMessage(getString(R.string.connect_to_google_play_games));
        }
    }

    private void initializeMatch(Level level, String myParticipantId) {
        Map<String, DotsGameSnapshot> snapshotMap = new HashMap<>();
        DotsGameSnapshot original = new GameSnapshotCreator().createGameSnapshot(level);
        snapshotMap.put(myParticipantId, original);
        for (String participantId : tbm.getParticipantIds()) {
            if (!participantId.equals(myParticipantId)) {
                DotsGameSnapshot copy =
                        gson.fromJson(gson.toJson(original), DotsGameSnapshot.class);
                snapshotMap.put(participantId, copy);
            }
        }
        gameAndLevelSnapshot = new GameAndLevelSnapshot(snapshotMap, level);
    }

    private void initMatchWithMyCopy(String myParticipantId) {
        Map<String, DotsGameSnapshot> snapshotMap = gameAndLevelSnapshot.getSnapshotMap();
        for (DotsGameSnapshot snapshot : snapshotMap.values()) {
            DotsGameSnapshot copy =
                    gson.fromJson(gson.toJson(snapshot), DotsGameSnapshot.class);
            snapshotMap.put(myParticipantId, copy);
        }
    }

    private void loadGame(TurnBasedMatch match) {
        try {
            gameAndLevelSnapshot =
                    gson.fromJson(new String(match.getData(), CHARSET_NAME),
                            GameAndLevelSnapshot.class);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException", e);
        }
    }

    private void takeTurn(GameAndLevelSnapshot gameAndLevelSnapshot, String participantId,
                          final boolean updateFragment) {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            gameAndLevelSnapshot.setLastPlayerId(participantId);
            Games.TurnBasedMultiplayer.takeTurn(mGoogleApiClient, tbm.getMatchId(),
                    gson.toJson(gameAndLevelSnapshot).getBytes(Charset.forName(CHARSET_NAME)),
                    participantId)
                    .setResultCallback(
                            new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
                                @Override
                                public void onResult(
                                        @NonNull TurnBasedMultiplayer.UpdateMatchResult result) {
                                    processResult(result, updateFragment);
                                }
                            });
        }
        else {
            showMessage(getString(R.string.connect_to_google_play_games));
        }
    }

    private void updateFragment() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            String playerId = Games.Players.getCurrentPlayerId(mGoogleApiClient);
            final String myParticipantId = tbm.getParticipantId(playerId);
            MultiPlayerGameFragment fragment =
                    (MultiPlayerGameFragment) getSupportFragmentManager().findFragmentByTag(
                            MultiPlayerGameFragment.TAG);
            updateScore(myParticipantId);
            if (null != fragment && fragment.isAdded()) {
                fragment.updateGame(gameAndLevelSnapshot, myParticipantId,
                        tbm.getTurnStatus(), tbm.getStatus());
            }
            else {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container,
                                MultiPlayerGameFragment
                                        .newInstance(gson.toJson(gameAndLevelSnapshot),
                                                myParticipantId, tbm.getTurnStatus(),
                                                tbm.getStatus()),
                                MultiPlayerGameFragment.TAG)
                        .addToBackStack(MultiPlayerGameFragment.TAG)
                        .commit();
                if (!tutorialModel.isTutorialShown()) {
                    tutorialModel.markTutorialShown();
                    showHelp();
                }
            }
        }
    }


    private void showHelp() {
        Intent tutorialActivity =
                new Intent(MultiPlayerActivity.this, MaterialTutorialActivity.class);
        tutorialActivity.putParcelableArrayListExtra(
                MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS,
                tutorialModel.getTutorialItems());
        startActivityForResult(tutorialActivity, REQUEST_CODE);
    }

    private void updateScore(final String myParticipantId) {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            if (tbm.getStatus() == TurnBasedMatch.MATCH_STATUS_COMPLETE) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Log.d(TAG, "Game over. Let's update the score");
                        DotsGameSnapshot snapshot =
                                gameAndLevelSnapshot.getSnapshotMap().get(myParticipantId);
                        ScoreComputer scoreComputer = new ScoreComputerImpl(snapshot);
                        scoreComputer.computeScoreWithResults();
                        scoreModel.updateScore(null, snapshot.getScore(), tbm.getMatchId());
                        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
                            leaderboardsModel
                                    .updateLeaderboards(mGoogleApiClient, snapshot.getScore());
                            achievementsModel
                                    .updateAchievements(mGoogleApiClient, snapshot.getScore());
                        }
                        return null;
                    }
                }.execute();
            }
        }
        else {
            showMessage(getString(R.string.connect_to_google_play_games));
        }
    }

    private void retrieveMatchFromHint(Bundle connectionHint) {
        if (null == tbm && null != connectionHint) {
            TurnBasedMatch match = connectionHint.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH);
            if (null != match) {
                if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
                    tbm = match;
                    Log.d(TAG, "Warning: accessing TurnBasedMatch when not connected");
                }
                else {
                    updateMatch(tbm);
                }
            }
        }
    }

    private void updateMatch(TurnBasedMatch match) {
        tbm = match;
        switch (match.getStatus()) {
            case TurnBasedMatch.MATCH_STATUS_COMPLETE:
            case TurnBasedMatch.MATCH_STATUS_ACTIVE:
                fetchGameData(match);
                updateFragment();
                break;
            default:
                handleInactiveStates(match);
        }
    }

    private void handleInactiveStates(TurnBasedMatch match) {
        switch (match.getStatus()) {
            case TurnBasedMatch.MATCH_STATUS_CANCELED:
                showMessage(getString(R.string.game_was_cancelled));
                break;
            case TurnBasedMatch.MATCH_STATUS_EXPIRED:
                showMessage(getString(R.string.game_is_expired));
                break;
            case TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING:
                showMessage(getString(R.string.auto_matching));
                break;
        }
    }

    private void fetchGameData(TurnBasedMatch match) {
        if (null != match.getData()) {
            try {
                gameAndLevelSnapshot =
                        gson.fromJson(new String(match.getData(), CHARSET_NAME),
                                GameAndLevelSnapshot.class);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {
            gameAndLevelSnapshot = null;
            startMatch(match);
        }
    }

    private void showMessage(String msg) {
        ViewGroup coordinateLayout = (ViewGroup) findViewById(R.id.fragment_container);
        Snackbar.make(coordinateLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    private boolean checkStatusCode(int statusCode) {
        switch (statusCode) {
            case GamesStatusCodes.STATUS_OK:
                return true;
            case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_DEFERRED:
                return true;
            case GamesStatusCodes.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
                showMessage(getResources().getString(
                        R.string.status_multiplayer_error_not_trusted_tester));
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_ALREADY_REMATCHED:
                showMessage(getResources().getString(
                        R.string.match_error_already_rematched));
                break;
            case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_FAILED:
                showMessage(getResources().getString(
                        R.string.network_error_operation_failed));
                break;
            case GamesStatusCodes.STATUS_CLIENT_RECONNECT_REQUIRED:
                showMessage(getResources().getString(
                        R.string.client_reconnect_required));
                break;
            case GamesStatusCodes.STATUS_INTERNAL_ERROR:
                showMessage(getResources().getString(R.string.internal_error));
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_INACTIVE_MATCH:
                showMessage(getResources().getString(
                        R.string.match_error_inactive_match));
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_LOCALLY_MODIFIED:
                showMessage(getResources().getString(
                        R.string.match_error_locally_modified));
                break;
            default:
                showMessage(getResources().getString(R.string.unexpected_status));
                Log.d(TAG, "Did not have warning or string to deal with: "
                        + statusCode);
        }

        return false;
    }

    public String getNextParticipantId() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            String playerId = Games.Players.getCurrentPlayerId(mGoogleApiClient);
            String myParticipantId = tbm.getParticipantId(playerId);

            ArrayList<String> participantIds = tbm.getParticipantIds();

            int desiredIndex = -1;

            for (int i = 0; i < participantIds.size(); i++) {
                if (participantIds.get(i).equals(myParticipantId)) {
                    desiredIndex = i + 1;
                }
            }

            if (desiredIndex < participantIds.size()) {
                return participantIds.get(desiredIndex);
            }

            if (tbm.getAvailableAutoMatchSlots() <= 0) {
                // You've run out of automatch slots, so we start over.
                return participantIds.get(0);
            }
            else {
                // You have not yet fully automatched, so null will find a new
                // person to play against.
                return null;
            }
        }
        else {
            showMessage(getString(R.string.connect_to_google_play_games));
        }
        return null;
    }

    private void handleCreateMatch(int response, Intent data) {
        if (response == Activity.RESULT_OK) {
            final ArrayList<String> invitees = data
                    .getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

            Bundle matchCriteria = null;

            int minAutoMatchPlayers = data.getIntExtra(
                    Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoMatchPlayers = data.getIntExtra(
                    Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

            if (minAutoMatchPlayers > 0) {
                matchCriteria = RoomConfig.createAutoMatchCriteria(
                        minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            }
            else {
                matchCriteria = null;
            }

            // Start the match
            startMatch(TurnBasedMatchConfig.builder()
                    .addInvitedPlayers(invitees)
                    .setAutoMatchCriteria(matchCriteria).build());
            showSpinner();
        }
    }

    private void handleBrowseMatches(int response, Intent data) {
        if (response == Activity.RESULT_OK) {
            TurnBasedMatch match = data
                    .getParcelableExtra(Multiplayer.EXTRA_TURN_BASED_MATCH);
            if (match != null) {
                updateMatch(match);
            }

            Log.d(TAG, "Match = " + match);
        }
    }

    private void handleSignin(int request, int response) {
        mSignInClicked = false;
        mResolvingConnectionFailure = false;
        if (response == Activity.RESULT_OK) {
            mGoogleApiClient.connect();
        }
        else {
            BaseGameUtils.showActivityResultError(this, request, response,
                    R.string.signin_other_error);
        }
    }

    @Override
    public void setTitleAndShowAppbar(int resId) {
        getSupportActionBar().show();
        getSupportActionBar().setTitle(resId);
    }

    @Override
    public void hideAppbar() {
        getSupportActionBar().hide();
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    @Override
    public void helpRequested() {
        showHelp();
    }

    public void showSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
    }

    public void dismissSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.GONE);
    }
}
