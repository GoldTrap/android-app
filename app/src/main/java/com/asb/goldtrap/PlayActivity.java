package com.asb.goldtrap;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.asb.goldtrap.fragments.play.BrowseEpisodesFragment;
import com.asb.goldtrap.fragments.play.BrowseLessonsFragment;
import com.asb.goldtrap.models.eo.migration.Episode;
import com.asb.goldtrap.models.eo.migration.Level;

public class PlayActivity extends AppCompatActivity
        implements BrowseEpisodesFragment.OnFragmentInteractionListener,
        BrowseLessonsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        if (null == getSupportFragmentManager().findFragmentByTag(
                BrowseEpisodesFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(
                        BrowseLessonsFragment.TAG)) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container,
                            BrowseEpisodesFragment.newInstance(),
                            BrowseEpisodesFragment.TAG)
                    .commit();
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
    public void onBackPressed() {
        if (0 == getSupportFragmentManager().getBackStackEntryCount()) {
            this.finish();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onEpisodeClicked(Episode episode) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container,
                        BrowseLessonsFragment.newInstance(episode.getCode(), episode.getName()),
                        BrowseLessonsFragment.TAG)
                .addToBackStack(BrowseLessonsFragment.TAG)
                .commit();
    }

    @Override
    public void onLevelClicked(Level level) {

    }
}
