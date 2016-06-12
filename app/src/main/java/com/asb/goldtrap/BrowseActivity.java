package com.asb.goldtrap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.ViewGroup;

import com.asb.goldtrap.fragments.play.BrowseEpisodesFragment;
import com.asb.goldtrap.fragments.play.BrowseLevelsFragment;
import com.asb.goldtrap.models.dao.PropertiesDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.PropertiesDaoImpl;
import com.asb.goldtrap.models.eo.migration.Episode;
import com.asb.goldtrap.models.eo.migration.Level;
import com.asb.goldtrap.models.iap.IAPCreditor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Browse Activity.
 * Created by arjun on 05/06/2016
 */
public class BrowseActivity extends AbstractPurchaseActivity
        implements BrowseEpisodesFragment.OnFragmentInteractionListener,
        BrowseLevelsFragment.OnFragmentInteractionListener {

    private static final String TAG = BrowseActivity.class.getSimpleName();
    private PropertiesDao propertiesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        propertiesDao = new PropertiesDaoImpl(
                DBHelper.getInstance(getApplicationContext()).getWritableDatabase());
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
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(IAPCreditor.LEVEL_TO_UNLOCK, level.getCode());
                propertiesDao.setValue(PropertiesDao.LEVEL_SELECTED_FOR_UNLOCK, level.getCode());
                this.handleInAppPurchase("unlock_level", 18005, "Let\'s Unlock this thing",
                        jsonObject);
            } catch (JSONException e) {
                Log.e(TAG, "JSON screw up", e);
            }
        }
    }

    @Override
    protected void onConsumptionComplete() {
        showMessage(getString(R.string.transaction_successful));
        finish();
    }

    @Override
    public void receivedBroadcast() {

    }

    private void showMessage(String msg) {
        ViewGroup frameLayout = (ViewGroup) findViewById(R.id.fragment_container);
        Snackbar.make(frameLayout, msg, Snackbar.LENGTH_SHORT).show();
    }
}
