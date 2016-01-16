package com.asb.goldtrap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.fragments.multiplayer.MultiPlayerGameFragment;
import com.asb.goldtrap.fragments.multiplayer.MultiPlayerMenuFragment;
import com.asb.goldtrap.models.eo.Level;
import com.asb.goldtrap.models.factory.GameSnapshotCreator;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.snapshots.GameAndLevelSnapshot;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
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

public class MultiPlayerActivity extends AppCompatActivity
        implements MultiPlayerMenuFragment.OnFragmentInteractionListener,
        MultiPlayerGameFragment.OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnInvitationReceivedListener, OnTurnBasedMatchUpdateReceivedListener {

    public static final String TAG = MultiPlayerActivity.class.getSimpleName();
    public static final String CHARSET_NAME = "UTF-8";

    private GoogleApiClient mGoogleApiClient;

    private boolean mResolvingConnectionFailure = false;

    private boolean mSignInClicked = false;

    private boolean mAutoStartSignInFlow = true;

    private TurnBasedMatch mTurnBasedMatch;

    // For our intents
    private static final int RC_SIGN_IN = 9001;
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_LOOK_AT_MATCHES = 10001;

    public boolean isDoingTurn = false;

    public TurnBasedMatch mMatch;
    private Gson gson;
    private GameAndLevelSnapshot gameAndLevelSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
        gson = new Gson();
        // Create the Google API Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, MultiPlayerMenuFragment.newInstance(),
                        MultiPlayerMenuFragment.TAG)
                .commit();
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
        if (request == RC_SIGN_IN) {
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
        else if (request == RC_LOOK_AT_MATCHES) {
            if (response == Activity.RESULT_OK) {
                TurnBasedMatch match = data
                        .getParcelableExtra(Multiplayer.EXTRA_TURN_BASED_MATCH);
                if (match != null) {
                    updateMatch(match);
                }

                Log.d(TAG, "Match = " + match);
            }
        }
        else if (request == RC_SELECT_PLAYERS) {
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
    }
    // Helpful dialogs

    public void showSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
    }

    public void dismissSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.GONE);
    }

    public void askForRematch() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Do you want a rematch?");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Sure, rematch!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                rematch();
                            }
                        })
                .setNegativeButton("No.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

        alertDialogBuilder.show();
    }

    @Override
    public void onAutoMatch() {
        startMatch(TurnBasedMatchConfig.builder()
                .setAutoMatchCriteria(RoomConfig.createAutoMatchCriteria(
                        1, 1, 0)).build());
    }

    private void startMatch(TurnBasedMatchConfig tbmc) {
        Games.TurnBasedMultiplayer.createMatch(mGoogleApiClient, tbmc)
                .setResultCallback(new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
                        processResult(result);
                    }
                });
    }

    @Override
    public void onMyTurnComplete(GameAndLevelSnapshot gameAndLevelSnapshot) {
        showSpinner();
        takeTurn(gameAndLevelSnapshot, getNextParticipantId());
    }

    @Override
    public void gameOver(GameAndLevelSnapshot gameAndLevel, Uri gamePreviewUri) {
        String playerId = Games.Players.getCurrentPlayerId(mGoogleApiClient);
        String myParticipantId = mMatch.getParticipantId(playerId);
        gameAndLevelSnapshot = gameAndLevel;
        gameAndLevel.setLastPlayerId(myParticipantId);
        Games.TurnBasedMultiplayer.finishMatch(mGoogleApiClient, mMatch.getMatchId(),
                gson.toJson(gameAndLevel).getBytes(Charset.forName(CHARSET_NAME)))
                .setResultCallback(new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                        processResult(result);
                    }
                });
    }

    public void rematch() {
        showSpinner();
        Games.TurnBasedMultiplayer.rematch(mGoogleApiClient, mMatch.getMatchId()).setResultCallback(
                new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
                        processResult(result);
                    }
                });
        mMatch = null;
        isDoingTurn = false;
    }

    private void processResult(TurnBasedMultiplayer.CancelMatchResult result) {
        dismissSpinner();

        if (!checkStatusCode(null, result.getStatus().getStatusCode())) {
            return;
        }

        isDoingTurn = false;

        showMessage("This match is canceled.  All other players will have their game ended.");
    }

    private void processResult(TurnBasedMultiplayer.InitiateMatchResult result) {
        TurnBasedMatch match = result.getMatch();
        dismissSpinner();
        if (checkStatusCode(match, result.getStatus().getStatusCode())) {
            startMatch(match);
        }
    }

    public void processResult(TurnBasedMultiplayer.UpdateMatchResult result) {
        TurnBasedMatch match = result.getMatch();
        dismissSpinner();
        if (checkStatusCode(match, result.getStatus().getStatusCode())) {
            updateMatch(match);
        }
    }

    private Level getMyLevel(int resourceId) {
        InputStream inputStream = getResources().openRawResource(resourceId);
        return gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), Level.class);
    }

    public void startMatch(TurnBasedMatch match) {
        mMatch = match;
        Level level = getMyLevel(R.raw.another_level);
        String playerId = Games.Players.getCurrentPlayerId(mGoogleApiClient);
        String myParticipantId = mMatch.getParticipantId(playerId);
        if (null != match.getData()) {
            try {
                gameAndLevelSnapshot =
                        gson.fromJson(new String(match.getData(), CHARSET_NAME),
                                GameAndLevelSnapshot.class);
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "UnsupportedEncodingException", e);
            }
        }
        if (null != gameAndLevelSnapshot) {
            Map<String, DotsGameSnapshot> snapshotMap = gameAndLevelSnapshot.getSnapshotMap();
            for (DotsGameSnapshot snapshot : snapshotMap.values()) {
                DotsGameSnapshot copy =
                        gson.fromJson(gson.toJson(snapshot), DotsGameSnapshot.class);
                snapshotMap.put(myParticipantId, copy);
            }
        }
        else {
            Map<String, DotsGameSnapshot> snapshotMap = new HashMap<>();
            DotsGameSnapshot original = new GameSnapshotCreator().createGameSnapshot(level);
            snapshotMap.put(myParticipantId, original);
            for (String participantId : mMatch.getParticipantIds()) {
                if (!participantId.equals(myParticipantId)) {
                    DotsGameSnapshot copy =
                            gson.fromJson(gson.toJson(original), DotsGameSnapshot.class);
                    snapshotMap.put(participantId, copy);
                }
            }
            gameAndLevelSnapshot = new GameAndLevelSnapshot(snapshotMap, level);
        }
        takeTurn(gameAndLevelSnapshot, myParticipantId);
    }

    private void takeTurn(GameAndLevelSnapshot gameAndLevelSnapshot, String participantId) {
        gameAndLevelSnapshot.setLastPlayerId(participantId);
        Games.TurnBasedMultiplayer.takeTurn(mGoogleApiClient, mMatch.getMatchId(),
                gson.toJson(gameAndLevelSnapshot).getBytes(Charset.forName(CHARSET_NAME)),
                participantId)
                .setResultCallback(
                        new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
                            @Override
                            public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                                processResult(result);
                            }
                        });
    }

    private void updateFragment(GameAndLevelSnapshot gameAndLevelSnapshot) {
        String playerId = Games.Players.getCurrentPlayerId(mGoogleApiClient);
        String myParticipantId = mMatch.getParticipantId(playerId);
        MultiPlayerGameFragment fragment =
                (MultiPlayerGameFragment) getSupportFragmentManager().findFragmentByTag(
                        MultiPlayerGameFragment.TAG);
        if (null == fragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,
                            MultiPlayerGameFragment.newInstance(gson.toJson(gameAndLevelSnapshot),
                                    myParticipantId, mMatch.getTurnStatus()),
                            MultiPlayerMenuFragment.TAG)
                    .commit();
        }
        else {
            fragment.updateSnapshot(gameAndLevelSnapshot);
        }
    }

    @Override
    public void onStartMatch() {
        Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(mGoogleApiClient,
                1, 1, true);
        startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    @Override
    public void onGamesInProgress() {
        Intent intent = Games.TurnBasedMultiplayer.getInboxIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_LOOK_AT_MATCHES);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        dismissSpinner();
        Log.d(TAG, "onConnected(): Connection successful");

        // Retrieve the TurnBasedMatch from the connectionHint
        if (connectionHint != null) {
            mTurnBasedMatch = connectionHint.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH);

            if (mTurnBasedMatch != null) {
                if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
                    Log.d(TAG, "Warning: accessing TurnBasedMatch when not connected");
                }
                updateMatch(mTurnBasedMatch);
            }
        }
        Games.Invitations.registerInvitationListener(mGoogleApiClient, this);
        Games.TurnBasedMultiplayer.registerMatchUpdateListener(mGoogleApiClient, this);
    }

    private void updateMatch(TurnBasedMatch match) {
        mMatch = match;
        String playerId = Games.Players.getCurrentPlayerId(mGoogleApiClient);
        String myParticipantId = mMatch.getParticipantId(playerId);
        int status = match.getStatus();
        int turnStatus = match.getTurnStatus();

        switch (status) {
            case TurnBasedMatch.MATCH_STATUS_CANCELED:
                showMessage("This game was canceled!");
                break;
            case TurnBasedMatch.MATCH_STATUS_EXPIRED:
                showMessage("This game is expired.  So sad!");
                break;
            case TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING:
                showMessage("We're still waiting for an automatch partner.");
                break;
            case TurnBasedMatch.MATCH_STATUS_COMPLETE:
                fetchGameData(match);
                if (!gameAndLevelSnapshot.getLastPlayerId().equals(myParticipantId)) {
                    updateFragment(gameAndLevelSnapshot);
                }
        }
        if (TurnBasedMatch.MATCH_STATUS_ACTIVE == status) {
            fetchGameData(match);
            switch (turnStatus) {
                case TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN:
                    updateFragment(gameAndLevelSnapshot);
                    break;
                case TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN:
                    updateFragment(gameAndLevelSnapshot);
                    showMessage("It's not your turn.");
                    break;
                case TurnBasedMatch.MATCH_TURN_STATUS_INVITED:
                    showMessage("Still waiting for invitations.\n\nBe patient!");
            }
        }
    }

    private void fetchGameData(TurnBasedMatch match) {
        try {
            gameAndLevelSnapshot =
                    gson.fromJson(new String(match.getData(), CHARSET_NAME),
                            GameAndLevelSnapshot.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String msg) {
        ViewGroup coordinateLayout = (ViewGroup) findViewById(R.id.fragment_container);
        Snackbar.make(coordinateLayout, msg, Snackbar.LENGTH_SHORT).show();
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
            // Already resolving
            Log.d(TAG, "onConnectionFailed(): ignoring connection failure, already resolving.");
        }
        else {
            // Launch the sign-in flow if the button was clicked or if auto sign-in is enabled
            if (mSignInClicked || mAutoStartSignInFlow) {
                mAutoStartSignInFlow = false;
                mSignInClicked = false;

                mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this,
                        mGoogleApiClient, connectionResult, RC_SIGN_IN,
                        getString(R.string.signin_other_error));
            }
        }

    }

    private boolean checkStatusCode(TurnBasedMatch match, int statusCode) {
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

    @Override
    public void onInvitationReceived(Invitation invitation) {

    }

    @Override
    public void onInvitationRemoved(String s) {

    }

    @Override
    public void onTurnBasedMatchReceived(TurnBasedMatch turnBasedMatch) {
        updateMatch(turnBasedMatch);
    }

    @Override
    public void onTurnBasedMatchRemoved(String s) {

    }

    public String getNextParticipantId() {

        String playerId = Games.Players.getCurrentPlayerId(mGoogleApiClient);
        String myParticipantId = mMatch.getParticipantId(playerId);

        ArrayList<String> participantIds = mMatch.getParticipantIds();

        int desiredIndex = -1;

        for (int i = 0; i < participantIds.size(); i++) {
            if (participantIds.get(i).equals(myParticipantId)) {
                desiredIndex = i + 1;
            }
        }

        if (desiredIndex < participantIds.size()) {
            return participantIds.get(desiredIndex);
        }

        if (mMatch.getAvailableAutoMatchSlots() <= 0) {
            // You've run out of automatch slots, so we start over.
            return participantIds.get(0);
        }
        else {
            // You have not yet fully automatched, so null will find a new
            // person to play against.
            return null;
        }
    }
}
