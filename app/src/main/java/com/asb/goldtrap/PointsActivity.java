package com.asb.goldtrap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.asb.goldtrap.fragments.points.PointsFragment;

public class PointsActivity extends AppCompatActivity
        implements PointsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container,
                        PointsFragment.newInstance(),
                        PointsFragment.TAG)
                .commit();
    }

}
