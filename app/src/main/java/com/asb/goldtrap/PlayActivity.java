package com.asb.goldtrap;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.fragments.game.GameFragment;
import com.asb.goldtrap.fragments.postgame.ScoreFragment;
import com.asb.goldtrap.fragments.postgame.SummaryFragment;
import com.asb.goldtrap.fragments.pregame.TasksDisplayFragment;
import com.asb.goldtrap.models.achievements.AchievementsModel;
import com.asb.goldtrap.models.achievements.impl.PlayAchievementsModel;
import com.asb.goldtrap.models.eo.BoosterType;
import com.asb.goldtrap.models.gameplay.GameTypes;
import com.asb.goldtrap.models.leaderboards.LeaderboardsModel;
import com.asb.goldtrap.models.leaderboards.impl.LeaderboardsModelImpl;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.scores.ScoreModel;
import com.asb.goldtrap.models.scores.impl.PlayScoreModelImpl;
import com.asb.goldtrap.models.tutorial.TutorialModel;
import com.asb.goldtrap.models.tutorial.impl.TutorialModelImpl;
import com.asb.goldtrap.models.utils.NetworkUtils;
import com.asb.goldtrap.models.utils.sharer.Sharer;
import com.asb.goldtrap.models.utils.sharer.impl.SharerImpl;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.stats.PlayerStats;
import com.google.android.gms.games.stats.Stats;
import com.google.example.games.basegameutils.BaseGameUtils;

import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class PlayActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        TasksDisplayFragment.OnFragmentInteractionListener,
        GameFragment.OnFragmentInteractionListener,
        ScoreFragment.OnFragmentInteractionListener,
        SummaryFragment.OnFragmentInteractionListener {

    private static final String TAG = PlayActivity.class.getSimpleName();
    public static final String LEVEL_RESOURCE_CODE = "levelResourceCode";
    public static final String LEVEL_CODE = "levelCode";
    private static final int REQUEST_CODE = 21000;
    private static int RC_SIGN_IN = 10001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_INVITE = 16001;
    private Sharer sharer;
    private ScoreModel scoreModel;
    private LeaderboardsModel leaderboardsModel;
    private AchievementsModel achievementsModel;
    private TutorialModel tutorialModel;
    private PlayerStats playerStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        sharer = new SharerImpl();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        MobileAds.initialize(getApplicationContext(), getString(R.string.application_id));
        scoreModel = new PlayScoreModelImpl(getApplicationContext());
        leaderboardsModel = new LeaderboardsModelImpl(getApplicationContext());
        achievementsModel = new PlayAchievementsModel(getApplicationContext());
        tutorialModel = new TutorialModelImpl(getApplicationContext(), GameTypes.PLAY);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API)
                .addApi(AppInvite.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
        if (null == getSupportFragmentManager().findFragmentByTag(TasksDisplayFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(GameFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(ScoreFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(SummaryFragment.TAG)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,
                            TasksDisplayFragment
                                    .newInstance(getIntent()
                                                    .getIntExtra(LEVEL_RESOURCE_CODE, R.raw.e01c01),
                                            getIntent().getStringExtra(LEVEL_CODE)),
                            TasksDisplayFragment.TAG)
                    .commit();
        }
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
    public void onBackPressed() {
        if (0 == getSupportFragmentManager().getBackStackEntryCount()) {
            this.finish();
        }
        else {
            getSupportFragmentManager().popBackStack();
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
        if (!tutorialModel.isTutorialShown()) {
            tutorialModel.markTutorialShown();
            showHelp();
        }
    }

    private void showHelp() {
        Intent tutorialActivity = new Intent(PlayActivity.this, MaterialTutorialActivity.class);
        tutorialActivity.putParcelableArrayListExtra(
                MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS,
                tutorialModel.getTutorialItems());
        startActivityForResult(tutorialActivity, REQUEST_CODE);
    }

    @Override
    public void gameOver(String levelCode, String snapshot, Uri gamePreviewUri) {
        GoldTrapApplication.getInstance().setGamePreviewUri(gamePreviewUri);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container,
                        ScoreFragment.newInstance(snapshot, levelCode),
                        ScoreFragment.TAG)
                .commit();
    }

    @Override
    public void takeMeToStore(BoosterType boosterType) {
        Intent intent = new Intent(getApplicationContext(), ShopORamaActivity.class);
        intent.putExtra(ShopORamaActivity.BOOSTER_TYPE, boosterType.name());
        startActivity(intent);
    }

    @Override
    public void onScoreViewed(final Score score, final String levelCode) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                scoreModel.updateScore(levelCode, score, null);
                if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
                    leaderboardsModel.updateLeaderboards(mGoogleApiClient, score);
                    achievementsModel.updateAchievements(mGoogleApiClient, score);
                }
                return null;
            }
        }.execute();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container,
                        SummaryFragment
                                .newInstance(GoldTrapApplication.getInstance().getGamePreviewUri()),
                        SummaryFragment.TAG)
                .commit();
    }

    @Override
    public void replayGame() {
        finish();
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
                .setDeepLink(Uri.parse(getString(R.string.player_deeplink)))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    public void next() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            }
        }
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
        checkPlayerStats();
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
    public void helpRequested() {
        showHelp();
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    private void showMessage(String msg) {
        ViewGroup coordinateLayout = (ViewGroup) findViewById(R.id.fragment_container);
        Snackbar.make(coordinateLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public void showSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
    }

    public void dismissSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.GONE);
    }

}
