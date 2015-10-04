package com.asb.goldtrap;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.asb.goldtrap.fragments.launch.HomeFragment;
import com.asb.goldtrap.fragments.launch.LaunchFragment;
import com.asb.goldtrap.models.utils.NetworkUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LaunchFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
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
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
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
    public void onConnected(Bundle bundle) {
        loadHomeScreen();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Suspended: " + i);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed(): attempting to resolve");
        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed(): already resolving, ignoring");
            return;
        }

        if (mSignInClicked) {
            mSignInClicked = false;
            mSignInComplete = false;
            mResolvingConnectionFailure = true;
            if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }
        // Put code here to display the sign-in button
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

    @Override
    public void signIn() {
        doSignIn();
    }


    private void doSignIn() {
        if (NetworkUtils.isConnected(getApplicationContext())) {
            mSignInClicked = true;
            mSignInComplete = false;
            mGoogleApiClient.connect();
        }
        else {
            loadHomeScreen();
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

}
