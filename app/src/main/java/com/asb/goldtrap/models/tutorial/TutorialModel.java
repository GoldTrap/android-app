package com.asb.goldtrap.models.tutorial;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;

/**
 * Tutorial Model.
 * Created by arjun on 09/07/16.
 */
public interface TutorialModel {

    /**
     * Gets the tutorial items.
     *
     * @return array list of tutorial items
     */
    ArrayList<TutorialItem> getTutorialItems();

    /**
     * Indicates if the tutorial is shown
     *
     * @return is shown or not
     */
    boolean isTutorialShown();

    /**
     * Mark tutorial as shown.
     */
    void markTutorialShown();

}
