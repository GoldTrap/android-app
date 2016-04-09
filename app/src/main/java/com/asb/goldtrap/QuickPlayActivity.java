package com.asb.goldtrap;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.fragments.game.GameFragment;
import com.asb.goldtrap.fragments.postgame.ScoreFragment;
import com.asb.goldtrap.fragments.postgame.SummaryFragment;
import com.asb.goldtrap.fragments.pregame.TasksDisplayFragment;
import com.asb.goldtrap.models.achievements.AchievementsModel;
import com.asb.goldtrap.models.achievements.impl.QuickPlayAchievementsModel;
import com.asb.goldtrap.models.leaderboards.LeaderboardsModel;
import com.asb.goldtrap.models.leaderboards.impl.LeaderboardsModelImpl;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.scores.ScoreModel;
import com.asb.goldtrap.models.scores.impl.QuickPlayScoreModelImpl;
import com.asb.goldtrap.models.utils.NetworkUtils;
import com.asb.goldtrap.models.utils.sharer.Sharer;
import com.asb.goldtrap.models.utils.sharer.impl.SharerImpl;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

public class QuickPlayActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GameFragment.OnFragmentInteractionListener,
        TasksDisplayFragment.OnFragmentInteractionListener,
        ScoreFragment.OnFragmentInteractionListener,
        SummaryFragment.OnFragmentInteractionListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = QuickPlayActivity.class.getSimpleName();
    private static int RC_SIGN_IN = 11001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private static final int REQUEST_INVITE = 15001;
    private GoldTrapApplication goldTrapApplication;
    private Sharer sharer;
    private GoogleApiClient mGoogleApiClient;
    private ScoreModel scoreModel;
    private LeaderboardsModel leaderboardsModel;
    private AchievementsModel achievementsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goldTrapApplication = (GoldTrapApplication) getApplication();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addApi(AppInvite.API).addScope(Games.SCOPE_GAMES)
                .build();
        sharer = new SharerImpl();
        scoreModel = new QuickPlayScoreModelImpl(getApplicationContext());
        leaderboardsModel = new LeaderboardsModelImpl(getApplicationContext());
        achievementsModel = new QuickPlayAchievementsModel();
        setContentView(R.layout.activity_quick_play);
        if (null == getSupportFragmentManager().findFragmentByTag(TasksDisplayFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(GameFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(ScoreFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(SummaryFragment.TAG)) {
            startANewGame();
        }

        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                // Because autoLaunchDeepLink = true we don't have to do anything
                                // here, but we could set that to false and manually choose
                                // an Activity to launch to handle the deep link here.
                            }
                        });
    }

    private void startANewGame() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container,
                        TasksDisplayFragment.newInstance(R.raw.level, ""),
                        TasksDisplayFragment.TAG)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == RC_SIGN_IN) {
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            }
        }
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Check how many invitations were sent and log a message
                // The ids array contains the unique invitation ids for each invitation sent
                // (one for each contact select by the user). You can use these for analytics
                // as the ID will be consistent on the sending and receiving devices.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                Log.d(TAG, getString(R.string.sent_invitations_fmt, ids.length));
            }
            else {
                // Sending failed or it was canceled, show failure message to the user
                showMessage(getString(R.string.send_failed));
            }
        }
    }

    @Override
    public void gameOver(String levelCode, String snapshot, Uri gamePreviewUri) {
        goldTrapApplication.setGamePreviewUri(gamePreviewUri);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container,
                        ScoreFragment.newInstance(snapshot, levelCode),
                        ScoreFragment.TAG)
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
    public void tasksShownAcknowledgement(int levelResourceCode, String levelCode) {
        if (null == getSupportFragmentManager().findFragmentByTag(GameFragment.TAG)) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container,
                            GameFragment.newInstance(levelResourceCode, levelCode),
                            GameFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onScoreViewed(final Score score, final String levelCode) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                scoreModel.updateScore(levelCode, score, null);
                leaderboardsModel.updateLeaderboards(mGoogleApiClient, score);
                achievementsModel.updateAchievements(mGoogleApiClient, score);
                return null;
            }
        }.execute();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container,
                        SummaryFragment.newInstance(goldTrapApplication.getGamePreviewUri()),
                        SummaryFragment.TAG)
                .commit();
    }

    @Override
    public void replayGame() {
        startANewGame();
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
                .setCustomImage(goldTrapApplication.getGamePreviewUri())
                .setDeepLink(Uri.parse(getString(R.string.quick_play_deeplink)))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    public void next() {
        finish();
    }

    private void showMessage(String msg) {
        ViewGroup coordinateLayout = (ViewGroup) findViewById(R.id.fragment_container);
        Snackbar.make(coordinateLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    public boolean isConnected() {
        return null != mGoogleApiClient && mGoogleApiClient.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected() && NetworkUtils.isConnected(getApplicationContext())) {
            Log.d(TAG, "onStart(): Connecting to Google APIs");
            mGoogleApiClient.connect();
            showSpinner();
        }
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
    public void onConnected(Bundle bundle) {
        dismissSpinner();
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
            if (mAutoStartSignInFlow) {
                mAutoStartSignInFlow = false;

                mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this,
                        mGoogleApiClient, connectionResult, RC_SIGN_IN,
                        getString(R.string.signin_other_error));
            }
        }

    }

    public void showSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
    }

    public void dismissSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.GONE);
    }

}
