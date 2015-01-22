package fr.m2dl.japanairlines;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import fr.m2dl.japanairlines.domain.Plane;
import fr.m2dl.japanairlines.services.BlowRecorder;
import fr.m2dl.japanairlines.services.HeightManager;


public class MainActivity extends ActionBarActivity {

    private HeightManager heightManager;
    private Plane plane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plane = new Plane();
        heightManager = new HeightManager(plane);
        startBlowRecording();
    }

    private void startBlowRecording() {
        Thread blowRecorder = new Thread(new Runnable() {
            @Override
            public void run() {
                BlowRecorder blowRecorder = new BlowRecorder();
                while (true) {
                    blowRecorder.recordBlow();
                    heightManager.movePlaneUp();
                }
            }
        });
        blowRecorder.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
