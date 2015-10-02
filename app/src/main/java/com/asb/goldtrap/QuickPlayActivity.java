package com.asb.goldtrap;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.asb.goldtrap.fragments.quickplay.QuickPlayGameFragment;

public class QuickPlayActivity extends AppCompatActivity
        implements QuickPlayGameFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_play);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, QuickPlayGameFragment.newInstance(),
                        QuickPlayGameFragment.TAG)
                .commit();
    }

    @Override
    public void gameOver() {

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
}
