package com.asb.goldtrap;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.fragments.launch.HomeFragment;
import com.asb.goldtrap.fragments.launch.LaunchFragment;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.example.games.basegameutils.BaseGameUtils;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LaunchFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener, OnTurnBasedMatchUpdateReceivedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;
    private boolean mSignInComplete = false;
    private boolean migrationComplete = false;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addApi(AppInvite.API).addScope(Games.SCOPE_GAMES)
                .build();

        if (!isHomeFragmentVisible()) {
            if (null == getSupportFragmentManager().findFragmentByTag(LaunchFragment.TAG)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, LaunchFragment.newInstance(),
                                LaunchFragment.TAG)
                        .commit();
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        dismissSpinner();
        Log.d(TAG, "onConnected():  Connected man.");
        if (connectionHint != null) {
            TurnBasedMatch turnBasedMatch =
                    connectionHint.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH);

            if (turnBasedMatch != null) {
                multiPlayerGameFromNotification(turnBasedMatch);
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mSignInComplete = true;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            }
            else {
                loadHomeScreen();
            }
        }
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

    private void loadHomeScreen() {
        if (!isHomeFragmentVisible()) {
            if (migrationComplete) {
                Fragment fragment = HomeFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, fragment, HomeFragment.TAG)
                        .commit();
            }
        }
    }

    @Override
    public void signOut() {
        mSignInClicked = false;
        mSignInComplete = false;
        Games.signOut(mGoogleApiClient);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean isConnected() {
        return null != mGoogleApiClient && mGoogleApiClient.isConnected();
    }

    @Override
    public boolean isSignInInProgress() {
        return mSignInClicked && !mSignInComplete;
    }

    @Override
    public void quickPlay() {
        Intent quickPlay = new Intent(this, QuickPlayActivity.class);
        startActivity(quickPlay);
    }

    @Override
    public void multiPlayerGame() {
        Intent multiPlayer = new Intent(this, MultiPlayerActivity.class);
        startActivity(multiPlayer);
    }

    @Override
    public void launch() {
        loadHomeScreen();
    }

    @Override
    public void migrationComplete() {
        migrationComplete = true;
        if (mSignInComplete) {
            loadHomeScreen();
        }
    }

    private boolean isHomeFragmentVisible() {
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        return null != homeFragment && homeFragment.isVisible();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected()) {
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

    private void showMessage(String msg) {
        ViewGroup coordinateLayout = (ViewGroup) findViewById(R.id.fragment_container);
        Snackbar.make(coordinateLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    public void showSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
    }

    public void dismissSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.GONE);
    }

    @Override
    public void onTurnBasedMatchReceived(TurnBasedMatch turnBasedMatch) {
        multiPlayerGameFromNotification(turnBasedMatch);
    }

    private void multiPlayerGameFromNotification(TurnBasedMatch turnBasedMatch) {
        Intent multiPlayer = new Intent(this, MultiPlayerActivity.class);
        multiPlayer.putExtra(Multiplayer.EXTRA_TURN_BASED_MATCH, turnBasedMatch);
        startActivity(multiPlayer);
    }

    @Override
    public void onTurnBasedMatchRemoved(String s) {

    }
}
