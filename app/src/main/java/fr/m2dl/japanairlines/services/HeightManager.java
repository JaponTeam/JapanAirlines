package fr.m2dl.japanairlines.services;

import java.util.Timer;
import java.util.TimerTask;

import fr.m2dl.japanairlines.domain.Plane;

/**
 * Created by msoum on 22/01/15.
 */
public class HeightManager {

    private static final long IN_FIVE_SECONDS = 5000;
    private static final long EVERY_FIVE_SECOND = 5000;
    private static final long NOW = 0;

    private Plane plane;
    private Timer fallTimer;

    public HeightManager(final Plane plane) {
        this.plane = plane;
        fallTimer = new Timer();

        TimerTask falling = new TimerTask() {
            @Override
            public void run() {
                plane.moveDown();
            }
        };
        
        fallTimer.schedule(falling, NOW, EVERY_FIVE_SECOND);
    }

    private void restartTimer() {
        TimerTask falling = new TimerTask() {
            @Override
            public void run() {
                plane.moveDown();
            }
        };

        fallTimer.cancel();
        fallTimer = new Timer();
        fallTimer.schedule(falling, IN_FIVE_SECONDS, EVERY_FIVE_SECOND);
    }

    public void movePlaneUp() {
        plane.moveUp();
        restartTimer();
    }
}
