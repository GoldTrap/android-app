package com.asb.goldtrap;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.asb.goldtrap.fragments.postgame.ScoreFragment;
import com.asb.goldtrap.fragments.postgame.SummaryFragment;
import com.asb.goldtrap.fragments.pregame.TasksDisplayFragment;
import com.asb.goldtrap.fragments.quickplay.QuickPlayGameFragment;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;

public class QuickPlayActivity extends AppCompatActivity
        implements QuickPlayGameFragment.OnFragmentInteractionListener,
        TasksDisplayFragment.OnFragmentInteractionListener,
        ScoreFragment.OnFragmentInteractionListener,
        SummaryFragment.OnFragmentInteractionListener {

    private static final String TAG = QuickPlayActivity.class.getSimpleName();
    private GoldTrapApplication goldTrapApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goldTrapApplication = (GoldTrapApplication) getApplication();
        setContentView(R.layout.activity_quick_play);
        if (null == getSupportFragmentManager().findFragmentByTag(TasksDisplayFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(QuickPlayGameFragment.TAG)) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container,
                            TasksDisplayFragment.newInstance(R.raw.level),
                            TasksDisplayFragment.TAG)
                    .commit();
        }

    }

    @Override
    public void gameOver(DotsGameSnapshot snapshot) {
        goldTrapApplication.setDotsGameSnapshot(snapshot);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container,
                        ScoreFragment.newInstance(),
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
    public void tasksShownAcknowledgement() {
        if (null == getSupportFragmentManager().findFragmentByTag(QuickPlayGameFragment.TAG)) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container,
                            QuickPlayGameFragment.newInstance(R.raw.level),
                            QuickPlayGameFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onScoreViewed() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container,
                        SummaryFragment.newInstance(
                                goldTrapApplication.getDotsGameSnapshot().getImageUri()),
                        SummaryFragment.TAG)
                .commit();
    }

    @Override
    public void replayGame() {

    }

    @Override
    public void shareGame() {

    }

    @Override
    public void invite() {

    }

    @Override
    public void next() {

    }
}
