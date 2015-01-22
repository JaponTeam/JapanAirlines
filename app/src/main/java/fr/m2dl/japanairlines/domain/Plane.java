package fr.m2dl.japanairlines.domain;

import android.util.Log;

/**
 * Created by msoum on 22/01/15.
 */
public class Plane {
    private static final int MAX_HEIGHT = 3;

    private int currentHeightLevel;
    private int distanceFromStart;
    private int distanceToEnd;

    public int getCurrentHeightLevel() {
        return currentHeightLevel;
    }

    public void moveDown() {
        if (currentHeightLevel > 0) {
            --currentHeightLevel;
        }

        Log.d("", "Plane !! Move DOWN : " + currentHeightLevel);
    }

    public void moveUp() {
        if (currentHeightLevel < MAX_HEIGHT) {
            ++currentHeightLevel;
        }

        Log.d("", "Plane !! Move UP : " + currentHeightLevel);
    }
}
