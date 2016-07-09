package com.asb.goldtrap.models.tutorial.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.dao.PropertiesDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.PropertiesDaoImpl;
import com.asb.goldtrap.models.gameplay.GameTypes;
import com.asb.goldtrap.models.tutorial.TutorialModel;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;

/**
 * Tutorial Model Implementation.
 * Created by arjun on 09/07/16.
 */
public class TutorialModelImpl implements TutorialModel {

    private GameTypes gameTypes;
    private Context context;
    private PropertiesDao propertiesDao;

    public TutorialModelImpl(Context context, GameTypes gameTypes) {
        this.context = context;
        this.gameTypes = gameTypes;
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        propertiesDao = new PropertiesDaoImpl(dbHelper.getWritableDatabase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<TutorialItem> getTutorialItems() {
        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(getClick(context));
        tutorialItems.add(getPoints(context));
        tutorialItems.add(clickFlip(context));
        tutorialItems.add(clickPlusOne(context));
        tutorialItems.add(clickSkip(context));
        return tutorialItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTutorialShown() {
        return (null != propertiesDao.getValue(gameTypes.name()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markTutorialShown() {
        propertiesDao.setValue(gameTypes.name(), String.valueOf(true));
    }

    private TutorialItem clickSkip(Context context) {
        return new TutorialItem(context.getString(R.string.skip_title),
                context.getString(R.string.skip_subtitle),
                R.color.md_blue_A200, R.drawable.skip_tut);
    }

    private TutorialItem clickPlusOne(Context context) {
        return new TutorialItem(context.getString(R.string.plus_one_title),
                context.getString(R.string.plus_one_subtitle),
                R.color.md_blue_A200, R.drawable.plus_one_tut);
    }

    private TutorialItem clickFlip(Context context) {
        return new TutorialItem(context.getString(R.string.flip_title),
                context.getString(R.string.flip_subtitle),
                R.color.md_blue_A200, R.drawable.flip_tut);
    }

    private TutorialItem getPoints(Context context) {
        return new TutorialItem(context.getString(R.string.points_title),
                context.getString(R.string.points_subtitle),
                R.color.md_blue_A200, R.drawable.point_tut);
    }

    private TutorialItem getClick(Context context) {
        return new TutorialItem(context.getString(R.string.click_title),
                context.getString(R.string.click_subtitle),
                R.color.md_blue_A200, R.drawable.click_tut);
    }
}
