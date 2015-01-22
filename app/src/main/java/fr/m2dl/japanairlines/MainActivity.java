package fr.m2dl.japanairlines;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import fr.m2dl.japanairlines.domain.Obstacle;
import fr.m2dl.japanairlines.domain.Plane;
import fr.m2dl.japanairlines.services.BlowRecorder;
import fr.m2dl.japanairlines.services.HeightManager;
import fr.m2dl.japanairlines.services.RandomGenerator;


public class MainActivity extends Activity implements SensorEventListener {

    private boolean isGameOver = false;
    private boolean isStarted;
    private LinearLayout layout2;
    private LinearLayout layout;
    private RelativeLayout mainLayout;
    private Handler mHandler;
    private Runnable mStatusChecker;
    private SensorManager sensorManager;
    private ImageView planeImage;
    private ImageView roadImage;

    private Sensor accelerometer;
    private TextView altimeterValue;

    private BlowRecorder blowRecorder;

    private HeightManager heightManager;
    private Plane plane;

    private int screenHeight;
    private int screenWidth;

    public static Activity activity;
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
    this.activity = this;
        isGameOver = false;
        plane = new Plane();
        heightManager = new HeightManager(plane, this);

        if(!isStarted){

            startBlowRecording();
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout2 = new LinearLayout(this);

        layout2.setLayoutParams(layout.getLayoutParams());
        layout2.setBackgroundResource(R.drawable.grass);


        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

        TypedValue tv = new TypedValue();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                screenHeight -= TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            screenHeight -= getResources().getDimensionPixelSize(resourceId);

        layout2.setY(-screenHeight);
        layout2.setX(layout.getX());

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);



        planeImage = (ImageView) findViewById(R.id.plane);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenHeight/7, screenHeight/7);
        planeImage.setLayoutParams(layoutParams);
        planeImage.setX(screenWidth/2);
        planeImage.setY(screenHeight-screenWidth/4);


        roadImage = (ImageView) findViewById(R.id.road);
        mHandler = new Handler();
        altimeterValue = (TextView) findViewById(R.id.altimeterValue);


        mainLayout.addView(layout2, 0);


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                affichage();

                mStatusChecker.run();
            }
        });
        t.start();

        int i =0;
        for(Obstacle o :RandomGenerator.generate()){
            putObstacle(o);
            ++i;
        }
        putObstacle(new Obstacle(-2,i));

    }

    private void startBlowRecording() {
            Thread blowRecorderThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    blowRecorder = new BlowRecorder();
                    while (!isGameOver) {
                        blowRecorder.recordBlow();
                        heightManager.movePlaneUp();
                    }
                    blowRecorder.stopRecording();
                }
            });
            blowRecorderThread.start();


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

                layout.setBackgroundResource(R.drawable.grass);

                //roadImage.setY(roadImage.getY()+10);
                layout.setY(layout.getY()+10);
                layout2.setY(layout2.getY()+10);





//                Log.d("",layout.getY()+"---"+screenHeight+"--"+ screenWidth);
                if(layout.getY()>= screenHeight){
                    layout.setY(-screenHeight);
                    roadImage.setVisibility(View.INVISIBLE);
                }
                if(layout2.getY()>=screenHeight){
                    layout2.setY(- screenHeight);
                }


//                i1.setImageResource(R.drawable.grass);
//                layout.addView(i1, 0);


                mHandler.postDelayed(mStatusChecker, 30);
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



    public void putObstacle(Obstacle obstacleObject){

        if(obstacleObject.isVide()){
            return;
        }
        final Obstacle copiePourPiste = obstacleObject;

        float x = obstacleObject.getX();
        float y = obstacleObject.getY();
        final ImageView obstacle = new ImageView(this);
        final Runnable mRunnableObstalce;

        if(obstacleObject.isPisteAtter()){

            obstacle.setImageResource(R.drawable.roadatter);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenHeight);
            obstacle.setLayoutParams(layoutParams);


        }else{

            obstacle.setImageResource(R.drawable.obstacle);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth/3, screenHeight/5);
            obstacle.setLayoutParams(layoutParams);


        }



        mRunnableObstalce = new Runnable() {
            @Override
            public void run() {
                if(copiePourPiste.isPisteAtter() && isInBox(planeImage.getX(), planeImage.getY(), planeImage.getWidth(), planeImage.getHeight(), obstacle.getX(), obstacle.getY(),obstacle.getWidth(), obstacle.getHeight())){
                    Log.d("","Gagné");
                }
                if(!copiePourPiste.isPisteAtter() &&!isGameOver && isInBox(planeImage.getX(), planeImage.getY(), planeImage.getWidth(), planeImage.getHeight(), obstacle.getX(), obstacle.getY(),obstacle.getWidth(), obstacle.getHeight())){

                    isGameOver = true;
                    planeImage.setBackgroundResource(R.drawable.explose);
                    Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);

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


        mainLayout.addView(obstacle,2);

        if(obstacleObject.isPisteAtter()){

            obstacle.setX(0);
            obstacle.setY(-y*(screenHeight/3));

        }else
        {
            obstacle.setX(x*(screenWidth/3));
            obstacle.setY(-y*(screenHeight/5));

        }

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

    @Override
    protected void onStop() {
        super.onStop();
        blowRecorder.stopRecording();
    }
}
