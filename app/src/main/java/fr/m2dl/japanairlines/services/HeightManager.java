package fr.m2dl.japanairlines.services;

import java.util.Timer;
import java.util.TimerTask;

import fr.m2dl.japanairlines.MainActivity;
import fr.m2dl.japanairlines.domain.Plane;

/**
 * Created by msoum on 22/01/15.
 */
public class HeightManager {

    private static final long IN_FIVE_SECONDS = 5000;
    private static final long EVERY_SECOND = 1000;
    private static final long NOW = 0;

    private Plane plane;
    private Timer fallTimer;
    private MainActivity parentActivity;

    public HeightManager(final Plane plane, MainActivity parentActivity) {
        this.plane = plane;
        this.parentActivity = parentActivity;
        fallTimer = new Timer();

        TimerTask falling = new TimerTask() {
            @Override
            public void run() {
                plane.moveDown();
            }
        };
        
        fallTimer.schedule(falling, NOW, EVERY_SECOND);
    }

    public void stop() {
        fallTimer.cancel();
        fallTimer.purge();
        fallTimer = null;
    }

    private void restartTimer() {
        TimerTask falling = new TimerTask() {
            @Override
            public void run() {
                plane.moveDown();
                updateUi();
            }
        };

        fallTimer.cancel();
        fallTimer = new Timer();
        fallTimer.schedule(falling, IN_FIVE_SECONDS, EVERY_SECOND);
    }

    private void updateUi() {
        parentActivity.updateHeightView();
    }

    public void movePlaneUp() {
        plane.moveUp();
        restartTimer();
        updateUi();
    }
}
