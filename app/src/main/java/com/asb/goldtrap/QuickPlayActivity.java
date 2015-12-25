package com.asb.goldtrap;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.fragments.postgame.ScoreFragment;
import com.asb.goldtrap.fragments.postgame.SummaryFragment;
import com.asb.goldtrap.fragments.pregame.TasksDisplayFragment;
import com.asb.goldtrap.fragments.quickplay.QuickPlayGameFragment;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.utils.sharer.Sharer;
import com.asb.goldtrap.models.utils.sharer.impl.SharerImpl;
import com.google.android.gms.appinvite.AppInviteInvitation;

public class QuickPlayActivity extends AppCompatActivity
        implements QuickPlayGameFragment.OnFragmentInteractionListener,
        TasksDisplayFragment.OnFragmentInteractionListener,
        ScoreFragment.OnFragmentInteractionListener,
        SummaryFragment.OnFragmentInteractionListener {

    private static final String TAG = QuickPlayActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 15001;
    private GoldTrapApplication goldTrapApplication;
    private Sharer sharer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goldTrapApplication = (GoldTrapApplication) getApplication();
        sharer = new SharerImpl();
        setContentView(R.layout.activity_quick_play);
        if (null == getSupportFragmentManager().findFragmentByTag(TasksDisplayFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(QuickPlayGameFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(ScoreFragment.TAG) &&
                null == getSupportFragmentManager().findFragmentByTag(SummaryFragment.TAG)) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container,
                            TasksDisplayFragment.newInstance(R.raw.level),
                            TasksDisplayFragment.TAG)
                    .commit();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Check how many invitations were sent and log a message
                // The ids array contains the unique invitation ids for each invitation sent
                // (one for each contact select by the user). You can use these for analytics
                // as the ID will be consistent on the sending and receiving devices.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                showMessage(getString(R.string.sent_invitations_fmt, ids.length));
                Log.d(TAG, getString(R.string.sent_invitations_fmt, ids.length));
            }
            else {
                // Sending failed or it was canceled, show failure message to the user
                showMessage(getString(R.string.send_failed));
            }
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
        sharer.shareGameImage(this);
    }

    @Override
    public void invite() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    public void next() {

    }

    private void showMessage(String msg) {
        ViewGroup container = (ViewGroup) findViewById(R.id.snackbar_layout);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }
}
