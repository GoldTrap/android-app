package com.asb.goldtrap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.asb.goldtrap.fragments.play.BrowseEpisodesFragment;
import com.asb.goldtrap.fragments.play.BrowseLevelsFragment;
import com.asb.goldtrap.models.eo.migration.Episode;
import com.asb.goldtrap.models.eo.migration.Level;

/**
 * Browse Activity.
 * Created by arjun on 05/06/2016
 */
public class BrowseActivity extends AppCompatActivity
        implements BrowseEpisodesFragment.OnFragmentInteractionListener,
        BrowseLevelsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,
                        BrowseEpisodesFragment.newInstance(),
                        BrowseEpisodesFragment.TAG)
                .commit();
    }

    @Override
    public void onEpisodeClicked(Episode episode) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container,
                        BrowseLevelsFragment.newInstance(episode.getCode(), episode.getName()),
                        BrowseLevelsFragment.TAG)
                .addToBackStack(BrowseLevelsFragment.TAG)
                .commit();
    }

    @Override
    public void onLevelClicked(Level level) {
        if (!level.isLocked()) {
            int levelResourceCode = getResources()
                    .getIdentifier(level.getCode(), "raw", getPackageName());
            if (0 < levelResourceCode) {
                Intent intent = new Intent(getBaseContext(), PlayActivity.class);
                intent.putExtra(PlayActivity.LEVEL_RESOURCE_CODE, levelResourceCode);
                intent.putExtra(PlayActivity.LEVEL_CODE, level.getCode());
                startActivity(intent);
            }
            else {
                showMessage(getString(R.string.stay_tuned));
            }
        }
        else {
            // TODO: Take him to the store
        }
    }

    private void showMessage(String msg) {
        ViewGroup frameLayout = (ViewGroup) findViewById(R.id.fragment_container);
        Snackbar.make(frameLayout, msg, Snackbar.LENGTH_SHORT).show();
    }
}
