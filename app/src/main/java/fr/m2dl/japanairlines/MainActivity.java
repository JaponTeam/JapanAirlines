package fr.m2dl.japanairlines;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import fr.m2dl.japanairlines.domain.Plane;
import fr.m2dl.japanairlines.services.BlowRecorder;
import fr.m2dl.japanairlines.services.HeightManager;


public class MainActivity extends Activity implements SensorEventListener {

    private LinearLayout layout;
    private RelativeLayout mainLayout;
    private Handler mHandler;
    private Runnable mStatusChecker;
    private SensorManager sensorManager;
    private ImageView planeImage;

    private Sensor accelerometer;
    private TextView altimeterValue;

    private HeightManager heightManager;
    private Plane plane;

    public void updateHeightView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                altimeterValue.setText("+ " + plane.getCurrentHeightLevel());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plane = new Plane();
        heightManager = new HeightManager(plane, this);
        startBlowRecording();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        layout = (LinearLayout) findViewById(R.id.layout);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        planeImage = (ImageView) findViewById(R.id.plane);
        mHandler = new Handler();
        altimeterValue = (TextView) findViewById(R.id.altimeterValue);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                affichage();

                mStatusChecker.run();
            }
        });
        t.start();

        obstacle(200);
        obstacle(100);

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
    protected void onResume() {
        /* Ce qu'en dit Google&#160;dans le cas de l'accéléromètre :
         * «&#160; Ce n'est pas nécessaire d'avoir les évènements des capteurs à un rythme trop rapide.
         * En utilisant un rythme moins rapide (SENSOR_DELAY_UI), nous obtenons un filtre
         * automatique de bas-niveau qui "extrait" la gravité  de l'accélération.
         * Un autre bénéfice étant que l'on utilise moins d'énergie et de CPU.&#160;»
         */
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    public void affichage(){

        mStatusChecker = new Runnable() {
            @Override
            public void run() {

                ImageView i1 = new ImageView(getApplicationContext());



                i1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));



                i1.setImageResource(R.drawable.grass);
                layout.addView(i1, 0);


                mHandler.postDelayed(mStatusChecker, 300);
            }
        };
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
           float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            if(planeImage.getX() <0){
                planeImage.setX(0);
            }
            if(planeImage.getX() > width-planeImage.getWidth()){
                planeImage.setX(width-planeImage.getWidth());
            }

//            Log.d("",x+"--"+y);
            planeImage.setX(planeImage.getX()-x*5);
            planeImage.setX(planeImage.getX()-x*5);

        }


    }

    // I've chosen to not implement this method

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // TODO Auto-generated method stub

    }


    public void obstacle(float placement){
        final ImageView obstacle = new ImageView(this);
        final Runnable mRunnableObstalce;


        obstacle.setImageResource(R.drawable.obstacle);


        mRunnableObstalce = new Runnable() {
            @Override
            public void run() {
                if(isInBox(planeImage.getX(), planeImage.getY(), planeImage.getWidth(), planeImage.getHeight(), obstacle.getX(), obstacle.getY(),obstacle.getWidth(), obstacle.getHeight())){
                    Log.d("","BAM");
                }
                obstacle.setY(obstacle.getY() + 5);

            }
        };


        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() { runOnUiThread(mRunnableObstalce);
            }
        };
        timer.schedule(timerTask, 0, 10);


        mainLayout.addView(obstacle);
        obstacle.setX(placement);
        obstacle.setY(-300);

    }

    public boolean isInBox(float x1, float y1, float width1, float height1, float x2, float y2, float width2, float height2) {


        Rect r1 = new Rect((int)x1,(int)y1,(int)x1+(int)width1,(int)y1+(int)height1);
        Rect r2 = new Rect((int)x2,(int)y2,(int)x2+(int)width2,(int)y2+(int)height2);

        if(r1.intersect(r2)){
            return true;
        }else{
            return false;
        }
    }
}
